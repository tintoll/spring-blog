package me.tintoll.post;


import lombok.Data;
import me.tintoll.category.Category;
import me.tintoll.comment.Comment;
import me.tintoll.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @Lob
    @NotNull
    private String content;

    @Lob
    private String code;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private LocalDateTime regDate;

    /*
        @ManyToOne은 다대일이라는 표현이다. 속성은 많지만 다 설명하면 jpa시간이 되버리니 생략한다.
        @JoinColumn은 외래키를 매핑할 때 사용한다. name이라는 속성에 매핑할 외래 키 이름을 지정해주면 된다. 하지만 이 이노테이션은 생략 가능하다.
        Post에 category는 mappedBy가 없으므로 post가 연관관계의 주인이 된다. 물론 연관관계의 주인을 바꿀 수는 있으나 성능과 관리 측면에서 권장하지는 않는다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "post" , fetch = FetchType.LAZY)
    private List<Comment> comments;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    Post(){
    }

    public Post(Long id){
        this.id = id;
    }
    public Post(String title, PostStatus status){
        this.title = title;
        this.status = status;
    }
    public Post(Long id, String title, String content, String code, PostStatus status){
        this.id = id;
        this.title = title;
        this.content = content;
        this.code = code;
        this.status = status;
    }

    public Post(String title, String content, String code, PostStatus status){
        this.title = title;
        this.content = content;
        this.code = code;
        this.status = status;
    }

    public Post(String title, String content, String code, PostStatus status, Category category) {
        this.title = title;
        this.content = content;
        this.code = code;
        this.status = status;
        this.category = category;
    }
}
