package com.example.server.repository;

import com.example.server.repository.entity.FormEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormEventRepository extends JpaRepository<FormEvent, Long> {
    @Query("SELECT fe FROM FormEvent fe WHERE fe.form.formId = :formId AND fe.formEventType = :formEventType")
    List<FormEvent> findByFormIdAndFormEventType(Long formId, FormEvent.FormEventType formEventType);

    @Query("SELECT fe FROM FormEvent fe WHERE fe.form.formId = :formId AND fe.customer.id = :customerId AND fe.formEventType = :formEventType")
    Optional<FormEvent> findByFormIdAndCustomerIdAndFormEventType(Long formId, Long customerId, FormEvent.FormEventType formEventType);

    List<FormEvent> findByCustomerId(Long customerId);

    @Query("SELECT fe FROM FormEvent fe WHERE fe.form.formId = :formId AND fe.customer.id = :customerId")
    FormEvent findByFormIdAndCustomerId(Long formId, Long customerId);

}
