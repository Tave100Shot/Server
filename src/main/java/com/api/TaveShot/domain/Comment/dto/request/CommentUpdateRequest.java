package com.api.TaveShot.domain.Comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentUpdateRequest {
    private final String comment;
}