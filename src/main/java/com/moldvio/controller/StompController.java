package com.moldvio.controller;

import com.moldvio.models.Greeting;
import com.moldvio.models.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class StompController {

  @MessageMapping("/hello")
  @SendTo("/topic/quiz")
  public Greeting quiz(HelloMessage message) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
  }

}