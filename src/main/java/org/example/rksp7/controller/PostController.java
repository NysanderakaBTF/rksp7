package org.example.rksp7.controller;


import org.example.rksp7.model.Post;
import org.example.rksp7.service.PostService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Mono<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping
    public Flux<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    @GetMapping("/stream")
    public Flux<Post> streamPosts() {
        return postService.getAllPosts()
                .limitRate(10);
    }

    @PostMapping
    public Mono<Post> createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public Mono<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @GetMapping("/author/{author}")
    public Flux<Post> getPostsByAuthor(@PathVariable String author) {
        return postService.getPostsByAuthor(author);
    }
}
