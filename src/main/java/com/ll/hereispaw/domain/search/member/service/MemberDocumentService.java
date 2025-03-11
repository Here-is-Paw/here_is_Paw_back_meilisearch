package com.ll.hereispaw.domain.search.member.service;


import com.ll.hereispaw.domain.search.member.document.MemberDocument;
import com.ll.hereispaw.domain.search.member.dto.response.SearchMemberResponse;
import com.ll.hereispaw.domain.search.member.repository.MemberDocumentRepository;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.Searchable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDocumentService {
    private final MemberDocumentRepository memberDocumentRepository;


    public void add(MemberDocument memDoc, String indexName) {
        memberDocumentRepository.save(memDoc, indexName);
    }

    public void delete(MemberDocument memDoc, String indexName) {
        memberDocumentRepository.delete(memDoc, indexName);
    }

    public void update(MemberDocument memDoc, String indexName) {
        memberDocumentRepository.update(memDoc, indexName);
    }

    public void clear(String indexName) {
        memberDocumentRepository.clear(indexName);
    }

    public Page<SearchMemberResponse> search(String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        SearchRequest searchRequest = new SearchRequest(keyword)
                .setOffset(offset)
                .setLimit(limit)
                .setAttributesToSearchOn(new String[]{"nickname"});;

//        // 정렬 적용 (필요한 경우)
//        addSortToRequest(searchRequest, pageable);
//
//        // MeiliSearch 검색 수행
//        SearchResult meiliResult = index.search(searchRequest);

        Searchable searchable = memberDocumentRepository.search("member", searchRequest);
        SearchResult searchResult = (SearchResult) searchable;

        List<SearchMemberResponse> content = convertToDto(searchResult.getHits());
        log.debug("searchResult {}", searchResult.getEstimatedTotalHits());
        log.debug("getHits {}", searchResult.getHits());

        int pageNumber = searchResult.getOffset() / searchResult.getLimit();
        int pageSize = searchResult.getLimit();
        long totalElements = searchResult.getEstimatedTotalHits();

        Pageable setPageable = PageRequest.of(pageNumber, pageSize);
        Page<SearchMemberResponse> page = new PageImpl<>(content, setPageable, totalElements);

        return page;
    }

    private List<SearchMemberResponse> convertToDto(ArrayList<HashMap<String, Object>> hits) {
        return hits.stream()
                .map(this::mapToDto)
                .filter(Objects::nonNull) // null 결과 필터링
                .collect(Collectors.toList());
    }

    private SearchMemberResponse mapToDto(Map<String, Object> hit) {
        try {
            SearchMemberResponse dto = new SearchMemberResponse();

            // 필수 필드 설정 (@NonNull 필드)
            dto.setId(getLongValue(hit, "id"));
            dto.setNickname(getStringValue(hit, "nickname"));
            dto.setAvatar(getStringValue(hit, "avatar"));

            return dto;
        } catch (Exception e) {
            log.error("검색 결과 변환 중 오류 발생: {}", e.getMessage());
            log.debug("검색 결과 내용: {}", hit);
            return null;
        }
    }

    private String getStringValue(Map<String, Object> hit, String key) {
        Object value = hit.get(key);
        return value != null ? value.toString() : "";
    }

    private long getLongValue(Map<String, Object> hit, String key) {
        Object value = hit.get(key);
        if (value == null) return 0L;
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private int getIntValue(Map<String, Object> hit, String key) {
        Object value = hit.get(key);
        if (value == null) return 0;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
