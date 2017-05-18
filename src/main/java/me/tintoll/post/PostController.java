package me.tintoll.post;


import lombok.RequiredArgsConstructor;
import me.tintoll.category.Category;
import me.tintoll.category.CategoryService;
import me.tintoll.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;

    @ModelAttribute("categories")
    public List<Category> categories(){
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public String findByPost(@PathVariable Long id, Model model) {
        Post post = postService.findByIdAndStatus(id, PostStatus.Y);
        if(post == null) throw new NotFoundException(id + " not found");
        model.addAttribute("post", post);
        return "post/post";
    }

    @GetMapping("/new")
    public String newPost(PostDto postDto){
        return "post/new";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.findByIdAndStatus(id, PostStatus.Y);
        if(post == null) throw new NotFoundException(id + " not found");

        PostDto editPost = new PostDto();
        editPost.setTitle(post.getTitle());
        editPost.setContent(post.getContent());
        editPost.setCode(post.getCode());
        editPost.setId(id);
        model.addAttribute("editPost", editPost);

        return "post/edit";
    }

    @PostMapping
    public String createPost(@ModelAttribute @Valid PostDto createPost, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "post/new";
        }
        // post도 comment와 마찬가지로 생성시에 연관관계의 category를 넣어 줬다.
        // 연관관계의 주인만이 읽기, 쓰기가 모두 가능하다. 주인이 아닌 곳에서는 읽기만 가능하다.
        Post post = new Post(createPost.getTitle(),
                             createPost.getContent(),
                             createPost.getCode(),
                             PostStatus.Y,
                             new Category(createPost.getCategoryId()));
        Post newPost = postService.createPost(post);
        model.addAttribute("post",newPost);
        return "redirect:/posts/"+newPost.getId();
    }

    @PutMapping("/{id}/edit")
    public String modifyPost(@PathVariable Long id, @ModelAttribute("editPost") @Valid PostDto createPost,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "post/edit";
        }
        // post도 comment와 마찬가지로 생성시에 연관관계의 category를 넣어 줬다.
        postService.updatePost(id, new Post(createPost.getTitle(),
                createPost.getContent(),
                createPost.getCode(),
                PostStatus.Y,
                new Category(createPost.getCategoryId())));

        return "redirect:/posts/"+id;
    }

    @DeleteMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/#/";
    }

}
