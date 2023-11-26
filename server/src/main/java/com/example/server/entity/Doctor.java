package com.example.server.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Doctor extends User{

    private String address;
    private String phoneNo;

    @Lob
    private byte[] avatar;

    private LocalDate dateOfEmployment;
}