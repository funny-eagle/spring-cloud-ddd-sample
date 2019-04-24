package org.nocoder.book.api.controller;

import org.nocoder.commons.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Join controller
 *
 * @author YangJinlong
 */
@RestController
@RequestMapping("/api/join")
public class JoinController {

    @GetMapping("/hello1")
    public BaseResponse hello1() {
        return new BaseResponse("jason");
    }

    @GetMapping("/hello2")
    public BaseResponse hello2() {
        return new BaseResponse("yang");
    }

}
