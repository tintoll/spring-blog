package me.tintoll.post;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class PostDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String code;

    @NotNull
    private Long categoryId;

    private String categoryName;
}
