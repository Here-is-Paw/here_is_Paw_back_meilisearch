package com.ll.hereispaw.domain.search.post.eventListener;

import com.ll.hereispaw.global.enums.IndexName;
import com.ll.hereispaw.domain.search.post.document.PostDocument;
import com.ll.hereispaw.domain.search.post.dto.request.PostEventDto;
import com.ll.hereispaw.domain.search.post.service.PostDocumentService;
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
            containerFactory = "kafkaListenerContainerPostFactory"
    )
    public void createDocuments(PostEventDto postEventDto){
        // 1 실종, 0 발견
        log.info("type: {}", postEventDto.getType());
        if (postEventDto.getType() == 0) {
            log.info("발견");
            typeConvert(postEventDto, IndexName.FINDING.getIndexName());
        }else {
            log.info("미싱");
            typeConvert(postEventDto, IndexName.MISSING.getIndexName());
        }
    }

    private void typeConvert(PostEventDto postEventDto, String indexName) {
        log.info("indexName: {}", indexName);

        switch (postEventDto.getState()) {
            case 0 :
                postDocumentService.add(new PostDocument(postEventDto), indexName);
                break;
            case 1 :
                postDocumentService.update(new PostDocument(postEventDto), indexName);
                break;
            case 2 :
                postDocumentService.delete(new PostDocument(postEventDto), indexName);
                break;
            default : throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }
}
