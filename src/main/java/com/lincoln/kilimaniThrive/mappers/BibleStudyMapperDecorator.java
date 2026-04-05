package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.BibleStudyDTO;
import com.lincoln.kilimaniThrive.models.entity.BibleStudy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BibleStudyMapperDecorator implements BibleStudyMapper {

    @Autowired
    @Qualifier("delegate")
    private BibleStudyMapper delegate;

    @Override
    public BibleStudyDTO bibleStudyToBibleStudyDto(BibleStudy study) {
        return delegate.bibleStudyToBibleStudyDto(study);
    }

    @Override
    public BibleStudy bibleStudyDtoToBibleStudy(BibleStudyDTO studyDto) {
        return delegate.bibleStudyDtoToBibleStudy(studyDto);
    }
}
