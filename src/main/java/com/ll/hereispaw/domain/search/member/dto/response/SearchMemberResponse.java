package com.ll.hereispaw.domain.search.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMemberResponse {
    @NonNull
    private Long id;

    private String avatar;

    @NonNull
    private String nickname;
}
