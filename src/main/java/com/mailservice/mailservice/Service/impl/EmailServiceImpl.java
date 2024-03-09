package com.mailservice.mailservice.Service.impl;

import com.mailservice.mailservice.Service.EmailService;
import com.mailservice.mailservice.utils.EmailUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String TEXT_HTML_ENCONDING = "text/html";
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;


    private String host = "http://localhost:8888";

    private String fromEmail = "amankumar.ak0012@gmail.com";

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
//            message.setText("Hello " + name + ",\n\n" +
//                    "Welcome to our application. Please click the link below to verify your account.\n\n" +
//                    "http://localhost:8080/verify?token=" + token + "\n\n" +
//                    "Thank you,\n" +
//                    "Aman Kumar");
            message.setText(EmailUtils.getEmailMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            System.out.println("Sending email with attachments"+host);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, host, token));
            //Add attachments
            FileSystemResource fort = new FileSystemResource(new File( System.getProperty("user.home") +"/Desktop/sahjgashjbasjas.png"));
//            FileSystemResource dog = new FileSystemResource(new File( System.getProperty("user.home") +"/Desktop/sahjgashjbasjas.png"));
//            FileSystemResource homework = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/sahjgashjbasjas.png"));
            helper.addAttachment(fort.getFilename(), fort);
//            helper.addAttachment(dog.getFilename(), dog);
//            helper.addAttachment(homework.getFilename(), homework);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, host, token));
            //Add attachments
            FileSystemResource fort = new FileSystemResource(new File( System.getProperty("user.home") +"/Desktop/sahjgashjbasjas.png"));
            FileSystemResource dog = new FileSystemResource(new File( System.getProperty("user.home") +"/Desktop/sahjgashjbasjas.png"));
            FileSystemResource homework = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/sahjgashjbasjas.png"));
            helper.addInline(getContentId(fort.getFilename()), fort);
            helper.addInline(getContentId(dog.getFilename()), dog);
            helper.addInline(getContentId(homework.getFilename()), homework);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

//    @Override
//    @Async
//    public void sendHtmlEmail(String name, String to, String token) {
//        try {
//            Context context = new Context();
//            FileSystemResource imageResource = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/sahjgashjbasjas.png"));
//            if (!imageResource.exists()) {
//                throw new FileNotFoundException("Image file not found");
//            }
//
//            /*context.setVariable("name", name);
//            context.setVariable("url", getVerificationUrl(host, token));*/
//            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token)));
//            String text = templateEngine.process(EMAIL_TEMPLATE, context);
//            MimeMessage message = getMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
//            helper.setPriority(1);
//            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//            helper.setText(text, true);
//            //Add attachments (Optional)
//            /*FileSystemResource fort = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/fort.jpg"));
//            FileSystemResource dog = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/dog.jpg"));
//            FileSystemResource homework = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/homework.docx"));
//            helper.addAttachment(fort.getFilename(), fort);
//            helper.addAttachment(dog.getFilename(), dog);
//            helper.addAttachment(homework.getFilename(), homework);*/
//            emailSender.send(message);
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new RuntimeException(exception.getMessage());
//        }
//    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token)));

            // Load the image as a resource
            FileSystemResource imageResource = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/sahjgashjbasjas.png"));
            if (!imageResource.exists()) {
                throw new FileNotFoundException("Image file not found");
            }

            // Add the image as an inline attachment
            context.setVariable("imageResourceName", imageResource.getFilename());

            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);

            // Add the inline image
            helper.addInline("image", imageResource);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            //helper.setText(text, true);
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            // Add HTML email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCONDING);
            mimeMultipart.addBodyPart(messageBodyPart);

            // Add images to the email body
            BodyPart imageBodyPart = new MimeBodyPart();

            DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Desktop/sahjgashjbasjas.png");

            if (dataSource == null) {
                throw new RuntimeException("Image not found");
            }

            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");


            mimeMultipart.addBodyPart(imageBodyPart);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }



//    @Override
//    @Async
//    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
//        try {
//            MimeMessage message = getMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
//            helper.setPriority(1);
//            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//
//            // Prepare the HTML content
//            Context context = new Context();
//            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token)));
//            String htmlContent = templateEngine.process(EMAIL_TEMPLATE, context);
//            message.setContent(htmlContent, TEXT_HTML_ENCONDING);
//
//            // Add inline image
//            FileSystemResource imageResource = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/sahjgashjbasjas.png"));
//            if (imageResource.exists()) {
//                MimeBodyPart imageBodyPart = new MimeBodyPart();
//                DataSource dataSource = new FileDataSource(imageResource.getFile());
//                imageBodyPart.setDataHandler(new DataHandler(dataSource));
//                imageBodyPart.setHeader("Content-ID", "<image>");
//                // Add the image to the email body
//                MimeMultipart multipart = new MimeMultipart();
//                multipart.addBodyPart(imageBodyPart);
//                // Set the multipart as the content of the message
//                message.setContent(multipart);
//            } else {
//                throw new FileNotFoundException("Image file not found");
//            }
//
//            emailSender.send(message);
//        } catch (Exception e) {
//            // Log the exception and handle it gracefully
//            log.error("Error sending HTML email with embedded files: " + e.getMessage(), e);
//            throw new RuntimeException("Error sending HTML email with embedded files: " + e.getMessage());
//        }
//    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }
}
