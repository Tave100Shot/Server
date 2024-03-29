package com.api.TaveShot.domain.post.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentCreateRequest {

    @Schema(description = "댓글 내용", example = "댓글 내용 예시")
    private String comment;

    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "1", nullable = true)
    private Long parentCommentId;

}
