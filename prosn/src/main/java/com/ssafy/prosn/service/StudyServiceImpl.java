package com.ssafy.prosn.service;

import com.ssafy.prosn.domain.study.StudyGroup;
import com.ssafy.prosn.domain.study.UserStudy;
import com.ssafy.prosn.domain.user.User;
import com.ssafy.prosn.dto.StudyGroupRequestDto;
import com.ssafy.prosn.dto.StudyGroupResponseDto;
import com.ssafy.prosn.dto.StudyResponseDto;
import com.ssafy.prosn.dto.UserStudyResponseDto;
import com.ssafy.prosn.repository.study.StudyGroupRepository;
import com.ssafy.prosn.repository.study.UserStudyRepository;
import com.ssafy.prosn.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * created by yeomyeong on 2022/07/26
 * updated by yeomyeong on 2022/07/31
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserStudyRepository userStudyRepository;
    private final EntityManager em;

    /**
     * 스터디 생성
     */
    @Transactional
    public void create(StudyGroupRequestDto studyGroupDto) {
    // 스터디장 아이디가 유효한지 확인
    // User user = userRepository.findById(studyGroupDto.getUid()).get();

        StudyGroup studyGroup = StudyGroup.builder().title(studyGroupDto.getTitle())
                .mainText(studyGroupDto.getMainText())
                .maxPerson(studyGroupDto.getMaxPerson())
                .secretText(studyGroupDto.getSecretText())
                .expiredDate(studyGroupDto.getExpiredDate())
                .place(studyGroupDto.getPlace())
                .build();
        studyGroupRepository.save(studyGroup);
    // 스터디장도 스터디가입자
    // UserStudy userStudy = new UserStudy(user, studyGroup);
    // em.persist(userStudy);
    }

    /**
     * 스터디 상세 내용 수정
     */
    @Transactional
    public void update(Long studyGroupId, StudyGroupRequestDto newData) {
        StudyGroup oldData = studyGroupRepository.findById(studyGroupId).get();
        oldData.update(newData);
    }

    /**
     * 스터디 삭제
     */
    public void deleteStudy(StudyGroup studyGroup) {
        List<UserStudy> userStudyList = userStudyRepository.findByStudyGroup(studyGroup);
        for (UserStudy userStudy : userStudyList) {
            userStudyRepository.delete(userStudy);
        }
        studyGroupRepository.delete(studyGroup);
    }

    /**
     * 스터디 가입
     */
    @Transactional
    public Long joinStudy(User user, StudyGroup studyGroup) {
        validateDuplicate(user.getId(), studyGroup.getId());

        UserStudy userStudy = new UserStudy(user, studyGroup);
        studyGroup.addCurrentPerson();
        em.persist(userStudy);

        return userStudy.getId();
    }
    /**
     *  스터디 탈퇴
     */
    @Transactional
    public void removeStudy(User user, StudyGroup studyGroup) {
        if(!userStudyRepository.existsByUserIdAndStudyGroupId(user.getId(), studyGroup.getId()))
            throw new IllegalStateException("가입되지 않은 스터디입니다.");

        UserStudy userStudy = userStudyRepository.findByUserAndStudyGroup(user, studyGroup);
        studyGroup.removeCurrentPerson();
        userStudyRepository.deleteById(userStudy.getId());
    }

    /**
     * 스터디 상세 내용 조회
     */
    public StudyResponseDto showStudyGroup(Long userId, Long studyGroupId){
        StudyGroup findGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 스터디그룹입니다."));
        // 로그인 유저가 스터디 그룹에 가입돼 있으면
        if(userStudyRepository.existsByUserIdAndStudyGroupId(userId, studyGroupId)) {
            UserStudyResponseDto res = UserStudyResponseDto.builder()
                    .id(findGroup.getId())
                    .title(findGroup.getTitle())
                    .currentPerson(findGroup.getCurrentPerson())
                    .maxPerson(findGroup.getMaxPerson())
                    .place(findGroup.getPlace())
                    .mainText(findGroup.getMainText())
                    .secretText(findGroup.getSecretText())
                    .build();

            List<String> members = studyGroupRepository.findMembers(findGroup);
            res.addMembers(members);

            return res;
        }else {
            StudyGroupResponseDto res = StudyGroupResponseDto.builder()
                    .expiredDate(findGroup.getExpiredDate())
                    .mainText(findGroup.getMainText())
                    .maxPerson(findGroup.getMaxPerson())
                    .title(findGroup.getTitle())
                    .currentPerson(findGroup.getCurrentPerson())
                    .place(findGroup.getPlace())
                    .build();
            return res;
        }

    }
    private void validateDuplicate(Long userId, Long sgId) {
        if (userStudyRepository.existsByUserIdAndStudyGroupId(userId, sgId)) {
            throw new IllegalStateException("이미 가입된 스터디입니다.");
        }
    }

}