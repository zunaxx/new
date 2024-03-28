package com.example.bilingualb10.repository;

import com.example.bilingualb10.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test,Long> {
    boolean existsById(Long testId);
}