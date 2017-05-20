package me.tintoll.user;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.tintoll.comment.Comment;
import me.tintoll.post.Post;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"comments", "post"})
@EqualsAndHashCode(exclude = {"comments", "post"})
public class User implements Serializable{

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String github;

    private String avatarUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> post = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Column
    @Lob
    private String bio;

    public User(String email, String name, String github, String avatarUrl) {
        this.email = email;
        this.name = name;
        this.github = github;
        this.avatarUrl = avatarUrl;
    }

    User() {
    }
}
