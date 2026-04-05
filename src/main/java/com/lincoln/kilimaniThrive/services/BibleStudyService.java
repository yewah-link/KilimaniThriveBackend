package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.models.dtos.BibleStudyDTO;

import java.util.List;

public interface BibleStudyService {
    GenericResponseV2<BibleStudyDTO> add(BibleStudyDTO studyDTO);
    GenericResponseV2<Void> delete(Long id);
    GenericResponseV2<BibleStudyDTO> update(Long id, BibleStudyDTO studyDTO);
    GenericResponseV2<BibleStudyDTO> getBibleStudy(Long id);
    GenericResponseV2<List<BibleStudyDTO>> getAllBibleStudies();
}
