package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.mappers.CommentMapper;
import com.lincoln.kilimaniThrive.models.dtos.CommentDTO;
import com.lincoln.kilimaniThrive.models.entity.Blogs;
import com.lincoln.kilimaniThrive.models.entity.Comment;
import com.lincoln.kilimaniThrive.models.entity.User;
import com.lincoln.kilimaniThrive.repositories.BlogsRepository;
import com.lincoln.kilimaniThrive.repositories.CommentRepository;
import com.lincoln.kilimaniThrive.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogsRepository blogsRepository;
    private final CommentMapper commentMapper;

    @Override
    public GenericResponseV2<CommentDTO> add(CommentDTO commentDTO) {
        try {
            // Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return GenericResponseV2.<CommentDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Unauthorized: You must be logged in to comment.")
                        .build();
            }

            Optional<Blogs> blogOpt = blogsRepository.findById(commentDTO.getBlogId());
            if (blogOpt.isEmpty()) {
                return GenericResponseV2.<CommentDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Blog not found")
                        .build();
            }

            User author = (User) authentication.getPrincipal();

            Comment commentToBeSaved = commentMapper.commentDtoToComment(commentDTO);
            commentToBeSaved.setBlog(blogOpt.get());
            commentToBeSaved.setAuthor(author);

            // Handle parent if it's a reply
            if (commentDTO.getParentId() != null) {
                Optional<Comment> parentOpt = commentRepository.findById(commentDTO.getParentId());
                parentOpt.ifPresent(commentToBeSaved::setParent);
            }
            
            Comment savedComment = commentRepository.save(commentToBeSaved);
            CommentDTO response = commentMapper.commentToCommentDto(savedComment);

            return GenericResponseV2.<CommentDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Comment added successfully")
                    ._embedded(response)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred adding comment", e);
            return GenericResponseV2.<CommentDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to add comment: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public GenericResponseV2<Void> delete(Long id) {
        try {
            if (!commentRepository.existsById(id)) {
                return GenericResponseV2.<Void>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Comment not found")
                        ._embedded(null)
                        .build();
            }

            commentRepository.deleteById(id);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Comment deleted successfully")
                    ._embedded(null)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred deleting comment", e);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to delete comment")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<CommentDTO> getComment(Long id) {
        try {
            Optional<Comment> commentOpt = commentRepository.findById(id);
            if (commentOpt.isEmpty()) {
                return GenericResponseV2.<CommentDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Comment not found")
                        ._embedded(null)
                        .build();
            }

            CommentDTO commentDTO = commentMapper.commentToCommentDto(commentOpt.get());

            return GenericResponseV2.<CommentDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Comment retrieved successfully")
                    ._embedded(commentDTO)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving comment", e);
            return GenericResponseV2.<CommentDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve comment")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<List<CommentDTO>> getAllCommentsForBlog(Long blogId) {
        try {
            Optional<Blogs> blogOpt = blogsRepository.findById(blogId);
            if (blogOpt.isEmpty()) {
                return GenericResponseV2.<List<CommentDTO>>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Blog not found")
                        ._embedded(null)
                        .build();
            }

            List<Comment> comments = commentRepository.findByBlog(blogOpt.get(), Pageable.unpaged()).getContent();
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(commentMapper::commentToCommentDto)
                    .toList();

            return GenericResponseV2.<List<CommentDTO>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Comments retrieved successfully")
                    ._embedded(commentDTOs)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving comments for blog", e);
            return GenericResponseV2.<List<CommentDTO>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve comments")
                    ._embedded(null)
                    .build();
        }
    }
}
