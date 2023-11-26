package com.example.server.repository;

import com.example.server.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveAndRetrieveCustomer(){
        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName("customer1");
        savedCustomer.setAccountStatus(Customer.AccountStatus.ACTIVE);
        customerRepository.save(savedCustomer);
        Customer retrievedCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        assertNotNull(retrievedCustomer);
        assertEquals("customer1", retrievedCustomer.getFirstName());
        assertEquals(Customer.AccountStatus.ACTIVE, retrievedCustomer.getAccountStatus());
    }
}
