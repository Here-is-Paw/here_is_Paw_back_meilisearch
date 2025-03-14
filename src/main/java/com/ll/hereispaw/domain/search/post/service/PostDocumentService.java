package com.ll.hereispaw.domain.search.post.service;

import com.ll.hereispaw.domain.search.post.document.PostDocument;
import com.ll.hereispaw.domain.search.post.dto.response.SearchPostResponseDto;
import com.ll.hereispaw.domain.search.post.repository.PostDocumentRepository;
import com.ll.hereispaw.global.enums.IndexName;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.Searchable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final PostDocumentRepository postDocumentRepository;


    public void add(PostDocument postDoc, String indexName) {
        postDocumentRepository.save(postDoc, indexName);
    }

    public void delete(PostDocument postDoc, String indexName) {
        postDocumentRepository.delete(postDoc, indexName);
    }

    public void update(PostDocument postDoc, String indexName) {
        postDocumentRepository.update(postDoc, indexName);
    }

    public void clear(String indexName) {
        postDocumentRepository.clear(indexName);
    }

    public Page<SearchPostResponseDto> searchMissing(String genre, String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        SearchRequest searchRequest = new SearchRequest(keyword)
                .setOffset(offset)
                .setLimit(limit)
                .setAttributesToSearchOn(getAttributes(genre));

        return search(addSortToRequest(searchRequest, pageable), IndexName.MISSING.getIndexName());
    }

    public Page<SearchPostResponseDto> searchFinding(String genre, String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        log.debug("genre {}", genre);

        SearchRequest searchRequest = new SearchRequest(keyword)
                .setOffset(offset)
                .setLimit(limit)
                .setAttributesToSearchOn(getAttributes(genre));

        return search(addSortToRequest(searchRequest, pageable), IndexName.FINDING.getIndexName());
    }

    private String[] getAttributes(String genre) {
        if (genre == null) {
            return new String[]{"location", "breed"}; // null인 경우 기본값 반환
        }

        return switch (genre) {
            case "품종" -> new String[]{"breed"};
            case "지역" -> new String[]{"location"};
            default -> new String[]{"location", "breed"};
        };
    }

    private SearchRequest addSortToRequest(SearchRequest searchRequest, Pageable pageable) {
        // 기본 정렬(createdDate 내림차순) 또는 요청된 정렬 적용
        if (pageable.getSort().isUnsorted()) {

            // 정렬이 지정되지 않은 경우 기본적으로 createdDate 내림차순(최신순)
            searchRequest.setSort(new String[]{"createdDate:desc"});
        } else {
            List<String> sortCriteria = new ArrayList<>();

            for (Sort.Order order : pageable.getSort()) {
                String direction = order.getDirection().isAscending() ? "asc" : "desc";
                sortCriteria.add(order.getProperty() + ":" + direction);
            }

            return searchRequest.setSort(sortCriteria.toArray(new String[0]));
        }
        return searchRequest;
    }

    private Page<SearchPostResponseDto> search(SearchRequest searchRequest, String indexName) {
        Searchable searchable = postDocumentRepository.search(indexName, searchRequest);
        SearchResult searchResult = (SearchResult) searchable;

        List<SearchPostResponseDto> content = convertToDto(searchResult.getHits());
        log.debug("searchResult {}", searchResult.getEstimatedTotalHits());
        log.debug("getHits {}", searchResult.getHits());

        int pageNumber = searchResult.getOffset() / searchResult.getLimit();
        int pageSize = searchResult.getLimit();
        long totalElements = searchResult.getEstimatedTotalHits();

        Pageable setPageable = PageRequest.of(pageNumber, pageSize);

        return new PageImpl<>(content, setPageable, totalElements);
    }

    private List<SearchPostResponseDto> convertToDto(ArrayList<HashMap<String, Object>> hits) {
        return hits.stream()
                .map(this::mapToDto)
                .filter(Objects::nonNull) // null 결과 필터링
                .collect(Collectors.toList());
    }

    private SearchPostResponseDto mapToDto(Map<String, Object> hit) {
        try {
            SearchPostResponseDto dto = new SearchPostResponseDto();

            // 필수 필드 설정 (@NonNull 필드)
            dto.setId(getLongValue(hit, "id"));
            dto.setBreed(getStringValue(hit, "breed"));
            dto.setLocation(getStringValue(hit, "location"));
            dto.setPathUrl(getStringValue(hit, "pathUrl"));
            dto.setX(getDoubleValue(hit, "x"));
            dto.setX(getDoubleValue(hit, "y"));
            dto.setEtc(getStringValue(hit, "etc"));

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

    private double getDoubleValue(Map<String, Object> hit, String key) {
        Object value = hit.get(key);
        if (value == null) return 0;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
