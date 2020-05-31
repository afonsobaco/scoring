package com.aviz.repository;

import java.util.List;
import java.util.Optional;

import com.aviz.model.UserScore;

import org.springframework.data.repository.CrudRepository;

public interface UserScoreRepository extends CrudRepository<UserScore, Long> {

	Optional<UserScore> findByUserId(Long id);

	List<UserScore> findTop20000ByOrderByScoreDesc();

	Long countByScoreGreaterThan(Long score);
}