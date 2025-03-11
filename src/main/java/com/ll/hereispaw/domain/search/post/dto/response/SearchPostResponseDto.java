package com.ll.hereispaw.domain.search.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostResponseDto {
    private long id;

    @NonNull
    private String breed;

    @NonNull
    private String pathUrl;

    @NonNull
    private String location;

    private double x;
    private double y;

    private String etc;
}
