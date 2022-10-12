package com.wejuai.accounts.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZM.Wang
 */
@RestController
@RequestMapping("/callback")
public class CallbackController {

    @RequestMapping("/qq")
    public String qq() {
        return "success";
    }
}
