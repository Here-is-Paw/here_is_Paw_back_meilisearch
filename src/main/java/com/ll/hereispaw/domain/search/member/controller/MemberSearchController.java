package com.ll.hereispaw.domain.search.member.controller;

import com.ll.hereispaw.domain.search.member.dto.response.SearchMemberResponse;
import com.ll.hereispaw.domain.search.member.service.MemberDocumentService;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/searchMember")
@RequiredArgsConstructor
public class MemberSearchController {
    private final MemberDocumentService memberDocumentService;

    @GetMapping("/{userId}")
    public GlobalResponse<Page<SearchMemberResponse>> search(
            @RequestParam("kw") String keyword,
            @PathVariable("userId") Long userId,
            @PageableDefault(size = 10) Pageable pageable) {
        log.debug("userId: {}", userId);

        return GlobalResponse.success(memberDocumentService.search(keyword, userId, pageable));
    }
}
