package com.example.recipeswebapp.web;

import com.example.recipeswebapp.model.Subscriber;
import com.example.recipeswebapp.service.interfaces.SubscriberService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/sendEmail")
public class EmailController {

    private final SubscriberService subscriberService;
    public EmailController( SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public String sendEmail(@RequestParam String emailAddress) throws MessagingException {
        subscriberService.registerSubscriber(emailAddress);
        return "redirect:/recipes?subscribed=true";
    }

}
