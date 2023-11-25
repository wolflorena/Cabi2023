package com.example.cabi2023backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Doctor extends User{

    private String address;
    private String phoneNo;

    @Lob
    private byte[] avatar;

    private LocalDate dateOfEmployment;
}