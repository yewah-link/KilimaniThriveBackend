package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.mappers.BibleStudyMapper;
import com.lincoln.kilimaniThrive.models.dtos.BibleStudyDTO;
import com.lincoln.kilimaniThrive.models.entity.BibleStudy;
import com.lincoln.kilimaniThrive.repositories.BibleStudyRepository;
import com.lincoln.kilimaniThrive.services.BibleStudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BibleStudyServiceImpl implements BibleStudyService {

    private final BibleStudyRepository bibleStudyRepository;
    private final BibleStudyMapper bibleStudyMapper;

    @Override
    public GenericResponseV2<BibleStudyDTO> add(BibleStudyDTO studyDTO) {
        try {
            BibleStudy studyToBeSaved = bibleStudyMapper.bibleStudyDtoToBibleStudy(studyDTO);
            BibleStudy savedStudy = bibleStudyRepository.save(studyToBeSaved);
            BibleStudyDTO response = bibleStudyMapper.bibleStudyToBibleStudyDto(savedStudy);

            return GenericResponseV2.<BibleStudyDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Bible study added successfully")
                    ._embedded(response)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred adding Bible study", e);
            return GenericResponseV2.<BibleStudyDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to add Bible study")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<Void> delete(Long id) {
        try {
            if (!bibleStudyRepository.existsById(id)) {
                return GenericResponseV2.<Void>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Bible study not found")
                        ._embedded(null)
                        .build();
            }

            bibleStudyRepository.deleteById(id);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Bible study deleted successfully")
                    ._embedded(null)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred deleting Bible study", e);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to delete Bible study")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<BibleStudyDTO> update(Long id, BibleStudyDTO studyDTO) {
        try {
            Optional<BibleStudy> studyOpt = bibleStudyRepository.findById(id);
            if (studyOpt.isEmpty()) {
                return GenericResponseV2.<BibleStudyDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Bible study not found")
                        ._embedded(null)
                        .build();
            }

            BibleStudy studyToUpdate = studyOpt.get();
            studyToUpdate.setTitle(studyDTO.getTitle());
            studyToUpdate.setDescription(studyDTO.getDescription());
            studyToUpdate.setContent(studyDTO.getContent());
            studyToUpdate.setContentUrl(studyDTO.getContentUrl());
            studyToUpdate.setImageUrl(studyDTO.getImageUrl());
            studyToUpdate.setCategory(studyDTO.getCategory());
            studyToUpdate.setStatus(studyDTO.getStatus());

            BibleStudy updatedStudy = bibleStudyRepository.save(studyToUpdate);
            BibleStudyDTO responseDto = bibleStudyMapper.bibleStudyToBibleStudyDto(updatedStudy);

            return GenericResponseV2.<BibleStudyDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Bible study updated successfully")
                    ._embedded(responseDto)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred updating Bible study", e);
            return GenericResponseV2.<BibleStudyDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to update Bible study")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<BibleStudyDTO> getBibleStudy(Long id) {
        try {
            Optional<BibleStudy> studyOpt = bibleStudyRepository.findById(id);
            if (studyOpt.isEmpty()) {
                return GenericResponseV2.<BibleStudyDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Bible study not found")
                        ._embedded(null)
                        .build();
            }

            BibleStudyDTO studyDTO = bibleStudyMapper.bibleStudyToBibleStudyDto(studyOpt.get());

            return GenericResponseV2.<BibleStudyDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Bible study retrieved successfully")
                    ._embedded(studyDTO)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving Bible study", e);
            return GenericResponseV2.<BibleStudyDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve Bible study")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<List<BibleStudyDTO>> getAllBibleStudies() {
        try {
            List<BibleStudy> studies = bibleStudyRepository.findAll();
            List<BibleStudyDTO> studyDTOs = studies.stream()
                    .map(bibleStudyMapper::bibleStudyToBibleStudyDto)
                    .toList();

            return GenericResponseV2.<List<BibleStudyDTO>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Bible studies retrieved successfully")
                    ._embedded(studyDTOs)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving all Bible studies", e);
            return GenericResponseV2.<List<BibleStudyDTO>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve Bible studies")
                    ._embedded(null)
                    .build();
        }
    }
}
