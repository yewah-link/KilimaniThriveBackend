package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.BibleStudyDTO;
import com.lincoln.kilimaniThrive.services.BibleStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bible-study")
public class BibleStudyController {

    private final BibleStudyService bibleStudyService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponseV2<BibleStudyDTO>> addStudy(@RequestBody BibleStudyDTO studyDTO) {
        GenericResponseV2<BibleStudyDTO> response = bibleStudyService.add(studyDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponseV2<Void>> deleteStudy(@PathVariable Long id) {
        GenericResponseV2<Void> response = bibleStudyService.delete(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponseV2<BibleStudyDTO>> updateStudy(
            @PathVariable Long id,
            @RequestBody BibleStudyDTO studyDTO) {
        GenericResponseV2<BibleStudyDTO> response = bibleStudyService.update(id, studyDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponseV2<List<BibleStudyDTO>>> getAllStudies() {
        GenericResponseV2<List<BibleStudyDTO>> response = bibleStudyService.getAllBibleStudies();
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseV2<BibleStudyDTO>> getStudy(@PathVariable Long id) {
        GenericResponseV2<BibleStudyDTO> response = bibleStudyService.getBibleStudy(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
