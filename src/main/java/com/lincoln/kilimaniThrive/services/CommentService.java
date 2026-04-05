package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.models.dtos.CommentDTO;

import java.util.List;

public interface CommentService {
    GenericResponseV2<CommentDTO> add(CommentDTO commentDTO);
    GenericResponseV2<Void> delete(Long id);
    GenericResponseV2<CommentDTO> getComment(Long id);
    GenericResponseV2<List<CommentDTO>> getAllCommentsForBlog(Long blogId);
}
