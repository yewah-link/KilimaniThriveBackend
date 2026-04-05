package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.BibleStudyDTO;
import com.lincoln.kilimaniThrive.models.entity.BibleStudy;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
@DecoratedWith(BibleStudyMapperDecorator.class)
public interface BibleStudyMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", expression = "java(study.getAuthor() != null ? study.getAuthor().getFirstName() + \" \" + study.getAuthor().getLastName() : null)")
    BibleStudyDTO bibleStudyToBibleStudyDto(BibleStudy study);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BibleStudy bibleStudyDtoToBibleStudy(BibleStudyDTO studyDto);
}
