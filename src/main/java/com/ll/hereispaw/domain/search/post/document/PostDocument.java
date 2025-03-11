package com.ll.hereispaw.domain.search.post.document;

import com.ll.hereispaw.domain.search.post.dto.request.PostEventDto;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PostDocument{
    private long id;

    @NonNull
    private String pathUrl;

    @NonNull
    private String breed;

    @NonNull
    private String location;

    private double x;
    private double y;

    private String etc;

    public PostDocument(PostEventDto postEventDto) {
        this.id = postEventDto.getId();
        this.pathUrl = postEventDto.getPathUrl();
        this.breed = postEventDto.getBreed();
        this.x = postEventDto.getX();
        this.y = postEventDto.getY();
        this.location = postEventDto.getLocation();
        this.etc = postEventDto.getEtc();
    }
}
