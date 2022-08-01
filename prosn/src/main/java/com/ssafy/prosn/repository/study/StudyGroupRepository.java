package com.ssafy.prosn.repository.study;

import com.ssafy.prosn.domain.study.StudyGroup;
import com.ssafy.prosn.dto.StudyGroupListResponseDto;
import com.ssafy.prosn.dto.UserStudyListResponseDto;
import com.ssafy.prosn.dto.UserStudyResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * created by yeomyeong on 2022/07/25
 * updated by yeomyeong on 2022/08/01
 */
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    @Query("select u.name from UserStudy us join User u on us.user = u.id where us.studyGroup = :studyGroup")
    List<String> findMembers(@Param("studyGroup") StudyGroup studyGroup);

    @Query("select new com.ssafy.prosn.dto.StudyGroupListResponseDto(s.id, s.title, s.currentPerson, s.maxPerson) from StudyGroup s")
    List<StudyGroupListResponseDto> showStudyGroupList();
}
