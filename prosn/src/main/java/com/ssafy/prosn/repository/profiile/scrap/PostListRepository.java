package com.ssafy.prosn.repository.profiile.scrap;

import com.ssafy.prosn.domain.profile.scrap.PostList;
import com.ssafy.prosn.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * created by seongmin on 2022/08/04
 */
public interface PostListRepository extends JpaRepository<PostList, Long> {
    Optional<PostList> findByTitleAndUser(String title, Member user);
    List<PostList> findByUser(Member user);
}
