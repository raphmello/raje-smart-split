package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.SplitExpensesGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SplitExpensesGroupRepository extends JpaRepository<SplitExpensesGroup, Long> {
    @Query(value = "SELECT * FROM split_expenses_group seg WHERE seg.creator_id = :userId",nativeQuery = true)
    List<SplitExpensesGroup> findByUserId(Long userId);

    @Query(value = "SELECT * FROM split_expenses_group seg WHERE seg.id = :groupId AND seg.creator_id = :userId", nativeQuery = true)
    Optional<SplitExpensesGroup> findByGroupIdAndUserId(Long groupId, Long userId);
}
