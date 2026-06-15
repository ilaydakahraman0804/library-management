package com.library.library_management.controller;

import com.library.library_management.entity.BorrowTransaction;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowTransactionRepository;
import com.library.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final BookRepository bookRepository;
    private final BorrowTransactionRepository borrowRepository;
    private final UserRepository userRepository;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Map<String, Object> report = new HashMap<>();
        report.put("totalBooks", bookRepository.count());
        report.put("availableBooks", bookRepository.findByAvailableTrue().size());
        report.put("totalUsers", userRepository.count());
        report.put("activeBorrows", borrowRepository.findByStatus(BorrowTransaction.Status.BORROWED).size());
        return report;
    }

    @GetMapping("/borrowed")
    public List<BorrowTransaction> getBorrowedBooks() {
        return borrowRepository.findByStatus(BorrowTransaction.Status.BORROWED);
    }

    @GetMapping("/overdue")
    public List<BorrowTransaction> getOverdueBooks() {
        return borrowRepository.findByStatus(BorrowTransaction.Status.OVERDUE);
    }
}