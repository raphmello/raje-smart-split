package com.raje.smartsplit.repository;

import com.raje.smartsplit.dto.utils.splitresult.DataDebtor;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitSimplificationResultRepository extends JpaRepository<SplitSimplificationResult, Long> {
    @Query(value = "SELECT exists " +
                        "(SELECT 1 FROM split_simplification_result ssr " +
                        "where ssr.split_group_id = :groupId)",
            nativeQuery = true)
    Boolean existsSplitSimplificationResultBySplitGroup(Long groupId);

    List<SplitSimplificationResult> findAllBySplitGroup(SplitGroup group);

    @Query(value = "SELECT id, username, amountSum  FROM ( " +
            "                              SELECT sr.participant_id as id, " +
            "                                     u.username as username, " +
            "                                     CASE " +
            "                                         WHEN sr.debt_type = 'DEBTOR' THEN sum(sr.amount) " +
            "                                         WHEN sr.debt_type = 'CREDITOR' THEN sum(sr.amount)*-1 " +
            "                                     END as amountSum " +
            "                              FROM split_result sr " +
            "                                       INNER JOIN participant p on p.id = sr.participant_id " +
            "                                       INNER JOIN users u on u.id = p.user_id " +
            "                              WHERE sr.split_group_id = :groupId " +
            "                              GROUP BY sr.participant_id, u.username, sr.debt_type " +
            "                              ) as BIGGEST_DEBTORS " +
            "            ORDER BY amountSum ASC", nativeQuery = true
    )
    List<Object[]> findAllBySplitGroupOrderByBiggestDebtorsAsc(Long groupId);
}
