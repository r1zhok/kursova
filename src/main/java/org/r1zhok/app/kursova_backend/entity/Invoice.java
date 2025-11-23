package org.r1zhok.app.kursova_backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceType type;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<ProductMovement> movements = new ArrayList<>();

    public Invoice() {
        this.date = LocalDateTime.now();
    }

    public void calculateTotal() {
        this.totalAmount = movements.stream()
                .map(m -> m.getProduct().getPrice().multiply(new BigDecimal(m.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addMovement(ProductMovement movement) {
        movements.add(movement);
        movement.setInvoice(this);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Contractor getContractor() { return contractor; }
    public void setContractor(Contractor contractor) { this.contractor = contractor; }

    public InvoiceType getType() { return type; }
    public void setType(InvoiceType type) { this.type = type; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<ProductMovement> getMovements() { return movements; }
    public void setMovements(List<ProductMovement> movements) { this.movements = movements; }
}

