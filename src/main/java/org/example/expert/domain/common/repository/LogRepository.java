package org.example.expert.domain.common.repository;

import org.example.expert.domain.common.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
