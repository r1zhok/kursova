package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.dto.ProductCreateDTO;
import org.r1zhok.app.kursova_backend.dto.ProductDto;
import org.r1zhok.app.kursova_backend.entity.Product;
import org.r1zhok.app.kursova_backend.entity.Supplier;
import org.r1zhok.app.kursova_backend.repository.SupplierRepository;
import org.r1zhok.app.kursova_backend.service.ProductService;
import org.r1zhok.app.kursova_backend.service.WarehouseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseFacade warehouseFacade;

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Створення нового товару з початковим надходженням
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Постачальник не знайдено"));

        Product product = new Product(dto.getName(), dto.getArticle(), dto.getPrice(), supplier);
        product.setMinQuantity(dto.getMinQuantity() != null ? dto.getMinQuantity() : 10);

        var created = ProductDto.from(warehouseFacade.registerProductWithInitialStock(
                product,
                dto.getInitialQuantity() != null ? dto.getInitialQuantity() : 0,
                1L
        ));

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Отримання всіх товарів
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        var products = productService.getAllProducts().stream().map(ProductDto::from).toList();
        return ResponseEntity.ok(products);
    }

    /**
     * Отримання товару по ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = ProductDto.from(productService.getProductById(id));
        return ResponseEntity.ok(product);
    }

    /**
     * Оновлення товару
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {
        var updated = ProductDto.from(productService.updateProduct(id, productDetails));
        return ResponseEntity.ok(updated);
    }

    /**
     * Видалення товару
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Отримання товарів з низьким запасом
     * GET /api/products/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto>> getLowStockProducts() {
        List<ProductDto> products = productService.getLowStockProducts().stream().map(ProductDto::from).toList();
        return ResponseEntity.ok(products);
    }

    /**
     * Пошук товару по артикулу
     * GET /api/products/search?article=XXX
     */
    @GetMapping("/search")
    public ResponseEntity<ProductDto> searchByArticle(@RequestParam String article) {
        var product = ProductDto.from(productService.findByArticle(article));
        return ResponseEntity.ok(product);
    }
}