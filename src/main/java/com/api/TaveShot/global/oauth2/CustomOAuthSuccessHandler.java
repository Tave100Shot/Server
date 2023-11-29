package com.api.TaveShot.global.oauth2;

import static com.api.TaveShot.global.constant.OauthConstant.REDIRECT_URL;

import com.api.TaveShot.domain.Member.domain.Member;
import com.api.TaveShot.domain.Member.dto.response.AuthResponse;
import com.api.TaveShot.domain.Member.repository.MemberRepository;
import com.api.TaveShot.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {

        CustomOauth2User oauth2User = (CustomOauth2User) authentication.getPrincipal();
        GithubUserInfo githubUserInfo = createGitHubUserInfo(oauth2User);

        if(response.isCommitted()) {
            log.debug("------------------ Response 전송 완료");
        }

        String profileImageUrl = githubUserInfo.getProfileImageUrl();
        String loginId = githubUserInfo.getLoginId();
        Long gitId = githubUserInfo.getId();

        log.info("------------------ "
                + "소셜 로그인 성공: " + loginId
                + "프로필 이미지: " + profileImageUrl);

        Member loginMember = memberRepository.findByGitId(gitId).orElseThrow(() -> new RuntimeException("해당 gitId로 회원을 찾을 수 없음"));
        String loginMemberId = String.valueOf(loginMember.getId());

        registerHeaderToken(response, loginMemberId);

        AuthResponse authResponse = AuthResponse.builder()
                .memberId(loginMember.getId())
                .gitLoginId(loginId)
                .gitProfileImageUrl(profileImageUrl)
                .build();

        // ToDo 아래는 임시 데이터, front와 협의 후 수정
        registerResponse(response, authResponse);
    }

    private void registerHeaderToken(final HttpServletResponse response, final String loginMemberId) {
        String ourToken = jwtProvider.generateJwtToken(loginMemberId);
        // 어세스 토큰은 헤더에 담아서 응답으로 보냄
        response.setHeader("Authorization", ourToken);
    }

    private GithubUserInfo createGitHubUserInfo(final CustomOauth2User oauth2User) {
        Map<String, Object> userInfo = oauth2User.getAttributes();

        return GithubUserInfo.builder()
                .userInfo(userInfo)
                .build();
    }

    private void registerResponse(final HttpServletResponse response,
                                  final AuthResponse authResponse) throws IOException {
        String encodedMemberId = URLEncoder.encode(String.valueOf(authResponse.memberId()), StandardCharsets.UTF_8);
        String encodedLoginId = URLEncoder.encode(authResponse.gitLoginId(), StandardCharsets.UTF_8);
        String encodedGitProfileImageUrl = URLEncoder.encode(authResponse.gitProfileImageUrl(), StandardCharsets.UTF_8);

        // 프론트엔드 페이지로 토큰과 함께 리다이렉트
        String frontendRedirectUrl = String.format(
                "%s/oauth2/github/code?memberId=%s&gitLoginId=%s&profileImgUrl=%s",
                REDIRECT_URL, encodedMemberId, encodedLoginId, encodedGitProfileImageUrl);

        response.sendRedirect(frontendRedirectUrl);
    }

}
