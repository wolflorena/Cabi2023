package com.example.cabi2023backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class Customer extends User {

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    private String phoneNo;
    private String occupation;
    //LocalDate from Java 8 only has the date not the time too.
    private LocalDate dateOfBirth;
    @Lob
    private byte[] avatar;

    //@Enumerated(EnumType.STRING) is best used for storing the enum values as strings and not as numbers.
    @Enumerated(EnumType.STRING)
    private AccountStatus AccountStatus;

}

