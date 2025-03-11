package com.ll.hereispaw.domain.search.member.document;

import com.ll.hereispaw.domain.search.member.dto.request.MemberEventDto;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MemberDocument {
    private long id;

    @NonNull
    private String avatar;

    @NonNull
    private String nickname;

    public MemberDocument(MemberEventDto memberEventDto) {
        this.id = memberEventDto.getId();
        this.nickname = memberEventDto.getNickname();
        this.avatar = memberEventDto.getAvatar();
    }
}
