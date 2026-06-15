package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import com.library.library_management.entity.Reservation;
import com.library.library_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByBook(Book book);
    Optional<Reservation> findByUserAndBookAndStatus(User user, Book book, Reservation.Status status);
    boolean existsByUserAndBookAndStatus(User user, Book book, Reservation.Status status);
}