package com.ll.hereispaw.domain.search.post.repository;

import com.ll.hereispaw.domain.search.post.document.PostDocument;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;


public interface PostDocumentRepository {
    void save(PostDocument postDoc, String indexName);

    void delete(PostDocument postDocument, String indexName);

    void update(PostDocument postDocument, String indexName);

    void clear(String indexName);

    Searchable search(String indexName, SearchRequest searchRequest);
}
