package com.ll.hereispaw.domain.search.search.eventListener;

import com.ll.hereispaw.domain.search.search.document.IndexName;
import com.ll.hereispaw.domain.search.search.document.PostDocument;
import com.ll.hereispaw.domain.search.search.dto.request.PostEventDto;
import com.ll.hereispaw.domain.search.search.service.PostDocumentService;
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
public class PostEventListener {

    private final PostDocumentService postDocumentService;

    @KafkaListener(
            topics = "meiliesearch",
            groupId = "search",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void createDocuments(PostEventDto postEventDto){
        switch (postEventDto.getState()) {
            case 0 :
                postDocumentService.add(new PostDocument(postEventDto), IndexName.POST.getIndexName());
                break;
            case 1 :
                postDocumentService.update(new PostDocument(postEventDto), IndexName.POST.getIndexName());
                break;
            case 2 :
                postDocumentService.delete(new PostDocument(postEventDto), IndexName.POST.getIndexName());
                break;
            default : throw new CustomException(ErrorCode.DATABASE_ERROR);
        }

        log.debug("PostEventDto: {}", postEventDto);

    }
}
