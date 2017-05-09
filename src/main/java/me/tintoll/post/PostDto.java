package me.tintoll.post;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PostDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String code;
}
