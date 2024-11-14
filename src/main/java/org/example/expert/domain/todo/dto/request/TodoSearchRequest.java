package org.example.expert.domain.todo.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoSearchRequest {
    private String title;
    private String nickname;
    private LocalDate createdAfter;
    private LocalDate createdBefore;
}
