package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.models.dtos.BlogDTO;
import java.util.List;

public interface BlogService {
    GenericResponseV2<BlogDTO> add(BlogDTO blogDTO);
    GenericResponseV2<Void> delete(Long id);
    GenericResponseV2<BlogDTO> update(Long id, BlogDTO blogDTO);
    GenericResponseV2<BlogDTO> getBlog(Long id);
    GenericResponseV2<BlogDTO> like(Long id);
    GenericResponseV2<List<BlogDTO>> getAllBlogs();
}
