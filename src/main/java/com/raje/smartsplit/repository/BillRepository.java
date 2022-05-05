package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(value = "SELECT * FROM smartsplit.public.bill b WHERE b.split_group_id = :groupId", nativeQuery = true)
    List<Bill> findBySplitGroup(Long groupId);
}
