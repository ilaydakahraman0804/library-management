package com.library.library_management.repository;

import com.library.library_management.entity.BorrowTransaction;
import com.library.library_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {
    List<BorrowTransaction> findByUser(User user);
    List<BorrowTransaction> findByStatus(BorrowTransaction.Status status);
    boolean existsByUserAndStatus(User user, BorrowTransaction.Status status);
}