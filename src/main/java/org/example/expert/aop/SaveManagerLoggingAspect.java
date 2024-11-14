package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.expert.domain.common.entity.Log;
import org.example.expert.domain.common.repository.LogRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
public class SaveManagerLoggingAspect {
    private final HttpServletRequest request;
    private final LogRepository logRepository;

    @Transactional
    @Before("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager(..))")
    public void logBeforeSaveManager() {
        String userId = (String)request.getAttribute("userId");
        Long reqUserId = userId == null || userId.isBlank() ? null : Long.valueOf(userId);
        String reqUrl = request.getRequestURI();
        String reqMethod = request.getMethod();
        Log log = Log.builder().reqUserId(reqUserId).reqURl(reqUrl).reqMethod(reqMethod).build();
        logRepository.save(log);
    }
}
