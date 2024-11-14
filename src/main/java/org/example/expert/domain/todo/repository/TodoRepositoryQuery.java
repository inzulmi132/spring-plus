package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepositoryQuery {
    Optional<Todo> findOneById(Long id);

    Page<TodoSearchResponse> searchAll(Pageable pageable, String title, String nickname, LocalDate createdAfter, LocalDate createdBefore);

    List<Todo> findAllWithComment();

    List<Todo> findAllWithManager();
}
