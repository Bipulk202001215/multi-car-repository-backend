package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.InvoiceEntity;
import com.multicar.repository.demo.entity.JobCardEntity;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
import com.multicar.repository.demo.model.CreateInvoiceRequest;
import com.multicar.repository.demo.model.Invoice;
import com.multicar.repository.demo.repository.InvoiceRepository;
import com.multicar.repository.demo.repository.JobCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final JobCardRepository jobCardRepository;

    public Invoice createInvoice(CreateInvoiceRequest request) {
        // Calculate total = subtotal + cgst + sgst + igst
        BigDecimal cgst = request.getCgst() != null ? request.getCgst() : BigDecimal.ZERO;
        BigDecimal sgst = request.getSgst() != null ? request.getSgst() : BigDecimal.ZERO;
        BigDecimal igst = request.getIgst() != null ? request.getIgst() : BigDecimal.ZERO;
        BigDecimal total = request.getSubtotal()
                .add(cgst)
                .add(sgst)
                .add(igst);

        InvoiceEntity entity = InvoiceEntity.builder()
                .jobId(request.getJobId())
                .subtotal(request.getSubtotal())
                .cgst(cgst)
                .sgst(sgst)
                .igst(igst)
                .total(total)
                .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : PaymentStatus.PENDING)
                .paymentMode(request.getPaymentMode())
                .build();

        InvoiceEntity savedEntity = invoiceRepository.save(entity);

        // Update JobCardEntity.invoice_id with created invoice ID
        Optional<JobCardEntity> jobCard = jobCardRepository.findByJobCardId(request.getJobId());
        if (jobCard.isPresent()) {
            jobCard.get().setInvoiceId(savedEntity.getInvoiceId());
            jobCardRepository.save(jobCard.get());
        }

        return convertToModel(savedEntity);
    }

    public Optional<Invoice> getInvoiceById(String invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(this::convertToModel);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Invoice> updateInvoice(String invoiceId, CreateInvoiceRequest request) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(existingEntity -> {
                    existingEntity.setJobId(request.getJobId());
                    existingEntity.setSubtotal(request.getSubtotal());
                    
                    BigDecimal cgst = request.getCgst() != null ? request.getCgst() : BigDecimal.ZERO;
                    BigDecimal sgst = request.getSgst() != null ? request.getSgst() : BigDecimal.ZERO;
                    BigDecimal igst = request.getIgst() != null ? request.getIgst() : BigDecimal.ZERO;
                    
                    existingEntity.setCgst(cgst);
                    existingEntity.setSgst(sgst);
                    existingEntity.setIgst(igst);
                    
                    // Recalculate total
                    BigDecimal total = request.getSubtotal()
                            .add(cgst)
                            .add(sgst)
                            .add(igst);
                    existingEntity.setTotal(total);
                    
                    if (request.getPaymentStatus() != null) {
                        existingEntity.setPaymentStatus(request.getPaymentStatus());
                    }
                    if (request.getPaymentMode() != null) {
                        existingEntity.setPaymentMode(request.getPaymentMode());
                    }
                    
                    InvoiceEntity updatedEntity = invoiceRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteInvoice(String invoiceId) {
        Optional<InvoiceEntity> entity = invoiceRepository.findByInvoiceId(invoiceId);
        if (entity.isPresent()) {
            // Remove invoice_id from job card if exists
            Optional<JobCardEntity> jobCard = jobCardRepository.findByJobCardId(entity.get().getJobId());
            if (jobCard.isPresent() && invoiceId.equals(jobCard.get().getInvoiceId())) {
                jobCard.get().setInvoiceId(null);
                jobCardRepository.save(jobCard.get());
            }
            invoiceRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public List<Invoice> getInvoicesByJobId(String jobId) {
        return invoiceRepository.findByJobId(jobId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoicesByPaymentStatus(PaymentStatus paymentStatus) {
        return invoiceRepository.findByPaymentStatus(paymentStatus).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Invoice> updatePaymentStatus(String invoiceId, PaymentStatus paymentStatus, PaymentMode paymentMode) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(existingEntity -> {
                    existingEntity.setPaymentStatus(paymentStatus);
                    if (paymentMode != null) {
                        existingEntity.setPaymentMode(paymentMode);
                    }
                    InvoiceEntity updatedEntity = invoiceRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    private Invoice convertToModel(InvoiceEntity entity) {
        return Invoice.builder()
                .invoiceId(entity.getInvoiceId())
                .jobId(entity.getJobId())
                .subtotal(entity.getSubtotal())
                .cgst(entity.getCgst())
                .sgst(entity.getSgst())
                .igst(entity.getIgst())
                .total(entity.getTotal())
                .paymentStatus(entity.getPaymentStatus())
                .paymentMode(entity.getPaymentMode())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}






