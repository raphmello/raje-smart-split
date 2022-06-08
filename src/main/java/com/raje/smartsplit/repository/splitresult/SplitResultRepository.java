package com.raje.smartsplit.repository.splitresult;

import com.raje.smartsplit.entity.SplitResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitResultRepository extends JpaRepository<SplitResult, Long> {
    @Query(value = "SELECT * FROM split_result sr " +
            "WHERE sr.split_group_id = :groupId", nativeQuery = true)
    List<SplitResult> findAllBySplitGroup(Long groupId);
}
