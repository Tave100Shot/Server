package com.api.TaveShot.domain.authorization.dto;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@Getter
@ToString
public class SolvedUserInfo {

    private String bio;

    @JsonProperty("tier")
    private Integer bojTier;
}
