package me.tintoll.index;


import lombok.RequiredArgsConstructor;
import me.tintoll.post.PostRepository;
import me.tintoll.post.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(Model model, Pageable pageable) {
        model.addAttribute("posts", postRepository.findAll(pageable));
        return "index";
    }

}
