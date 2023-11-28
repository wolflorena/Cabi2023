package com.example.server.repository;

import com.example.server.entity.Customer;
import com.example.server.entity.Form;
import com.example.server.entity.FormSeenByCustomer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class FormsRepositoryTest {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormSignedByCustomerRepository formSignedByCustomerRepository;

    @Autowired
    private FormSeenByCustomerRepository formSeenByCustomerRepository;

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

    @Test
    public void testNewFormIsSeenButNotSignedBySomeone(){
        Customer patient = new Customer();
        patient.setFirstName("Raul");

        Form form = new Form();
        form.setDescription("Something not signed");
        form.setTitle("Don't Sign!");

        FormSeenByCustomer seenForm = new FormSeenByCustomer();
        seenForm.setCustomer(patient);
        seenForm.setForm(form);
        LocalDateTime now = LocalDateTime.now();
        seenForm.setTimestamp(now);
        formSeenByCustomerRepository.save(seenForm);

        FormSeenByCustomer retrievedForm = formSeenByCustomerRepository.findById(seenForm.getSeenById()).orElse(null);

        assertNotNull(retrievedForm);
        assertEquals(retrievedForm.getCustomer(), patient);
        assertEquals(retrievedForm.getTimestamp(), now);
        assertEquals(retrievedForm.getForm(), form);
    }
}
