package com.minhnphde180174.fu.hsf301assigment1.service.interfaces;

public interface NotificationService {
    void sendVerification(String type, String data);
    void sendResetPasswordEmail(String email, String resetToken) ;

    }
