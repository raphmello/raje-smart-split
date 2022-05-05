package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.SplitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SplitGroupRepository extends JpaRepository<SplitGroup, Long> {
    @Query(value = "SELECT * FROM smartsplit.public.split_group seg WHERE seg.creator_id = :userId",nativeQuery = true)
    List<SplitGroup> findByUserId(Long userId);

    @Query(value = "SELECT * FROM smartsplit.public.split_group seg WHERE seg.id = :groupId AND seg.creator_id = :userId", nativeQuery = true)
    Optional<SplitGroup> findByGroupIdAndUserId(Long groupId, Long userId);
}
