package org.nocoder.book.applicatoin.service.book.impl;

import org.apache.commons.lang.Validate;
import org.nocoder.book.applicatoin.assembler.book.BookAssembler;
import org.nocoder.book.applicatoin.dto.book.BookDto;
import org.nocoder.book.applicatoin.exception.BookServiceException;
import org.nocoder.book.applicatoin.service.book.BookService;
import org.nocoder.book.domain.model.book.Book;
import org.nocoder.book.domain.model.book.BookFactory;
import org.nocoder.book.infrastructure.repository.BookRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * BookServiceImpl
 *
 * @author yangjinlong
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    private BookFactory bookFactory;

    @Resource
    private BookRepository bookRepository;

    @Resource
    private BookAssembler bookAssembler;

    @Override
    public int addBook(BookDto bookDto) throws BookServiceException {
        try {
            // 1.使用 BookFactory 创建 domain 对象
            Book book = bookFactory.createBook(bookDto.getName());
            // 2.使用 mybatis 进行数据持久化操作
            bookRepository.addBook(book);
        } catch (Exception e) {
            throw new BookServiceException(e.getMessage());
        }
        return 1;
    }


    @Override
    public BookDto getBookById(long id) throws BookServiceException {
        // 参数校验
        Validate.isTrue(id > 0);
        // 查询数据
        Book book = bookRepository.getById(id);
        // 组装DTO
        return bookAssembler.convertToBookDto(book);
    }
}
