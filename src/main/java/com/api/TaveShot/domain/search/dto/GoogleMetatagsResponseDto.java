package com.api.TaveShot.domain.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleMetatagsResponseDto {

    @JsonProperty(value = "article:pc_service_home")
    private String blogType;
}
