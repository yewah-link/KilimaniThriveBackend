package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.CommentDTO;
import com.lincoln.kilimaniThrive.models.entity.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentMapper {

    @Mapping(target = "blogId", source = "blog.id")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "authorName", expression = "java(comment.getAuthor() != null ? comment.getAuthor().getFirstName() + \" \" + (comment.getAuthor().getLastName() != null ? comment.getAuthor().getLastName() : \"\") : \"Anonymous\")")
    @Mapping(target = "authorEmail", source = "author.email")
    @Mapping(target = "authorInitial", ignore = true) // Handled in decorator
    CommentDTO commentToCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blog", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commentDtoToComment(CommentDTO commentDto);
}
