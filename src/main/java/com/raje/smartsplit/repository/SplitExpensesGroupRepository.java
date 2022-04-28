package com.raje.smartsplit.repository;

import com.raje.smartsplit.entity.SplitExpensesGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitExpensesGroupRepository extends JpaRepository<SplitExpensesGroup, Long> {
}
