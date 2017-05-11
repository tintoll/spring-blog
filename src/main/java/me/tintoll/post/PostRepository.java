package me.tintoll.post;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본적으로 구현체는 SimpleJpaRepository를 사용하고 findByIdAndStatus이 경우에는 메서드 이름을 읽어 JPQL로 변환시켜 준다.
    Post findByIdAndStatus(Long id, PostStatus status);
}
