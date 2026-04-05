package com.lincoln.kilimaniThrive.repositories;

import com.lincoln.kilimaniThrive.enums.BlogStatus;
import com.lincoln.kilimaniThrive.models.entity.Blogs;
import com.lincoln.kilimaniThrive.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogsRepository extends JpaRepository<Blogs, Long> {
    Page<Blogs> findByStatus(BlogStatus status, Pageable pageable);
    List<Blogs> findByIsFeaturedTrue();
    Page<Blogs> findByAuthor(User author, Pageable pageable);
}
