package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.TagDTO;
import com.lincoln.kilimaniThrive.models.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class TagMapperDecorator implements TagMapper {

    @Autowired
    @Qualifier("delegate")
    private TagMapper delegate;

    @Override
    public TagDTO tagToTagDto(Tag tag) {
        return delegate.tagToTagDto(tag);
    }

    @Override
    public Tag tagDtoToTag(TagDTO tagDto) {
        return delegate.tagDtoToTag(tagDto);
    }
}
