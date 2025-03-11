package com.ll.hereispaw.domain.search.member.repository;

import com.ll.hereispaw.domain.search.member.document.MemberDocument;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;


public interface MemberDocumentRepository {
    void save(MemberDocument memDoc, String indexName);

    void delete(MemberDocument memDoc, String indexName);

    void update(MemberDocument memDoc, String indexName);

    void clear(String indexName);

    Searchable search(String indexName, SearchRequest searchRequest);
}
