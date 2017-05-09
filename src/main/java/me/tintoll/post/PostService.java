package me.tintoll.post;


import lombok.RequiredArgsConstructor;
import me.tintoll.exception.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    /*
        @RequiredArgsConstructor를 해주므로서 아래와 같이 생성이 된다. @Autowired를 안해줘도 됨

        @ConstructorProperties({"postRepository"})
        public PostService(PostRepository postRepository) {
            this.postRepository = postRepository;
        }
    */

    public final PostRepository postRepository;



    public Post createPost(Post post) {
        post.setRegDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    /*
        여기서 눈에 띄는것은 updatePost메서드와 deletePost메서드이다.
        조회를 한뒤에 업데이트를 날리는 메서드가 없다. 이것을 이해하려면 JPA의 영속성에 대해 알아야 한다.
        영속성에 대해서는 양이 많으니 여기서는 이렇게만 알고 있자. Transaction 범위에서는 영속상태의 객체를 만들수 있다.
        일단 이것만 기억하자. 영속상태인 객체가 변경되고 트랜잭션이 끝나면 변경된 객체의 속성을 감지하여 update 쿼리를 날린다.
        이것을 변경감지라고 한다. 그래서 update메서드는 존재 하지 않고 영속상태의 객체에 변경만 해주었다.
     */
    public Post updatePost(Long id, Post post) {
        Post oldPost = postRepository.findByIdAndStatus(id, PostStatus.Y);
        if(oldPost == null) throw new NotFoundException( id + " not found!!");

        oldPost.setTitle(post.getTitle());
        oldPost.setContent(post.getContent());
        oldPost.setCode(post.getCode());
        return oldPost;
    }

    public void deletePost(Long id) {
        Post oldPost = postRepository.findByIdAndStatus(id, PostStatus.Y);
        if(oldPost == null) throw new NotFoundException( id + " not found!!");
        oldPost.setStatus(PostStatus.N);
    }

    public Post findByIdAndStatus(Long id, PostStatus status) {
        Post post = postRepository.findByIdAndStatus(id, PostStatus.Y);
        if(post == null) throw new NotFoundException( id + " not found!!");
        return post;
    }
}
