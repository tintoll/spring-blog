package me.tintoll.post;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByIdAndStatus(Long id, PostStatus status);
}
