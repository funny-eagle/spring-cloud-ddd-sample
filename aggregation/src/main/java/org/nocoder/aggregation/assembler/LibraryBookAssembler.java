package org.nocoder.aggregation.assembler;

import org.nocoder.aggregation.dto.BookDto;
import org.nocoder.aggregation.dto.LibraryBookDto;
import org.nocoder.aggregation.dto.LibraryDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * library book assembler
 *
 * @author YangJinlong
 */
@Component
public class LibraryBookAssembler {

    public LibraryBookDto assemble(BookDto bookDto, LibraryDto libraryDto) {
        if (bookDto == null || libraryDto == null) {
            return null;
        }
        LibraryBookDto libraryBookDto = new LibraryBookDto();
        // TODO transfer to LibraryBookDto
        return libraryBookDto;
    }

    public LibraryBookDto assemble(BookDto bookDto1, BookDto bookDto2) {
        if (bookDto1 == null || bookDto2 == null) {
            return null;
        }
        LibraryBookDto libraryBookDto = new LibraryBookDto();
        libraryBookDto.setBook(Arrays.asList(bookDto1.getName(), bookDto2.getName()));
        return libraryBookDto;
    }

}
