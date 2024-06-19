package com.example.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formId;

    private String title;
    @Lob
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;
    private Boolean visibility;
    private LocalDate editedDate;
    private LocalTime editedTime;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value="form-formEvents")
    private List<FormEvent> formEvents;
}
