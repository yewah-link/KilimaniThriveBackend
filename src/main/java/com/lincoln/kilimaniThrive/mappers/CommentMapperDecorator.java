package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.CommentDTO;
import com.lincoln.kilimaniThrive.models.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CommentMapperDecorator implements CommentMapper {

    @Autowired
    @Qualifier("delegate")
    private CommentMapper delegate;

    @Override
    public CommentDTO commentToCommentDto(Comment comment) {
        CommentDTO dto = delegate.commentToCommentDto(comment);
        if (comment.getAuthor() != null) {
            String firstName = comment.getAuthor().getFirstName();
            String lastName = comment.getAuthor().getLastName();
            StringBuilder initials = new StringBuilder();
            
            if (firstName != null && !firstName.isEmpty()) {
                initials.append(firstName.charAt(0));
            }
            if (lastName != null && !lastName.isEmpty()) {
                initials.append(lastName.charAt(0));
            }
            
            dto.setAuthorInitial(initials.length() > 0 ? initials.toString().toUpperCase() : "?");
        } else {
            dto.setAuthorInitial("?");
        }
        return dto;
    }

    @Override
    public Comment commentDtoToComment(CommentDTO commentDto) {
        return delegate.commentDtoToComment(commentDto);
    }
}
