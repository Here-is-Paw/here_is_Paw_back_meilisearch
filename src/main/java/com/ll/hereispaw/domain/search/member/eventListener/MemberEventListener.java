package com.ll.hereispaw.domain.search.member.eventListener;

import com.ll.hereispaw.domain.search.member.document.MemberDocument;
import com.ll.hereispaw.domain.search.member.dto.request.MemberEventDto;
import com.ll.hereispaw.domain.search.member.service.MemberDocumentService;
import com.ll.hereispaw.global.enums.IndexName;
import com.ll.hereispaw.global.error.ErrorCode;
import com.ll.hereispaw.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class MemberEventListener {

    private final MemberDocumentService memberDocumentService;

    @KafkaListener(
            topics = "members",
            groupId = "member",
            containerFactory = "kafkaListenerContainerMemberFactory"
    )
    public void createDocuments(MemberEventDto memberEventDto){
        switch (memberEventDto.getState()) {
            case 0 :
                memberDocumentService.add(new MemberDocument(memberEventDto), IndexName.MEMBER.getIndexName());
                break;
            case 1 :
                memberDocumentService.update(new MemberDocument(memberEventDto), IndexName.MEMBER.getIndexName());
                break;
            case 2 :
                memberDocumentService.delete(new MemberDocument(memberEventDto), IndexName.MEMBER.getIndexName());
                break;
            default : throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }

}
