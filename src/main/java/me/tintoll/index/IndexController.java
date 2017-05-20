package me.tintoll.index;


import lombok.RequiredArgsConstructor;
import me.tintoll.config.Navigation;
import me.tintoll.config.Section;
import me.tintoll.post.PostRepository;
import me.tintoll.post.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Navigation(Section.HOME)
public class IndexController {

    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(Model model, Pageable pageable) {
        model.addAttribute("posts", postRepository.findAll(pageable));
        return "index";
    }

}
