package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.BlogDTO;
import com.lincoln.kilimaniThrive.models.entity.Blogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BlogMapperDecorator implements BlogMapper {

    @Autowired
    @Qualifier("delegate")
    private BlogMapper delegate;

    @Override
    public BlogDTO blogsToBlogDto(Blogs blogs) {
        return delegate.blogsToBlogDto(blogs);
    }

    @Override
    public Blogs blogDtoToBlogs(BlogDTO blogDto) {
        return delegate.blogDtoToBlogs(blogDto);
    }
}
