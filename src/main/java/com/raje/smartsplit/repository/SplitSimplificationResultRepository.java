package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitSimplificationResultRepository extends JpaRepository<SplitSimplificationResult, Long> {
    Boolean existsSplitSimplificationResultBySplitGroup(Long id);
    Boolean deleteAllBySplitGroup(Long groupId);
}
