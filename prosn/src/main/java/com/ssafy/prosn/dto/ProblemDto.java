package com.ssafy.prosn.dto;

import com.ssafy.prosn.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by seongmin on 2022/08/09
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDto {
    private Post post;
    private Long id;
    private String title;
    private Long writerId;
    private String writerName;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String dtype;
    private Long numOfLikes;
    private Long numOfDislikes;
}
