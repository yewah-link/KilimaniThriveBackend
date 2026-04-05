package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.TagDTO;
import com.lincoln.kilimaniThrive.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponseV2<TagDTO>> addTag(@RequestBody TagDTO tagDTO) {
        GenericResponseV2<TagDTO> response = tagService.add(tagDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponseV2<Void>> deleteTag(@PathVariable Long id) {
        GenericResponseV2<Void> response = tagService.delete(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponseV2<TagDTO>> updateTag(
            @PathVariable Long id,
            @RequestBody TagDTO tagDTO) {
        GenericResponseV2<TagDTO> response = tagService.update(id, tagDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponseV2<List<TagDTO>>> getAllTags() {
        GenericResponseV2<List<TagDTO>> response = tagService.getAllTags();
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseV2<TagDTO>> getTag(@PathVariable Long id) {
        GenericResponseV2<TagDTO> response = tagService.getTag(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
