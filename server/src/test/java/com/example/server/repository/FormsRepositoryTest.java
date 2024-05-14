package com.example.server.repository;

import com.example.server.repository.entity.Form;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class FormsRepositoryTest {

    @Autowired
    private FormRepository formRepository;

    @Test
    public void testCreateAndRetrieveForm(){
        Form savedForm = new Form();
        savedForm.setTitle("GDPR");
        savedForm.setDescription("Do you agree with the GDPR statements?");
        savedForm.setVisibility(true);
        formRepository.save(savedForm);

        Form retrievedForm = formRepository.findById(savedForm.getFormId()).orElse(null);

        assertNotNull(retrievedForm);
        assertEquals(retrievedForm.getTitle(), "GDPR");
        assertEquals(retrievedForm.getVisibility(), true);
    }
}
