package me.tintoll.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
/*
    @WebMvcTest는 Spring Boot 1.4에 추가된 어노테이션으로 간단하게 Web만 Test를 할 수 있다.
    딱 Controller만 설정하기 때문에 때문에 테스트 시간이 짧다.
 */
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @Test
    public void findByPost() throws Exception {

        given(this.postService.findByIdAndStatus(anyLong(), anyObject()))
                .willReturn(new Post("제목","컨텐츠","마크다운",PostStatus.Y));

        MvcResult mvcResult = this.mvc.perform(get("/posts/{id}",1))
                                      .andExpect(status().isOk())
                                      .andReturn();
        Post post = (Post) mvcResult.getModelAndView().getModel().get("post");
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("컨텐츠");
        assertThat(post.getCode()).isEqualTo("마크다운");
        assertThat(post.getStatus()).isEqualTo(PostStatus.Y);
    }

    @Test
    public void newPost() throws Exception {
        this.mvc.perform(get("/posts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/new"))
                .andReturn();
    }

    @Test
    public void editPost() throws Exception {
        /*
            given() 메서드와 willReturn() 메서드는 가짜 객체들 만들기 위함이다.
            예를 들어 given(this.postService.findByIdAndStatus(anyLong(), anyObject())).willReturn(new Post(“제목”, “컨텐츠”,”마크다운”, PostStatus.Y))
            이 코드는 postService.findByIdAndStatus 호출하면 return 값으로 new Post(“제목”, “컨텐츠”,”마크다운”, PostStatus.Y)를 받아 올거라는 가짜로 만든 객체다.
            실제 postService 메서드는 호출하지 않으며 AOP를 통해 가짜 객체만 넣어서 리턴한다.
         */

        given(this.postService.findByIdAndStatus(anyLong(), anyObject())).willReturn(new Post("제목", "컨텐츠","마크다운", PostStatus.Y));
        MvcResult mvcResult = this.mvc.perform(get("/posts/edit/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();

        PostDto postDto = (PostDto) mvcResult.getModelAndView().getModel().get("editPost");
        assertThat(postDto.getTitle()).isEqualTo("제목");
        assertThat(postDto.getContent()).isEqualTo("컨텐츠");
        assertThat(postDto.getCode()).isEqualTo("마크다운");
    }

    @Test
    public void editPostNotFoundException() throws Exception {
        given(this.postService.findByIdAndStatus(1L, PostStatus.Y)).willReturn(new Post("제목", "컨텐츠","마크다운", PostStatus.Y));
        this.mvc.perform(get("/posts/edit/{id}", 2))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPost() throws Exception {
        Post post = new Post(1L, "제목1", "컨텐츠1","마크다운1", PostStatus.Y);
        given(postService.createPost(any())).willReturn(post);

        this.mvc.perform(post("/posts")
                .param("title","제목1")
                .param("content","컨텐츠1")
                .param("code","마크다운1"))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, "/posts/1"));
    }

    @Test
    public void createPostValid() throws Exception {
        this.mvc.perform(post("/posts")
                .param("title","제목1")
                .param("code","마크다운1"))
                .andExpect(view().name("post/new"));

    }
    @Test
    public void modifyPost() throws Exception {
        Post post = new Post(1L, "제목2", "컨텐츠2","마크다운2", PostStatus.Y);
        given(postService.updatePost(any(),any())).willReturn(post);

        this.mvc.perform(put("/posts/{id}/edit", 1L)
                .param("title","제목2")
                .param("content","컨텐츠2")
                .param("code","마크다운2"))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, "/posts/1"));
    }
    @Test
    public void deletePost() throws Exception {
        doNothing().when(postService).deletePost(anyLong());
        this.mvc.perform(delete("/posts/{id}/delete", 1L))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, "/#/"));

    }

}