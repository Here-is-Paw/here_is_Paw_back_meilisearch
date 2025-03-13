package com.ll.hereispaw.domain.search.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드가 있는 생성자 추가 (필수는 아님)
public class PostEventDto {
    private long id;

    @NonNull
    private String pathUrl;

    @NonNull
    private String breed;

    @NonNull
    private String location;

    private double x;

    private double y;

    private int type; // 0 = 실종 1 = 발견

    private String name;

    private int state;

    private String etc;

    private LocalDateTime createdDate;
}
