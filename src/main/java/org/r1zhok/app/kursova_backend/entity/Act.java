package org.r1zhok.app.kursova_backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "acts")
public class Act {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "act", cascade = CascadeType.ALL)
    private List<WriteOff> writeOffs = new ArrayList<>();

    public Act() {
        this.date = LocalDateTime.now();
    }

    public void calculateTotal() {
        this.totalAmount = writeOffs.stream()
                .map(w -> w.getProduct().getPrice().multiply(new BigDecimal(w.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<WriteOff> getWriteOffs() { return writeOffs; }
    public void setWriteOffs(List<WriteOff> writeOffs) { this.writeOffs = writeOffs; }
}
