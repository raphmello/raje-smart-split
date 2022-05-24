package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.SplitResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitResultRepository extends JpaRepository<SplitResult, Long> {
}
