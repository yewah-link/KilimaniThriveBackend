package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.TagDTO;
import com.lincoln.kilimaniThrive.models.entity.Tag;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(TagMapperDecorator.class)
public interface TagMapper {

    TagDTO tagToTagDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blogs", ignore = true)
    Tag tagDtoToTag(TagDTO tagDto);
}
