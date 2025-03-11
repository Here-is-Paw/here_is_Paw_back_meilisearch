package com.ll.hereispaw.domain.search.member.controller;

import com.ll.hereispaw.domain.search.member.dto.response.SearchMemberResponse;
import com.ll.hereispaw.domain.search.member.service.MemberDocumentService;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/searchMember")
@RequiredArgsConstructor
public class MemberSearchController {
    private final MemberDocumentService memberDocumentService;

    @GetMapping
    public GlobalResponse<Page<SearchMemberResponse>> search(
            @RequestParam("kw") String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        return GlobalResponse.success(memberDocumentService.search(keyword, pageable));
    }
}
