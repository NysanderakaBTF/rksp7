package org.example.rksp7;



import io.r2dbc.spi.ConnectionFactory;
import org.example.rksp7.controller.PostController;
import org.example.rksp7.model.Post;
import org.example.rksp7.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class PostControllerTests {

    @Autowired
    private WebTestClient webTestClient; // Класс для тестирования API

    @MockBean
    private PostService postService; // Мокируем сервис


    @Test
    public void testGetPostById() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setAuthor("Test Author");
        post.setCreatedAt(LocalDateTime.now());

        // Мокируем вызов сервисного метода
        Mockito.when(postService.getPostById(1L)).thenReturn(Mono.just(post));

        // Вызываем GET-запрос к контроллеру
        webTestClient.get()
                .uri("/posts/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Post.class)
                .isEqualTo(post);
    }

    @Test
    public void testGetAllPosts() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Title 1");
        post1.setContent("Content 1");
        post1.setAuthor("Author 1");
        post1.setCreatedAt(LocalDateTime.now());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Title 2");
        post2.setContent("Content 2");
        post2.setAuthor("Author 2");
        post2.setCreatedAt(LocalDateTime.now());

        // Мокируем вызов сервисного метода
        Mockito.when(postService.getAllPosts()).thenReturn(Flux.just(post1, post2));

        // Вызываем GET-запрос ко всем постам
        webTestClient.get()
                .uri("/posts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Post.class)
                .hasSize(2)
                .contains(post1, post2);
    }

    @Test
    public void testStreamPosts() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Stream Post 1");
        post1.setContent("Content 1");
        post1.setAuthor("Author 1");
        post1.setCreatedAt(LocalDateTime.now());

        // Мокируем потоковый запрос
        Mockito.when(postService.getAllPosts()).thenReturn(Flux.just(post1));

        webTestClient.get()
                .uri("/posts/stream")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Post.class)
                .hasSize(1)
                .contains(post1);
    }

    @Test
    public void testCreatePost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("New Post");
        post.setContent("New Content");
        post.setAuthor("Author");
        post.setCreatedAt(LocalDateTime.now());

        // Мокируем создание поста
        Mockito.when(postService.createPost(Mockito.any(Post.class))).thenReturn(Mono.just(post));

        webTestClient.post()
                .uri("/posts")
                .bodyValue(post)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Post.class)
                .isEqualTo(post);
    }

    @Test
    public void testUpdatePost() {
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Old Title");
        existingPost.setContent("Old Content");
        existingPost.setAuthor("Old Author");
        existingPost.setCreatedAt(LocalDateTime.now());

        Post updatedPost = new Post();
        updatedPost.setId(1L);
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated Content");
        updatedPost.setAuthor("Updated Author");
        updatedPost.setCreatedAt(LocalDateTime.now());

        // Мокируем вызов обновления поста
        Mockito.when(postService.updatePost(1L, updatedPost)).thenReturn(Mono.just(updatedPost));

        webTestClient.put()
                .uri("/posts/1")
                .bodyValue(updatedPost)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Post.class)
                .isEqualTo(updatedPost);
    }

    @Test
    public void testDeletePost() {
        // Мокируем вызов удаления поста
        Mockito.when(postService.deletePost(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/posts/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetPostsByAuthor() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Post by Author 1");
        post1.setContent("Content 1");
        post1.setAuthor("Author 1");
        post1.setCreatedAt(LocalDateTime.now());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Post by Author 1");
        post2.setContent("Content 2");
        post2.setAuthor("Author 1");
        post2.setCreatedAt(LocalDateTime.now());

        // Мокируем вызов получения постов по автору
        Mockito.when(postService.getPostsByAuthor("Author 1")).thenReturn(Flux.just(post1, post2));

        webTestClient.get()
                .uri("/posts/author/Author 1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Post.class)
                .hasSize(2)
                .contains(post1, post2);
    }
}
