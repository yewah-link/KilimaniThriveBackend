package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.models.dtos.TagDTO;

import java.util.List;

public interface TagService {
    GenericResponseV2<TagDTO> add(TagDTO tagDTO);
    GenericResponseV2<Void> delete(Long id);
    GenericResponseV2<TagDTO> update(Long id, TagDTO tagDTO);
    GenericResponseV2<TagDTO> getTag(Long id);
    GenericResponseV2<List<TagDTO>> getAllTags();
}
