package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByBookTitleContainingIgnoreCase(String title);
    List<Book> findByBookAuthorContainingIgnoreCase(String author);
    List<Book> findByAvailableTrue();
    Page<Book> findByAvailableTrue(Pageable pageable);
}