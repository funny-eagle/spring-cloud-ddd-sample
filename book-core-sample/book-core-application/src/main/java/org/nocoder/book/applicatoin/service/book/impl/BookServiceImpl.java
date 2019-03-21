package org.nocoder.book.applicatoin.service.book.impl;

import org.nocoder.book.applicatoin.service.book.BookService;
import org.nocoder.book.domain.model.book.Book;
import org.nocoder.book.infrastructure.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    public int addBook(Book book){
        return bookRepository.addBook(book);
    }
}
