package com.api.TaveShot.domain.search.service;

import com.api.TaveShot.domain.search.dto.GoogleItemDto;
import com.api.TaveShot.domain.search.dto.GoogleListResponseDto;
import com.api.TaveShot.domain.search.dto.GoogleResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class SearchService {

    @Value("${google.secret.key}")
    private String KEY;

    @Value("${google.secret.cx}")
    private String CX;


    public GoogleListResponseDto findBlog(String query) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com/customsearch/v1")
                .build();

        Flux<GoogleResponseDto> dto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", KEY)
                        .queryParam("cx", CX)
                        .queryParam("q", query)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(GoogleResponseDto.class);


        dto = dto.map(googleResponseDto -> {
            for (GoogleItemDto googleItemDto : googleResponseDto.getItems()){
                googleItemDto.modifyBlog(googleItemDto.getLink());
                googleItemDto.modifyCreatedDate();
            }
            return googleResponseDto;
        });

        List<GoogleResponseDto> googleResponseDtos = dto.collectList().block();

        return GoogleListResponseDto.builder()
                .dtos(googleResponseDtos)
                .build();

    }
}
