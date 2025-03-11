package com.ll.hereispaw.domain.search.post.repository;

import com.ll.hereispaw.domain.search.post.document.PostDocument;
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
public class PostDocumentRepositoryImpl implements PostDocumentRepository {
    private final MeilisearchConfig meilisearchConfig;

    private Index getIndex(String indexName) {
        Index index = meilisearchConfig.meilisearchClient().index(indexName);

        if (index == null) {
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }

        return index;
    }

    @Override
    public void save(PostDocument postDoc, String indexName) {
        log.debug("indexNameRepo: {}", indexName);
        String jsonDocument = Ut.json.toString(postDoc);
        log.debug("getIndex() : {}", getIndex(indexName));
        getIndex(indexName).addDocuments(jsonDocument, "id");
        log.info("문서 작성 완료");
    }

    @Override
    public void delete(PostDocument postDocument, String indexName) {
        getIndex(indexName).deleteDocument(String.valueOf(postDocument.getId()));
        log.info("문서 삭제 완료");
    }

    @Override
    public void update(PostDocument postDocument, String indexName) {
        save(postDocument, indexName);
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
