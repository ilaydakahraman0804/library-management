package com.library.library_management.controller;

import com.library.library_management.entity.Book;
import com.library.library_management.entity.Reservation;
import com.library.library_management.entity.User;
import com.library.library_management.service.BookService;
import com.library.library_management.service.ReservationService;
import com.library.library_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final BookService bookService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestParam Long userId,
                                               @RequestParam String isbn) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new RuntimeException("Kitap bulunamadı!"));
            Reservation reservation = reservationService.createReservation(user, book);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReservations(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
            List<Reservation> reservations = reservationService.getUserReservations(user);
            return ResponseEntity.ok(reservations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}