package com.ssafy.prosn.dto;

import com.ssafy.prosn.domain.post.Post;
import com.ssafy.prosn.domain.post.PostTag;
import com.ssafy.prosn.domain.post.PostType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * created by seongmin on 2022/08/07
 * updated by seongmin on 2022/08/15
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PostResponseDto {
    private List<Content> content = new ArrayList<>();
    private int totalPages;
    private Long totalElements;

    public void addPost(List<? extends Post> posts, int totalPages, Long totalElements) {

        for (Post post : posts) {
            List<String> tags = new ArrayList<>();
            for (PostTag postTag : post.getPostTags()) {
                tags.add(postTag.getTag().getCode());
            }
            this.content.add(new Content(
                    post.getId(),
                    post.getPtype(),
                    new UserResponseDto(post.getUser().getId(), post.getUser().getName()),
                    post.getTitle(),
                    post.getMainText(),
                    post.getViews(),
                    post.getNumOfLikes(),
                    post.getNumOfDislikes(),
                    tags,
                    post.getCreated(),
                    post.getUpdated()
            ));
        }
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    private static class Content {
        private Long id;
        private PostType ptype;
        private UserResponseDto writer;
        private String title;

        private String mainText;
        private Integer views;
        private Long numOfLikes;
        private Long numOfDislikes;
        private List<String> tags;
        private LocalDateTime created;
        private LocalDateTime updated;

        public Content(Long id, PostType ptype, UserResponseDto writer, String title, String mainText, Integer views, Long numOfLikes, Long numOfDislikes, List<String> tags, LocalDateTime created, LocalDateTime updated) {
            this.id = id;
            this.ptype = ptype;
            this.writer = writer;
            this.title = title;
            this.mainText = mainText;
            this.views = views;
            this.numOfLikes = numOfLikes;
            this.numOfDislikes = numOfDislikes;
            this.tags = tags;
            this.created = created;
            this.updated = updated;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMainText() {
            return mainText;
        }

        public void setMainText(String mainText) {
            this.mainText = mainText;
        }

        public PostType getPtype() {
            return ptype;
        }

        public void setPtype(PostType ptype) {
            this.ptype = ptype;
        }

        public UserResponseDto getWriter() {
            return writer;
        }

        public void setWriter(UserResponseDto writer) {
            this.writer = writer;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getViews() {
            return views;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public void setViews(Integer views) {
            this.views = views;
        }

        public Long getNumOfLikes() {
            return numOfLikes;
        }

        public void setNumOfLikes(Long numOfLikes) {
            this.numOfLikes = numOfLikes;
        }

        public Long getNumOfDislikes() {
            return numOfDislikes;
        }

        public void setNumOfDislikes(Long numOfDislikes) {
            this.numOfDislikes = numOfDislikes;
        }

        public LocalDateTime getCreated() {
            return created;
        }

        public void setCreated(LocalDateTime created) {
            this.created = created;
        }

        public LocalDateTime getUpdated() {
            return updated;
        }

        public void setUpdated(LocalDateTime updated) {
            this.updated = updated;
        }
    }
}
