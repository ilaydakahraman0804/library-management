package com.library.library_management.controller;

import com.library.library_management.entity.BorrowTransaction;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowTransactionRepository;
import com.library.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

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
        report.put("overdueBooks", borrowRepository.findByStatus(BorrowTransaction.Status.OVERDUE).size());
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

    @GetMapping("/most-borrowed")
    public List<Map<String, Object>> getMostBorrowedBooks() {
        List<BorrowTransaction> all = borrowRepository.findAll();
        Map<String, Long> countMap = all.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getBook().getIsbn(),
                        Collectors.counting()
                ));
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("isbn", e.getKey());
                    item.put("borrowCount", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/active-users")
    public List<Map<String, Object>> getActiveUsers() {
        List<BorrowTransaction> all = borrowRepository.findAll();
        Map<Long, Long> countMap = all.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getUser().getId(),
                        Collectors.counting()
                ));
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("userId", e.getKey());
                    item.put("borrowCount", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/monthly-stats")
    public Map<String, Long> getMonthlyStats() {
        List<BorrowTransaction> all = borrowRepository.findAll();
        return all.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getBorrowDate().getYear() + "-" +
                                String.format("%02d", t.getBorrowDate().getMonthValue()),
                        Collectors.counting()
                ));
    }

    @GetMapping("/check-overdue")
    public ResponseEntity<?> checkAndUpdateOverdue() {
        List<BorrowTransaction> borrowed = borrowRepository.findByStatus(BorrowTransaction.Status.BORROWED);
        long count = 0;
        for (BorrowTransaction t : borrowed) {
            if (t.getDueDate().isBefore(LocalDate.now())) {
                t.setStatus(BorrowTransaction.Status.OVERDUE);
                borrowRepository.save(t);
                count++;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("updatedToOverdue", count);
        return ResponseEntity.ok(result);
    }
}