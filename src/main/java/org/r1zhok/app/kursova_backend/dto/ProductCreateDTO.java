package org.r1zhok.app.kursova_backend.dto;

import java.math.BigDecimal;

public class ProductCreateDTO {
    private String name;
    private String article;
    private BigDecimal price;
    private Integer minQuantity;
    private Long supplierId;
    private Integer initialQuantity;

    public ProductCreateDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public Integer getInitialQuantity() { return initialQuantity; }
    public void setInitialQuantity(Integer initialQuantity) { this.initialQuantity = initialQuantity; }
}
