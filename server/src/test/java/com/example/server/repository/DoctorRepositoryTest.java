package com.example.server.repository;

import com.example.server.entity.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void testSaveAndRetrieveDoctor(){
        Doctor savedDoctor = new Doctor();
        savedDoctor.setFirstName("Ciorea");
        savedDoctor.setLastName("Edmond");
        doctorRepository.save(savedDoctor);
        Doctor retrievedDoctor = doctorRepository.findById(savedDoctor.getId()).orElse(null);

        assertNotNull(retrievedDoctor);
        assertEquals("Ciorea",retrievedDoctor.getFirstName());
        assertEquals("Edmond",retrievedDoctor.getLastName());
    }
}
