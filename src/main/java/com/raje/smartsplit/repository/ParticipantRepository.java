package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query(value = "SELECT * FROM participant p WHERE p.split_group_id = :groupId and p.user_id = :userId", nativeQuery = true)
    Optional<Participant> findByGroupIdAndUserId(Long groupId, Long userId);
}
