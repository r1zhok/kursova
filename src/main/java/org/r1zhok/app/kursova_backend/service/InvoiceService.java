package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.entity.*;
import org.r1zhok.app.kursova_backend.repository.ContractorRepository;
import org.r1zhok.app.kursova_backend.repository.InvoiceRepository;
import org.r1zhok.app.kursova_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InvoiceService extends DocumentProcessor<Invoice> {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovementService movementService;

    /**
     * Створення нової накладної з використанням Template Method
     */
    public Invoice createInvoice(Long contractorId, InvoiceType type, Long userId) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Контрагент не знайдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдено"));

        Invoice invoice = new Invoice();
        invoice.setContractor(contractor);
        invoice.setType(type);
        invoice.setCreatedBy(user);

        // Використовуємо Template Method
        return processDocument(invoice);
    }

    /**
     * Додавання руху товару до накладної
     */
    public Invoice addMovementToInvoice(Long invoiceId, Long productId, Integer quantity, Long userId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Накладна не знайдена"));

        ProductMovement movement;

        if (invoice.getType() == InvoiceType.ARRIVAL) {
            movement = movementService.createArrival(productId, quantity, userId,
                    "Надходження за накладною " + invoice.getNumber());
            ((Arrival) movement).setInvoice(invoice);
        } else {
            movement = movementService.createShipment(productId, quantity, userId,
                    "Відвантаження за накладною " + invoice.getNumber());
            ((Shipment) movement).setInvoice(invoice);
        }

        invoice.getMovements().add(movement);
        invoice.calculateTotal();

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Накладна не знайдена"));
    }

    // ========== Template Method Implementation ==========

    @Override
    protected void validate(Invoice invoice) {
        if (invoice.getContractor() == null) {
            throw new IllegalArgumentException("Накладна повинна мати контрагента");
        }
        if (invoice.getType() == null) {
            throw new IllegalArgumentException("Тип накладної не вказано");
        }
        if (invoice.getCreatedBy() == null) {
            throw new IllegalArgumentException("Автор накладної не вказаний");
        }
    }

    @Override
    protected void setDocumentNumber(Invoice invoice, String number) {
        invoice.setNumber(number);
    }

    @Override
    protected void calculateTotals(Invoice invoice) {
        invoice.calculateTotal();
    }

    @Override
    protected Invoice saveDocument(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    protected String getDocumentPrefix() {
        return "INV";
    }

    @Override
    protected long getDocumentCount() {
        return invoiceRepository.count();
    }

    @Override
    protected void sendNotifications(Invoice invoice) {
        // Hook method - можна додати відправку email, SMS тощо
        System.out.println("📧 Накладна " + invoice.getNumber() + " створена для " +
                invoice.getContractor().getName());
    }
}
