package org.nocoder.book.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @GetMapping("/hello")
    public String hello(){
        return "hello, book service!";
    }

    @PutMapping("/add")
    public String addBook(){
        return "add book";
    }
}
