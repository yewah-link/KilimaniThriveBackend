package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.CommentDTO;
import com.lincoln.kilimaniThrive.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponseV2<CommentDTO>> addComment(@RequestBody CommentDTO commentDTO) {
        GenericResponseV2<CommentDTO> response = commentService.add(commentDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponseV2<Void>> deleteComment(@PathVariable Long id) {
        GenericResponseV2<Void> response = commentService.delete(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<GenericResponseV2<List<CommentDTO>>> getCommentsForBlog(@PathVariable Long blogId) {
        GenericResponseV2<List<CommentDTO>> response = commentService.getAllCommentsForBlog(blogId);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseV2<CommentDTO>> getComment(@PathVariable Long id) {
        GenericResponseV2<CommentDTO> response = commentService.getComment(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
