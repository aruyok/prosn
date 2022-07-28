package com.ssafy.prosn.repository.study;

import com.ssafy.prosn.domain.study.StudyGroup;
import com.ssafy.prosn.domain.study.UserStudy;
import com.ssafy.prosn.domain.user.User;
import com.ssafy.prosn.dto.UserStudyListResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * created by yeomyeong on 2022/07/28
 */
public interface UserStudyRepository extends JpaRepository<UserStudy, Long>, UserStudyRepositoryCustom {
//    // 사용자별 스터디그룹 목록 (제목, 내용20자) 조회
//    @Query("select new com.ssafy.prosn.dto.UserStudyDto(s.title, substring(s.mainText,0,20)) from StudyGroup s join UserStudy u on u.studyGroup = s.id where u.user=:user")
//    List<UserStudyListResponseDto> findByUser(@Param("user") User user);

    // 탈퇴하기 위한 스터디그룹 단건 조회
    @Query("select u.id from UserStudy u where u.user = :user and u.studyGroup = :studyGroup")
    Long findByUserAndStudyGroup(@Param("user") User user, @Param("studyGroup") StudyGroup studyGroup);
}

//select * from UserStudy u join  StudyGroup s on u.sgId = s.sgId where u.uid = "사용자id"
//select s.name, substr(s.maintext,0,20) from USERSTUDY u join  STUDYGROUP s on u.sgid = s.Id where u.uid =1;