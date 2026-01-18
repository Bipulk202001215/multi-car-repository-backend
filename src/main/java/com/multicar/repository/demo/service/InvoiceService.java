package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.InvoiceEntity;
import com.multicar.repository.demo.entity.JobCardEntity;
import com.multicar.repository.demo.enums.InventoryEvent;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.CreateInvoiceRequest;
import com.multicar.repository.demo.model.Invoice;
import com.multicar.repository.demo.model.InvoiceItem;
import com.multicar.repository.demo.model.Partcode;
import com.multicar.repository.demo.model.SellInventoryRequest;
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
    private final PartcodeService partcodeService;
    private final InventoryEventService inventoryEventService;

    public Invoice createInvoice(CreateInvoiceRequest request) {
        // Create SELL events for each item in the list
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (InvoiceItem item : request.getItems()) {
                SellInventoryRequest sellRequest = SellInventoryRequest.builder()
                        .partCode(item.getPartCode())
                        .units(item.getUnits())
                        .companyId(request.getCompanyId())
                        .jobId(request.getJobId())
                        .build();
                
                // Create SELL event (this will also update partcode units)
                partcodeService.sellUnits(sellRequest);
            }
        }
        
        // Calculate subtotal: sum of (unitsPrice * units) for each item
        BigDecimal subtotal = BigDecimal.ZERO;
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (InvoiceItem item : request.getItems()) {
                Partcode partcode = partcodeService.getPartcodeByPartCode(item.getPartCode())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Partcode not found with part code: " + item.getPartCode(),
                                ErrorCode.PARTCODE_NOT_FOUND));
                
                BigDecimal itemPrice = partcode.getUnitsPrice().multiply(BigDecimal.valueOf(item.getUnits()));
                subtotal = subtotal.add(itemPrice);
            }
        }

        // Calculate total = subtotal + cgst + sgst + igst
        BigDecimal cgst = BigDecimal.valueOf(9);
        BigDecimal sgst = BigDecimal.valueOf(9);
        BigDecimal igst = BigDecimal.valueOf(9);
        BigDecimal total = subtotal.add(cgst).add(sgst).add(igst);

        InvoiceEntity entity = InvoiceEntity.builder()
                .jobId(request.getJobId())
                .companyId(request.getCompanyId())
                .subtotal(subtotal)
                .total(total)
                .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : PaymentStatus.PENDING)
                .paymentMode(request.getPaymentMode())
                .additionalDetails(request.getAdditionalDetails())
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
    public Optional<Invoice> getFullInvoiceById(String invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(this::convertToFullModel);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::convertToModelWithoutItems)
                .collect(Collectors.toList());
    }

    public Optional<Invoice> updateInvoice(String invoiceId, CreateInvoiceRequest request) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(existingEntity -> {
                    existingEntity.setJobId(request.getJobId());
                    if (request.getCompanyId() != null) {
                        existingEntity.setCompanyId(request.getCompanyId());
                    }
                    
                    // Calculate subtotal: sum of (unitsPrice * units) for each item
                    BigDecimal subtotal = BigDecimal.ZERO;
                    if (request.getItems() != null && !request.getItems().isEmpty()) {
                        for (InvoiceItem item : request.getItems()) {
                            Partcode partcode = partcodeService.getPartcodeByPartCode(item.getPartCode())
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            "Partcode not found with part code: " + item.getPartCode(),
                                            ErrorCode.PARTCODE_NOT_FOUND));
                            
                            BigDecimal itemPrice = partcode.getUnitsPrice().multiply(BigDecimal.valueOf(item.getUnits()));
                            subtotal = subtotal.add(itemPrice);
                        }
                    }
                    existingEntity.setSubtotal(subtotal);
                    
                    // Fixed values for GST
                    BigDecimal cgst = BigDecimal.valueOf(9);
                    BigDecimal sgst = BigDecimal.valueOf(9);
                    BigDecimal igst = BigDecimal.valueOf(9);
                    
                    // Recalculate total
                    BigDecimal total = subtotal.add(cgst).add(sgst).add(igst);
                    existingEntity.setTotal(total);
                    
                    if (request.getPaymentStatus() != null) {
                        existingEntity.setPaymentStatus(request.getPaymentStatus());
                    }
                    if (request.getPaymentMode() != null) {
                        existingEntity.setPaymentMode(request.getPaymentMode());
                    }
                    if (request.getAdditionalDetails() != null) {
                        existingEntity.setAdditionalDetails(request.getAdditionalDetails());
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
                .map(this::convertToModelWithoutItems)
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoicesByPaymentStatus(PaymentStatus paymentStatus) {
        return invoiceRepository.findByPaymentStatus(paymentStatus).stream()
                .map(this::convertToModelWithoutItems)
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

    private Invoice convertToModelWithoutItems(InvoiceEntity entity) {
        // Fixed values for GST
        BigDecimal cgst = BigDecimal.valueOf(9);
        BigDecimal sgst = BigDecimal.valueOf(9);
        BigDecimal igst = BigDecimal.valueOf(9);
        
        return Invoice.builder()
                .invoiceId(entity.getInvoiceId())
                .jobId(entity.getJobId())
                .companyId(entity.getCompanyId())
                .subtotal(entity.getSubtotal())
                .cgst(cgst)
                .sgst(sgst)
                .igst(igst)
                .total(entity.getTotal())
                .paymentStatus(entity.getPaymentStatus())
                .paymentMode(entity.getPaymentMode())
                .items(null) // Items not fetched for list views
                .additionalDetails(entity.getAdditionalDetails())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }

    private Invoice convertToModel(InvoiceEntity entity) {
        // Fetch SELL events for this jobId and convert to items
        List<InvoiceItem> items = inventoryEventService.getEventsByJobId(entity.getJobId()).stream()
                .filter(event -> event.getEvent() == InventoryEvent.SELL) // Only SELL events
                .map(event -> InvoiceItem.builder()
                        .partCode(event.getPartCode())
                        .units(event.getUnits())
                        .build())
                .collect(Collectors.toList());
        
        // Fixed values for GST
        BigDecimal cgst = BigDecimal.valueOf(9);
        BigDecimal sgst = BigDecimal.valueOf(9);
        BigDecimal igst = BigDecimal.valueOf(9);
        
        return Invoice.builder()
                .invoiceId(entity.getInvoiceId())
                .jobId(entity.getJobId())
                .companyId(entity.getCompanyId())
                .subtotal(entity.getSubtotal())
                .cgst(cgst)
                .sgst(sgst)
                .igst(igst)
                .total(entity.getTotal())
                .paymentStatus(entity.getPaymentStatus())
                .paymentMode(entity.getPaymentMode())
                .items(items)
                .additionalDetails(entity.getAdditionalDetails())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
    private Invoice convertToFullModel(InvoiceEntity entity) {
        // Fetch SELL events for this jobId and convert to items
        List<InvoiceItem> items = inventoryEventService.getEventsByJobId(entity.getJobId()).stream()
                .filter(event -> event.getEvent() == InventoryEvent.SELL) // Only SELL events
                .map(event -> {
                    // Get unitsPrice from event (it's stored in the event)
                    BigDecimal unitsPrice = event.getUnitsPrice();
                    
                    // Calculate totalPrice = unitsPrice * units
                    BigDecimal totalPrice = unitsPrice.multiply(BigDecimal.valueOf(event.getUnits()));
                    
                    // Use partCode as partDescription (since there's no description field in PartcodeEntity)
                    String partDescription = event.getPartCode();

                    BigDecimal discountPercentage= event.getDiscount() != null ? event.getDiscount() : BigDecimal.ZERO;

                    BigDecimal discountAmount = totalPrice.subtract(totalPrice.multiply(discountPercentage).divide(BigDecimal.valueOf(100)));

                    return InvoiceItem.builder()
                            .partCode(event.getPartCode())
                            .units(event.getUnits())
                            .partDescription(partDescription)
                            .unitsPrice(unitsPrice)
                            .totalPrice(totalPrice)
                            .discountPercentage(discountPercentage)
                            .discountedPrice(discountAmount)
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal totalAmount = items.stream()
                .map(InvoiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // Fixed values for GST
        BigDecimal totalDiscountedPrice = items.stream()
                .map(InvoiceItem::getDiscountedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cgst = totalDiscountedPrice.multiply(BigDecimal.valueOf(9)).divide(BigDecimal.valueOf(100));
        BigDecimal sgst = cgst;


        return Invoice.builder()
                .invoiceId(entity.getInvoiceId())
                .jobId(entity.getJobId())
                .companyId(entity.getCompanyId())
                .subtotal(entity.getSubtotal())
                .cgst(cgst)
                .sgst(sgst)
                .total(totalAmount)
                .paymentStatus(entity.getPaymentStatus())
                .paymentMode(entity.getPaymentMode())
                .items(items)
                .netCalculationAmount(totalDiscountedPrice.add(cgst).add(sgst))
                .additionalDetails(entity.getAdditionalDetails())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}







