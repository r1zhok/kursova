package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.entity.Product;
import org.r1zhok.app.kursova_backend.entity.Supplier;
import org.r1zhok.app.kursova_backend.exception.ResourceNotFoundException;
import org.r1zhok.app.kursova_backend.repository.ProductRepository;
import org.r1zhok.app.kursova_backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StockSubject stockSubject;

    /**
     * Створення нового товару
     */
    public Product createProduct(Product product) {
        validateProduct(product);
        Product saved = productRepository.save(product);

        // Observer Pattern - перевірка низького запасу при створенні
        if (saved.isLowStock()) {
            stockSubject.notifyObservers(saved, 0, saved.getQuantity());
        }

        return saved;
    }

    /**
     * Отримання товару по ID
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар з ID " + id + " не знайдено"));
    }

    /**
     * Отримання всіх товарів
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Оновлення товару
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        int oldQuantity = product.getQuantity();

        product.setName(productDetails.getName());
        product.setArticle(productDetails.getArticle());
        product.setPrice(productDetails.getPrice());
        product.setMinQuantity(productDetails.getMinQuantity());

        if (productDetails.getSupplier() != null) {
            Supplier supplier = supplierRepository.findById(productDetails.getSupplier().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Постачальник не знайдено"));
            product.setSupplier(supplier);
        }

        Product updated = productRepository.save(product);

        // Observer Pattern - повідомлення про зміну
        if (oldQuantity != updated.getQuantity()) {
            stockSubject.notifyObservers(updated, oldQuantity, updated.getQuantity());
        }

        return updated;
    }

    /**
     * Видалення товару
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    /**
     * Отримання товарів з низьким запасом
     */
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    /**
     * Пошук товару по артикулу
     */
    public Product findByArticle(String article) {
        return productRepository.findByArticle(article)
                .orElseThrow(() -> new ResourceNotFoundException("Товар з артикулом " + article + " не знайдено"));
    }

    /**
     * Отримання товарів постачальника
     */
    public List<Product> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplier_Id(supplierId);
    }

    /**
     * Валідація товару
     */
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою");
        }

        if (product.getArticle() == null || product.getArticle().trim().isEmpty()) {
            throw new IllegalArgumentException("Артикул товару не може бути порожнім");
        }

        if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Ціна товару повинна бути більше 0");
        }

        if (product.getSupplier() == null) {
            throw new IllegalArgumentException("Товар повинен мати постачальника");
        }
    }
}
