package com.library.library_management.service;

import com.library.library_management.entity.Book;
import com.library.library_management.entity.Reservation;
import com.library.library_management.entity.User;
import com.library.library_management.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation createReservation(User user, Book book) {
        if (reservationRepository.existsByUserAndBookAndStatus(
                user, book, Reservation.Status.PENDING)) {
            throw new RuntimeException("Bu kitap için zaten rezervasyonunuz var!");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(Reservation.Status.PENDING);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getUserReservations(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getBookReservations(Book book) {
        return reservationRepository.findByBook(book);
    }

    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı!"));
        reservation.setStatus(Reservation.Status.CANCELLED);
        return reservationRepository.save(reservation);
    }
}