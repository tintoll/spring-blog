package me.tintoll.comment;


import lombok.RequiredArgsConstructor;
import me.tintoll.post.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String createComment(@ModelAttribute @Valid CommentDto commentDto,
                                BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "post/post";
        }
        /*
            comment를 생성할 때 해당하는 연관관계의 post를 넣어 줘야 한다.
            comment에는 post와의 연관관계가 설정 되어 있다.
            그래야 comment의 외래키에 postId가 들어간다.
            원래는 postId가 있는지 검증도 해야하고 영속 객체를 넣는게 더 좋은 방법인듯 하다.
            하지만 여기서는 무조건 있는 값이 넘어 온다고 가정했다. 그런 로직은 service에 있는게 맞을 듯 하다.
         */
        model.addAttribute("comment", commentService.createComment(
                new Comment(commentDto.getContent(),
                new Post(commentDto.getPostId()))));

        return "redirect:/posts/"+commentDto.getPostId();
    }

    @DeleteMapping("/{postId}/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId){

        commentService.deleteComment(commentId);
        return "redirect:/posts/"+postId;
    };

}
