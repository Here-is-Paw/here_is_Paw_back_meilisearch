package com.ll.hereispaw.global.enums;

import lombok.Getter;

@Getter
public enum IndexName {
    MISSING("missing"), FINDING("finding"), MEMBER("member");

    private final String indexName; // String 필드 추가

    IndexName(String indexName) { // Getter 메서드
        this.indexName = indexName;
    }
}
