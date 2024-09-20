package org.example.rksp7.repository;

import org.example.rksp7.model.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends R2dbcRepository<Post, Long> {
    Flux<Post> findByAuthor(String author);
}