package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.InvoiceEntity;
import com.multicar.repository.demo.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String> {
    
    Optional<InvoiceEntity> findByInvoiceId(String invoiceId);
    
    List<InvoiceEntity> findByJobId(String jobId);
    
    List<InvoiceEntity> findByPaymentStatus(PaymentStatus paymentStatus);
}








