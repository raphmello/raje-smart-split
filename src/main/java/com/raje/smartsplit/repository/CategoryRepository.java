package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.BillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<BillCategory, Long> {
    @Query(value = "SELECT DISTINCT(bc.*) FROM split_group "
                        + "INNER JOIN participant p on split_group.id = p.split_group_id "
                        + "INNER JOIN bill b on p.id = b.participant_id "
                        + "INNER JOIN bill_category bc on bc.id = b.category_id "
                        + "WHERE split_group_id = :groupId", nativeQuery = true)
    Set<BillCategory> findAllByGroupId(Long groupId);
}
