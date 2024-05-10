package com.budgetapp.repository;


import com.budgetapp.domain.BudgetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRecordRepository extends JpaRepository<BudgetRecord, Integer> {
}
