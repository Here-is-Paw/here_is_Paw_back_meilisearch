package com.ll.hereispaw.domain.search.post.controller;

import com.ll.hereispaw.domain.search.post.service.PostDocumentService;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/searchPost")
@RequiredArgsConstructor
public class PostSearchController {
    private final PostDocumentService postDocumentService;

    @GetMapping("/missing")
    public GlobalResponse<?> searchMissing(
            @RequestParam("kw") String keyword,
            @RequestParam(required = false) String genre,
            @PageableDefault(size = 10) Pageable pageable) {
        return GlobalResponse.success(postDocumentService.searchMissing(genre, keyword, pageable));
    }

    @GetMapping("/finding")
    public GlobalResponse<?> searchFinding(
            @RequestParam("kw") String keyword,
            @RequestParam(required = false) String genre,
            @PageableDefault(size = 10) Pageable pageable) {
        return GlobalResponse.success(postDocumentService.searchFinding(genre, keyword, pageable));
    }
}
