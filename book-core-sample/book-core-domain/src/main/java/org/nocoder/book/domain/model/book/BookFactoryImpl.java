package org.nocoder.book.domain.model.book;

import org.nocoder.book.infrastructure.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class BookFactoryImpl implements BookFactory {

    private BookRepository bookRepository;

    @Override
    public Book createBook(long id, String name) throws Exception {
        Book book = new Book(id, name);
        if(bookRepository.addBook(book) <= 0){
            throw new Exception("add book failed!");
        }
        return book;
    }
}
