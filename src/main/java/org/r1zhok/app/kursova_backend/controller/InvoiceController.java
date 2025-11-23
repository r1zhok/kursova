package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.dto.ShipmentRequestDTO;
import org.r1zhok.app.kursova_backend.entity.Invoice;
import org.r1zhok.app.kursova_backend.service.InvoiceService;
import org.r1zhok.app.kursova_backend.service.WarehouseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private WarehouseFacade warehouseFacade;

    /**
     * Комплексне відвантаження з автоматичним створенням накладної
     * POST /api/invoices/process-shipment
     */
    @PostMapping("/process-shipment")
    public ResponseEntity<Invoice> processShipment(@RequestBody ShipmentRequestDTO request) {
        // Конвертуємо DTO в об'єкти Facade
        List<WarehouseFacade.ShipmentItem> items = request.getItems().stream()
                .map(dto -> new WarehouseFacade.ShipmentItem(dto.getProductId(), dto.getQuantity()))
                .collect(Collectors.toList());

        // Використовуємо Facade для комплексної операції
        Invoice invoice = warehouseFacade.processShipment(
                request.getContractorId(),
                items,
                request.getUserId()
        );

        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

    /**
     * Отримання всіх накладних
     * GET /api/invoices
     */
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    /**
     * Отримання накладної по ID
     * GET /api/invoices/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }
}
