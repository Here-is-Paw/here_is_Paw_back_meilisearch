package com.ll.hereispaw.global.initData;

import com.ll.hereispaw.global.enums.IndexName;
import com.ll.hereispaw.domain.search.post.service.PostDocumentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class initData {

    private final PostDocumentService postDocumentService;

    @PostConstruct
    public void init() {
        log.info("테스트 데이터 초기화 시작");
//        initTestData();
        log.info("테스트 데이터 초기화 완료");
    }

    public void initTestData() {
        // 기존 데이터 초기화
        postDocumentService.clear(IndexName.MISSING.getIndexName());
        postDocumentService.clear(IndexName.FINDING.getIndexName());
        postDocumentService.clear(IndexName.MEMBER.getIndexName());
    }
}
