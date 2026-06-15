package com.library.library_management.service;

import com.library.library_management.entity.Book;
import com.library.library_management.entity.BorrowTransaction;
import com.library.library_management.entity.User;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowTransactionRepository borrowRepository;
    private final BookRepository bookRepository;

    public BorrowTransaction borrowBook(User user, Book book) {
        if (!book.getAvailable()) {
            throw new RuntimeException("Bu kitap şu an müsait değil!");
        }
        book.setAvailable(false);
        bookRepository.save(book);

        BorrowTransaction transaction = new BorrowTransaction();
        transaction.setUser(user);
        transaction.setBook(book);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(14));
        transaction.setStatus(BorrowTransaction.Status.BORROWED);
        return borrowRepository.save(transaction);
    }

    public BorrowTransaction returnBook(Long transactionId) {
        BorrowTransaction transaction = borrowRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("İşlem bulunamadı!"));

        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus(BorrowTransaction.Status.RETURNED);

        Book book = transaction.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowRepository.save(transaction);
    }

    public List<BorrowTransaction> getUserTransactions(User user) {
        return borrowRepository.findByUser(user);
    }

    public List<BorrowTransaction> getAllBorrowedBooks() {
        return borrowRepository.findByStatus(BorrowTransaction.Status.BORROWED);
    }
}