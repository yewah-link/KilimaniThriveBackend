package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.mappers.TagMapper;
import com.lincoln.kilimaniThrive.models.dtos.TagDTO;
import com.lincoln.kilimaniThrive.models.entity.Tag;
import com.lincoln.kilimaniThrive.repositories.TagRepository;
import com.lincoln.kilimaniThrive.services.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public GenericResponseV2<TagDTO> add(TagDTO tagDTO) {
        try {
            if (tagRepository.findByName(tagDTO.getName()).isPresent()) {
                return GenericResponseV2.<TagDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Tag already exists")
                        ._embedded(null)
                        .build();
            }

            Tag tagToBeSaved = tagMapper.tagDtoToTag(tagDTO);
            Tag savedTag = tagRepository.save(tagToBeSaved);
            TagDTO response = tagMapper.tagToTagDto(savedTag);

            return GenericResponseV2.<TagDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Tag added successfully")
                    ._embedded(response)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred adding tag", e);
            return GenericResponseV2.<TagDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to add tag")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<Void> delete(Long id) {
        try {
            if (!tagRepository.existsById(id)) {
                return GenericResponseV2.<Void>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Tag not found")
                        ._embedded(null)
                        .build();
            }

            tagRepository.deleteById(id);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Tag deleted successfully")
                    ._embedded(null)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred deleting tag", e);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to delete tag")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<TagDTO> update(Long id, TagDTO tagDTO) {
        try {
            Optional<Tag> tagOpt = tagRepository.findById(id);
            if (tagOpt.isEmpty()) {
                return GenericResponseV2.<TagDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Tag not found")
                        ._embedded(null)
                        .build();
            }

            Tag tagToUpdate = tagOpt.get();
            tagToUpdate.setName(tagDTO.getName());

            Tag updatedTag = tagRepository.save(tagToUpdate);
            TagDTO responseDto = tagMapper.tagToTagDto(updatedTag);

            return GenericResponseV2.<TagDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Tag updated successfully")
                    ._embedded(responseDto)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred updating tag", e);
            return GenericResponseV2.<TagDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to update tag")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<TagDTO> getTag(Long id) {
        try {
            Optional<Tag> tagOpt = tagRepository.findById(id);
            if (tagOpt.isEmpty()) {
                return GenericResponseV2.<TagDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Tag not found")
                        ._embedded(null)
                        .build();
            }

            TagDTO tagDTO = tagMapper.tagToTagDto(tagOpt.get());

            return GenericResponseV2.<TagDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Tag retrieved successfully")
                    ._embedded(tagDTO)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving tag", e);
            return GenericResponseV2.<TagDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve tag")
                    ._embedded(null)
                    .build();
        }
    }

    @Override
    public GenericResponseV2<List<TagDTO>> getAllTags() {
        try {
            List<Tag> tags = tagRepository.findAll();
            List<TagDTO> tagDTOs = tags.stream()
                    .map(tagMapper::tagToTagDto)
                    .toList();

            return GenericResponseV2.<List<TagDTO>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Tags retrieved successfully")
                    ._embedded(tagDTOs)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred retrieving all tags", e);
            return GenericResponseV2.<List<TagDTO>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to retrieve tags")
                    ._embedded(null)
                    .build();
        }
    }
}
