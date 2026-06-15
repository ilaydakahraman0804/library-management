package com.library.library_management.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "year_of_publication")
    private Integer yearOfPublication;

    @Column(name = "available", nullable = false)
    private Boolean available = true;
}