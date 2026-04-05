package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.enums.BlogStatus;
import com.lincoln.kilimaniThrive.mappers.BlogMapper;
import com.lincoln.kilimaniThrive.models.dtos.BlogDTO;
import com.lincoln.kilimaniThrive.models.entity.Blogs;
import com.lincoln.kilimaniThrive.models.entity.Tag;
import com.lincoln.kilimaniThrive.models.entity.User;
import com.lincoln.kilimaniThrive.repositories.BlogsRepository;
import com.lincoln.kilimaniThrive.repositories.TagRepository;
import com.lincoln.kilimaniThrive.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogsRepository blogsRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final BlogMapper blogMapper;

    @Override
    public GenericResponseV2<BlogDTO> add(BlogDTO blogDTO) {
        try {
            // Check for current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return GenericResponseV2.<BlogDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Unauthorized: You must be logged in to post.")
                        .build();
            }

            String email = authentication.getName();
            User author = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            Blogs blogToBeSaved = blogMapper.blogDtoToBlogs(blogDTO);
            blogToBeSaved.setAuthor(author);

            // Resolve tags from DB (find existing or create new)
            if (blogDTO.getTags() != null && !blogDTO.getTags().isEmpty()) {
                List<Tag> resolvedTags = new ArrayList<>();
                for (var tagDto : blogDTO.getTags()) {
                    Tag tag = tagRepository.findByName(tagDto.getName())
                            .orElseGet(() -> tagRepository.save(
                                    Tag.builder().name(tagDto.getName()).build()
                            ));
                    resolvedTags.add(tag);
                }
                blogToBeSaved.setTags(resolvedTags);
            } else {
                blogToBeSaved.setTags(new ArrayList<>());
            }

            // Handle published date
            if (blogToBeSaved.getStatus() == BlogStatus.PUBLISHED) {
                blogToBeSaved.setPublishedAt(LocalDateTime.now());
            }

            Blogs savedBlog = blogsRepository.save(blogToBeSaved);
            BlogDTO response = blogMapper.blogsToBlogDto(savedBlog);

            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Blog added successfully")
                    ._embedded(response)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred adding blog", e);
            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to add blog: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public GenericResponseV2<Void> delete(Long id) {
        try {
            if (!blogsRepository.existsById(id)) {
                return GenericResponseV2.<Void>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Blog not found")
                        .build();
            }

            blogsRepository.deleteById(id);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Blog deleted successfully")
                    .build();

        } catch (Exception e) {
            log.error("An error occurred deleting blog", e);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to delete blog")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<BlogDTO> update(Long id, BlogDTO blogDTO) {
        try {
            // Check for current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return GenericResponseV2.<BlogDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Unauthorized: You must be logged in to update posts.")
                        .build();
            }

            Optional<Blogs> blogOpt = blogsRepository.findById(id);
            if (blogOpt.isEmpty()) {
                return GenericResponseV2.<BlogDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Blog not found")
                        .build();
            }

            Blogs blogToUpdate = blogOpt.get();
            String email = authentication.getName();
            if (!blogToUpdate.getAuthor().getEmail().equals(email)) {
                return GenericResponseV2.<BlogDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Unauthorized: You can only edit your own posts.")
                        .build();
            }

            blogToUpdate.setTitle(blogDTO.getTitle());
            blogToUpdate.setContent(blogDTO.getContent());
            blogToUpdate.setFeaturedImage(blogDTO.getFeaturedImage());
            blogToUpdate.setStatus(blogDTO.getStatus());
            blogToUpdate.setFeatured(Boolean.TRUE.equals(blogDTO.getIsFeatured()));
            blogToUpdate.setAllowComments(Boolean.TRUE.equals(blogDTO.getAllowComments()));

            // Resolve tags from DB (find existing or create new)
            if (blogDTO.getTags() != null) {
                List<Tag> resolvedTags = new ArrayList<>();
                for (var tagDto : blogDTO.getTags()) {
                    Tag tag = tagRepository.findByName(tagDto.getName())
                            .orElseGet(() -> tagRepository.save(
                                    Tag.builder().name(tagDto.getName()).build()
                            ));
                    resolvedTags.add(tag);
                }
                blogToUpdate.setTags(resolvedTags);
            }

            Blogs updatedBlog = blogsRepository.save(blogToUpdate);
            BlogDTO responseDto = blogMapper.blogsToBlogDto(updatedBlog);

            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Blog updated successfully")
                    ._embedded(responseDto)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred updating blog", e);
            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to update blog: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public GenericResponseV2<BlogDTO> getBlog(Long id) {
        try {
            Optional<Blogs> blogOpt = blogsRepository.findById(id);
            if (blogOpt.isEmpty()) {
                return GenericResponseV2.<BlogDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Blog not found")
                        .build();
            }

            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Blog retrieved successfully")
                    ._embedded(blogMapper.blogsToBlogDto(blogOpt.get()))
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving blog", e);
            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve blog")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<BlogDTO> like(Long id) {
        try {
            Optional<Blogs> blogOpt = blogsRepository.findById(id);
            if (blogOpt.isEmpty()) {
                return GenericResponseV2.<BlogDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Blog not found")
                        .build();
            }

            Blogs blog = blogOpt.get();
            Long currentLikes = blog.getLikes() != null ? blog.getLikes() : 0L;
            blog.setLikes(currentLikes + 1);
            Blogs updatedBlog = blogsRepository.save(blog);
            
            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Blog liked successfully")
                    ._embedded(blogMapper.blogsToBlogDto(updatedBlog))
                    .build();
        } catch (Exception e) {
            log.error("Error liking blog", e);
            return GenericResponseV2.<BlogDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to like blog")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<List<BlogDTO>> getAllBlogs() {
        try {
            List<Blogs> blogs = blogsRepository.findAll();
            List<BlogDTO> blogDTOs = blogs.stream()
                    .map(blogMapper::blogsToBlogDto)
                    .toList();

            return GenericResponseV2.<List<BlogDTO>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Blogs retrieved successfully")
                    ._embedded(blogDTOs)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving all blogs", e);
            return GenericResponseV2.<List<BlogDTO>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve blogs")
                    .build();
        }
    }
}
