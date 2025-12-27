package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
import com.multicar.repository.demo.model.CreateInvoiceRequest;
import com.multicar.repository.demo.model.Invoice;
import com.multicar.repository.demo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody CreateInvoiceRequest request) {
        Invoice createdInvoice = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable String invoiceId) {
        return invoiceService.getInvoiceById(invoiceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable String invoiceId,
            @RequestBody CreateInvoiceRequest request) {
        return invoiceService.updateInvoice(invoiceId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {
        boolean deleted = invoiceService.deleteInvoice(invoiceId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Invoice>> getInvoicesByJobId(@PathVariable String jobId) {
        List<Invoice> invoices = invoiceService.getInvoicesByJobId(jobId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<List<Invoice>> getInvoicesByPaymentStatus(@PathVariable PaymentStatus paymentStatus) {
        List<Invoice> invoices = invoiceService.getInvoicesByPaymentStatus(paymentStatus);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/{invoiceId}/payment")
    public ResponseEntity<Invoice> updatePaymentStatus(
            @PathVariable String invoiceId,
            @RequestParam PaymentStatus paymentStatus,
            @RequestParam(required = false) PaymentMode paymentMode) {
        return invoiceService.updatePaymentStatus(invoiceId, paymentStatus, paymentMode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}



