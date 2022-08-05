package com.ssafy.prosn.service;

import com.ssafy.prosn.domain.post.Post;
import com.ssafy.prosn.domain.post.Workbook;
import com.ssafy.prosn.domain.profile.scrap.PostList;
import com.ssafy.prosn.domain.profile.scrap.Scrap;
import com.ssafy.prosn.domain.user.User;
import com.ssafy.prosn.dto.ScrapResponseDto;
import com.ssafy.prosn.repository.post.PostRepository;
import com.ssafy.prosn.repository.profiile.scrap.PostListRepository;
import com.ssafy.prosn.repository.profiile.scrap.ScrapRepository;
import com.ssafy.prosn.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * created by seongmin on 2022/08/04
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScrapServiceImpl implements ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostListRepository postListRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public Scrap save(Long pid, Long lid, Long uid) {
        PostList postList = postListRepository.findById(lid).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 폴더입니다."));
        Post post = postRepository.findById(pid).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글입니다."));
        User user = userRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (!postList.getUser().equals(user)) throw new IllegalStateException("권한이 없습니다.");
        if (post instanceof Workbook) {
            throw new RuntimeException("문제집은 스크랩 할 수 없습니다.");
        }
        return scrapRepository.save(new Scrap(post, postList));
    }

    @Override
    public List<ScrapResponseDto> getScrapList(Long lid) {
        PostList postList = postListRepository.findById(lid).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 폴더입니다."));
        List<Scrap> scraps = scrapRepository.findByPostList(postList);
        List<ScrapResponseDto> result = new ArrayList<>();
        for (Scrap scrap : scraps) {
            result.add(new ScrapResponseDto(scrap.getPost().getId(), scrap.getPost().getTitle()));
        }
        return result;
    }

    // 폴더에서 post 지우기(스크랩 삭제).
    @Override
    public void delete(Long uid, Long id) {
        User user = userRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        Scrap scrap = scrapRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 스크랩입니다."));

        if (!scrap.getPost().getUser().equals(user)) throw new IllegalStateException("삭제할 권한이 없습니다.");
        scrapRepository.delete(scrap);
    }


}