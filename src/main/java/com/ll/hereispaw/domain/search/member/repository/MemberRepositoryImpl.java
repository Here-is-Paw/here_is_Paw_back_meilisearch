package com.ll.hereispaw.domain.search.member.repository;

import com.ll.hereispaw.domain.search.member.document.MemberDocument;
import com.ll.hereispaw.global.config.MeilisearchConfig;
import com.ll.hereispaw.global.error.ErrorCode;
import com.ll.hereispaw.global.exception.CustomException;
import com.ll.hereispaw.standard.Ut.Ut;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberDocumentRepository {
    private final MeilisearchConfig meilisearchConfig;

    private Index getIndex(String indexName) {
        Index index = meilisearchConfig.meilisearchClient().index(indexName);

        if (index == null) {
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }

        return index;
    }

    @Override
    public void save(MemberDocument memDoc, String indexName) {
        String jsonDocument = Ut.json.toString(memDoc);
        getIndex(indexName).addDocuments(jsonDocument, "id");
        log.info("멤버 문서 작성 완료");
    }

    @Override
    public void delete(MemberDocument memDoc, String indexName) {
        getIndex(indexName).deleteDocument(String.valueOf(memDoc.getId()));
        log.info("멤버 문서 삭제 완료");
    }

    @Override
    public void update(MemberDocument memDoc, String indexName) {
        save(memDoc, indexName);
        log.info("멤버 문서 수정 완료");
    }

    @Override
    public void clear(String indexName) {
        getIndex(indexName).deleteAllDocuments();
    }

    @Override
    public Searchable search(String indexName, SearchRequest searchRequest) {
        return getIndex(indexName).search(searchRequest);
    }

}
