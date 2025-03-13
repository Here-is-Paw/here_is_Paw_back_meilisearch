package com.ll.hereispaw.domain.search.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드가 있는 생성자 추가 (필수는 아님)
public class MemberEventDto {
    @NonNull
    private Long id;

    private String avatar;

    @NonNull
    private String nickname;

    private int state;
}
