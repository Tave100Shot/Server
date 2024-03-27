package com.api.TaveShot.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class GoogleItemDtos {

    List<GoogleItemDto> dtos;
}
