package com.example.server.service.implementation;

import com.example.server.repository.DoctorRepository;
import com.example.server.repository.entity.Doctor;
import com.example.server.service.SendEmailService;
import com.example.server.utils.PasswordGenerator;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Template;
import freemarker.template.Configuration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final DoctorRepository doctorRepository;

    private final JavaMailSender emailSender;

    private final Configuration configuration;
    private final PasswordEncoder passwordEncoder;

    public SendEmailServiceImpl(DoctorRepository doctorRepository, JavaMailSender emailSender, Configuration configuration, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.emailSender = emailSender;
        this.configuration = configuration;
        this.passwordEncoder = passwordEncoder;
    }

    private String adminEmail = "dentify2023@gmail.com";
    private final String companyName = "Cabi 2023";

    @Override
    public void sendPasswordToDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        String password = PasswordGenerator.generatePassayPassword(15);
        String subject = "Account Activation for "  + companyName + " app";
        sendEmailUtils("welcome-template.ftl", doctorId, password, subject);
        String encodedPassword = passwordEncoder.encode(password);
        doctor.setPassword(encodedPassword);
        doctorRepository.save(doctor);
    }

    private void sendEmailUtils(String fileName, Long doctorId, String password, String subject) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        String emailTo = doctor.getEmail();
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("firstName", doctor.getFirstName());
        mapUser.put("lastName", doctor.getLastName());
        mapUser.put("email", doctor.getEmail());
        mapUser.put("companyName", companyName);
        mapUser.put("password", password);
        mapUser.put("date", new Date().toString());
        mapUser.put("imageUrl", "./welcome.jpg");
        try {
            Template template  = configuration.getTemplate(fileName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, mapUser);
            sendEmail(emailTo, subject, htmlTemplate);
        } catch (IOException | TemplateException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void sendEmail(String emailTo, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(adminEmail);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailSender.send(message);
            System.out.println("Email sent successfully to: " + emailTo);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
