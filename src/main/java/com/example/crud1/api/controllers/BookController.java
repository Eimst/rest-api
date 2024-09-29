package com.example.crud1.api.controllers;
import com.example.crud1.models.Book;
import com.example.crud1.repositories.BookRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> getBooksByFilter(@RequestParam(value = "title", required = false) String title,
                                                                @RequestParam(value = "publishYear", required = false) Integer publishYear,
                                                                @RequestParam(value = "author", required = false) String author,
                                                                @RequestParam(value = "rating", required = false) Integer rating) {

        List<Book> books = bookRepository.getFilteredBooks(title, publishYear, author, rating)
                .orElse(Collections.emptyList());

        return ResponseEntity.ok(Map.of(
                "books", books
        ));
    }


    @PatchMapping("/books/update-rating/{id}")
    public ResponseEntity<Map<String, String>> updateBookRating(@PathVariable("id") int id,
                                                                @RequestParam("new-rating") @Min(1) @Max(5) Integer newRating){

        if(bookRepository.updateRating(id, newRating)) {
            return ResponseEntity.ok(Map.of(
                    "message", "Successfully updated rating."
            ));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "The provided book doesn't exist."
        ));
    }
}
