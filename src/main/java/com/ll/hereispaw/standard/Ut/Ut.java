package com.ll.hereispaw.standard.Ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.SneakyThrows;

import java.time.format.DateTimeFormatter;

public class Ut {
    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om;

        // 정적 초기화 블록에서 ObjectMapper 설정
        static {
            om = new ObjectMapper();

            // JavaTimeModule 생성 및 등록
            JavaTimeModule javaTimeModule = new JavaTimeModule();

            // ISO 형식으로 LocalDateTime 직렬화 설정
            javaTimeModule.addSerializer(java.time.LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            om.registerModule(javaTimeModule);

            // 날짜를 타임스탬프가 아닌 문자열로 직렬화
            om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }

        @SneakyThrows
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }
}