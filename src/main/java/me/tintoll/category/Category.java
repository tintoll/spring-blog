package me.tintoll.category;


import lombok.Data;
import me.tintoll.post.Post;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private LocalDateTime regDate;
    /*
        @OneToMany 어노테이션으로 우리는 일대다 관계를 만들 수 있다.
        속성 중 mappedBy는 설명할게 좀 있으니 그냥 연관관계의 주인이 아니라고만 알고 있자.
        fetch의 경우에는 즉시로딩 과 지연로딩을 설정할 수 있다.
        즉시로딩일 경우에는 Category를 가져올때 무조건 post도 가져온다는 의미이고
        지연로딩일 경우에는 Category를 가져올때 post는 프록시 객체로 가져오고 만약 post를 사용한다면 그때 쿼리해서 가져온다.
        말은 쉽지 실제 모르고 개발하면 쉽지 않다. 영속성과 관련도 많이 있어서.. 특별한 경우가 아니라면 지연로딩을 추천하고 있다.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Post> post = new ArrayList<>();


    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }
    public Category(Long id, String name) {
        this.name = name;
        this.id = id;
    }
}
