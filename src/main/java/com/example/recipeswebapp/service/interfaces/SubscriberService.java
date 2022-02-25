package com.example.recipeswebapp.service.interfaces;

import com.example.recipeswebapp.model.Subscriber;

import javax.mail.MessagingException;
import java.util.Optional;

public interface SubscriberService {

    void registerSubscriber(String email) throws MessagingException;
}
