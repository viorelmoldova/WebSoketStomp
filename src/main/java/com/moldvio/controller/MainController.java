package com.moldvio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping({"", "/"})
    public ModelAndView start(){
        return new ModelAndView("stomp");
    }

}
