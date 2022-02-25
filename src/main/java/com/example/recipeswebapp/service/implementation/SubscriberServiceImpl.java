package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.Subscriber;
import com.example.recipeswebapp.model.exceptions.SubscriberExistsException;
import com.example.recipeswebapp.repository.SubscriberRepository;
import com.example.recipeswebapp.service.interfaces.SubscriberService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final JavaMailSender mailSender;
    private static String SENDER="noreply.tastethejoy@gmail.com";

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, JavaMailSender mailSender) {
        this.subscriberRepository = subscriberRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void registerSubscriber(String email) throws MessagingException {
        if(subscriberRepository.findByEmail(email).isPresent()){
            throw new SubscriberExistsException(email);
        }
        subscriberRepository.save(new Subscriber(email));
        sendEmail(email);

    }

    private void sendEmail(String toEmail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(SENDER);
        helper.setTo(toEmail);
        helper.setSubject("Subscription to newsletter Recipes Guide: Taste the Joy confirmed!");
        helper.setText(String.format("<h1>Dear %s,<br> </h1> <h2><i>Thank you for your subscription!</i></h2> <br><h3>Best regards, <br>Taste the Joy team!<h3/>",toEmail),true);

        mailSender.send(message);
    }

}
