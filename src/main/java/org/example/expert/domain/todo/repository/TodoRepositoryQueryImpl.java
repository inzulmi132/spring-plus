package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
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

    @Override
    public Page<TodoSearchResponse> searchAll(Pageable pageable, String title, String nickname, LocalDate createdAfter, LocalDate createdBefore) {
        var todos = jpaQueryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title, manager.countDistinct(), comment.countDistinct()
                ))
                .from(todo)
                .leftJoin(user).on(user.eq(todo.user))
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(comment).on(comment.todo.eq(todo))
                .where(
                        Objects.nonNull(title) ? todo.title.contains(title) : null,
                        Objects.nonNull(nickname) ? user.nickname.eq(nickname) : null,
                        Objects.nonNull(createdAfter) ? todo.createdAt.after(createdAfter.atStartOfDay()) : null,
                        Objects.nonNull(createdBefore) ? todo.createdAt.before(createdBefore.atTime(LocalTime.MAX)) : null
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(todos, pageable, todos::size);
    }

    @Override
    public List<Todo> findAllWithComment() {
        return jpaQueryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.comments).fetchJoin()
                .fetch();
    }

    @Override
    public List<Todo> findAllWithManager() {
        return jpaQueryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.managers).fetchJoin()
                .fetch();
    }
}
