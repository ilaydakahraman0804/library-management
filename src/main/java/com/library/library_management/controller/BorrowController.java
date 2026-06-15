package com.library.library_management.controller;

import com.library.library_management.entity.Book;
import com.library.library_management.entity.BorrowTransaction;
import com.library.library_management.entity.User;
import com.library.library_management.service.BorrowService;
import com.library.library_management.service.BookService;
import com.library.library_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final BookService bookService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> borrowBook(@RequestParam Long userId,
                                        @RequestParam String isbn) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new RuntimeException("Kitap bulunamadı!"));
            BorrowTransaction transaction = borrowService.borrowBook(user, book);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<?> returnBook(@PathVariable Long transactionId) {
        try {
            BorrowTransaction transaction = borrowService.returnBook(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserTransactions(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
            List<BorrowTransaction> transactions = borrowService.getUserTransactions(user);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    public List<BorrowTransaction> getAllBorrowedBooks() {
        return borrowService.getAllBorrowedBooks();
    }
}