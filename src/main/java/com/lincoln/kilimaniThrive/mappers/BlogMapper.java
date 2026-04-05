package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.BlogDTO;
import com.lincoln.kilimaniThrive.models.entity.Blogs;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
@DecoratedWith(BlogMapperDecorator.class)
public interface BlogMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", expression = "java(blogs.getAuthor() != null ? blogs.getAuthor().getFirstName() + \" \" + blogs.getAuthor().getLastName() : null)")
    BlogDTO blogsToBlogDto(Blogs blogs);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Blogs blogDtoToBlogs(BlogDTO blogDto);
}
