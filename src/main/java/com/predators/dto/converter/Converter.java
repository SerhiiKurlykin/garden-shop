package com.predators.dto.converter;

public interface Converter<RequestDto,ResponseDto,Entity> {

    ResponseDto toDto(Entity entity);

    Entity toEntity(RequestDto requestDto);
}
