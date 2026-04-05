package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.BlogDTO;
import com.lincoln.kilimaniThrive.services.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blog")
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponseV2<BlogDTO>> addBlog(@RequestBody BlogDTO blogDTO) {
        GenericResponseV2<BlogDTO> response = blogService.add(blogDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponseV2<Void>> deleteBlog(@PathVariable Long id) {
        GenericResponseV2<Void> response = blogService.delete(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponseV2<BlogDTO>> updateBlog(
            @PathVariable Long id,
            @RequestBody BlogDTO blogDTO) {
        GenericResponseV2<BlogDTO> response = blogService.update(id, blogDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponseV2<List<BlogDTO>>> getAllBlogs() {
        GenericResponseV2<List<BlogDTO>> response = blogService.getAllBlogs();
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseV2<BlogDTO>> getBlog(@PathVariable Long id) {
        GenericResponseV2<BlogDTO> response = blogService.getBlog(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<GenericResponseV2<BlogDTO>> likeBlog(@PathVariable Long id) {
        GenericResponseV2<BlogDTO> response = blogService.like(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
