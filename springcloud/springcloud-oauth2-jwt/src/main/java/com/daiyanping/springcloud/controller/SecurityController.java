package com.daiyanping.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/security")
public class SecurityController {

    /**
     * 将从服务器获取的认证信息返回给客户端
     * @param principal
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        log.info(principal.toString());
        return principal;
    }
}
