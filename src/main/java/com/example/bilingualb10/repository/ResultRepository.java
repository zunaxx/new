package com.example.bilingualb10.repository;

import com.example.bilingualb10.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result>deleteResultByAnswerId(Long answerId);
}