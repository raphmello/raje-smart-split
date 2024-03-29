package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.SplitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SplitGroupRepository extends JpaRepository<SplitGroup, Long> {
    @Query(value = "SELECT * FROM split_group seg "
                        + "INNER JOIN participant p on seg.id = p.split_group_id "
                        + "INNER JOIN users u on u.id = p.user_id "
                        + "WHERE p.user_id = :userId",
            nativeQuery = true)
    List<SplitGroup> findByUserId(Long userId);

    @Query(value =  "SELECT seg.* FROM split_group seg "
                        + "INNER JOIN participant p on seg.id = p.split_group_id "
                        + "WHERE seg.id = :groupId AND p.user_id = :userId",
            nativeQuery = true)
    Optional<SplitGroup> findByGroupIdAndParticipant(Long groupId, Long userId);
}
