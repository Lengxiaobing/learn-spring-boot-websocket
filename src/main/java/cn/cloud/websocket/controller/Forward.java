package cn.cloud.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Forward {

    @RequestMapping("/")
    public String login() {
        return "login";
    }

}