package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;

import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findOneById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(todo)
                        .from(todo)
                        .leftJoin(todo.user, user).fetchJoin()
                        .where(todo.id.eq(id))
                        .fetchOne()
        );
    }
}
