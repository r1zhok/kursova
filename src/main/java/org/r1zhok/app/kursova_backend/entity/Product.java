package org.r1zhok.app.kursova_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String article;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity = 10;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductMovement> movements = new ArrayList<>();

    public Product() {}

    public Product(String name, String article, BigDecimal price, Supplier supplier) {
        this.name = name;
        this.article = article;
        this.price = price;
        this.supplier = supplier;
    }

    // Business methods
    public void updateQuantity(Integer delta) {
        this.quantity += delta;
        if (this.quantity < 0) {
            throw new IllegalStateException("Недостатньо товару на складі");
        }
    }

    public boolean isLowStock() {
        return this.quantity <= this.minQuantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public List<ProductMovement> getMovements() { return movements; }
    public void setMovements(List<ProductMovement> movements) { this.movements = movements; }
}