package org.nocoder.aggregation.controller;

import org.nocoder.aggregation.assembler.LibraryBookAssembler;
import org.nocoder.aggregation.dto.BookDto;
import org.nocoder.aggregation.dto.LibraryBookDto;
import org.nocoder.aggregation.enumeration.ServiceEnum;

import org.apache.commons.beanutils.BeanUtils;
import org.nocoder.commons.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * library book aggregation controller
 *
 * @author YangJinlong
 */
@RestController
@RequestMapping("/api/aggregation")
public class LibraryBookAggregationController extends BaseController {

    @Resource
    private LibraryBookAssembler libraryBookAssembler;

    @GetMapping("/library-book")
    public BaseResponse aggregate(@RequestParam String libraryId, @RequestParam String bookId, HttpServletRequest request) {
        // step 1. 调用 book-service 接口
        Map<String, Object> param1 = new HashMap<>(2);
        param1.put("bookId", bookId);
        BaseResponse bookResponse1 = rpc(ServiceEnum.BOOK_SERVICE.getName(), "/api/book/book/1", "GET", null, param1, request);

        // step 2. 调用 library-service 接口
        Map<String, Object> param2 = new HashMap<>(2);
        param2.put("libraryId", libraryId);
        BaseResponse bookResponse2 = rpc(ServiceEnum.BOOK_SERVICE.getName(), "/api/book/book/2", "GET", null, param1, request);

        // step 3. 整合数据
        LibraryBookDto libraryBookDto = null;
        try {
            // 构建一个DTO
            BookDto bookDto1 = new BookDto();
            BeanUtils.populate(bookDto1, (HashMap<String, Object>) bookResponse1.getData());
            // 构建另一个DTO
            BookDto bookDto2 = new BookDto();
            BeanUtils.populate(bookDto2, (HashMap<String, Object>) bookResponse2.getData());
            // 整合前两个DTO的数据，返回新的DTO
            libraryBookDto = libraryBookAssembler.assemble(bookDto1, bookDto2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new BaseResponse(libraryBookDto);
    }
}
