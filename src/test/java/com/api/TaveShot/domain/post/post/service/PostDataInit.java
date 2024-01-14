package com.api.TaveShot.domain.post.post.service;

import com.api.TaveShot.domain.Member.domain.Member;
import com.api.TaveShot.domain.Member.repository.MemberRepository;
import com.api.TaveShot.domain.post.post.domain.PostTier;
import com.api.TaveShot.domain.post.post.dto.request.PostCreateRequest;
import com.api.TaveShot.global.exception.ApiException;
import com.api.TaveShot.global.exception.ErrorType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class PostDataInit {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Test
    @DisplayName("팀원들 계정으로 예시 게시글 생성")
    @Transactional
    @Rollback(false)
    void initExPost() throws IOException {

        Member toychip = getMember("toychip");
//        Member young0519 = getMember("young0519");
//        Member cucubob = getMember("cucubob");
//        Member thwjddlqslek = getMember("thwjddlqslek");
//        Member wjd4204 = getMember("wjd4204");

        List<MultipartFile> multipartFiles = initMultipartFiles();

        registerData(toychip, multipartFiles);
//        registerData(young0519, multipartFiles);
//        registerData(cucubob, multipartFiles);
//        registerData(thwjddlqslek, multipartFiles);
//        registerData(wjd4204, multipartFiles);
    }

    private Member getMember(String gitLoginId) {
        return memberRepository.findByGitLoginId(gitLoginId)
                .orElseThrow(() -> new ApiException(ErrorType._USER_NOT_FOUND_DB));
    }

    private void registerData(Member member, List<MultipartFile> multipartFiles) {
        for (int i = 0; i < 300; i++) {
            PostCreateRequest request = PostCreateRequest.builder()
                    .title("title Test Ex Data " + i + " " + member.getGitLoginId())
                    .content("Content Test Ex Data " + i + " " + member.getGitLoginId())
                    .postTier(PostTier.POST_BRONZE_SILVER.getTier())
                    .attachmentFile(multipartFiles)
                    .build();

            postService.registerTest(request, member);
        }
    }

    private List<MultipartFile> initMultipartFiles() throws IOException {
        Path blueImagePath = Paths.get("exdata-png/blue_square.png");
        byte[] blueImageBytes = Files.readAllBytes(blueImagePath);
        MultipartFile blueFile = new MockMultipartFile("file", "blue_square.png", "image/png", blueImageBytes);

        Path greenImagePath = Paths.get("exdata-png/green_square.png");
        byte[] greenImageBytes = Files.readAllBytes(greenImagePath);
        MultipartFile greenFile = new MockMultipartFile("file", "green_square.png", "image/png", greenImageBytes);

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(blueFile);
        multipartFiles.add(greenFile);
        return multipartFiles;
    }
}