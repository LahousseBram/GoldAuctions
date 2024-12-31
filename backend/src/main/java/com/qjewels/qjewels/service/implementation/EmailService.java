package com.qjewels.qjewels.service.implementation;
import com.qjewels.qjewels.utils.exceptions.QJewelsException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService{

    private final JavaMailSender mailSender;
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token) {
        String subject = "Verify your email";
        String verificationUrl = "http://localhost:3000/ConfirmMail?token=" + token;
        String content = "<p>Please click the following link to verify your email:</p>"
                + "<a href=\"" + verificationUrl + "\">Verify</a>";

        sendEmail(to, subject, content);
    }

    public void sendPasswordResetEmail(String to, String token) {
        LOGGER.log(Level.WARNING, "Sending password reset email to: " + to);
        String subject = "Reset your password";
        String resetUrl = "http://localhost:3000/ResetPassword?token=" + token;

        sendEmail(to, subject, content(resetUrl, subject));
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setText(content, "utf-8", "html");
            message.setSubject(subject);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            message.setFrom("info@gold-auction.com");

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new QJewelsException(e.getMessage());
        }
    }

    private String content(String verificationUrl, String mss) {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eaeaea; border-radius: 10px; background-color: #f9f9f9;\">"
                + "<h2 style=\"color: #333; text-align: center;\">" + mss + " </h2>"
                + "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Hi there,</p>"
                + "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Thank you for signing up! To start using our service, please verify your email by clicking the button below:</p>"
                + "<div style=\"text-align: center; margin: 20px 0;\">"
                + "<a href=\"" + verificationUrl + "\" style=\"background-color: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 16px;\">Verify Email</a>"
                + "</div>"
                + "<p style=\"color: #999; font-size: 14px;\">If the button doesn't work, copy and paste the following URL into your browser:</p>"
                + "<p style=\"color: #007bff; word-wrap: break-word;\">" + verificationUrl + "</p>"
                + "<hr style=\"border: none; border-top: 1px solid #eaeaea; margin: 20px 0;\">"
                + "<p style=\"color: #555; font-size: 14px; text-align: center;\">If you didn't request this email, you can safely ignore it.</p>"
                + "<p style=\"color: #555; font-size: 14px; text-align: center;\">Thank you!</p>"
                + "</div>";

    }

    public void sendContactEmail(String to, String from,  String content, String name) {
        String subject = "Mail from QJewels Contact Form from: "+ name + ", his mail is: " + from;

        sendEmail(to, subject, content);
    }
}
