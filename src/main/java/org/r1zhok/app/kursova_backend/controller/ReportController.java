package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.dto.ReportRequestDTO;
import org.r1zhok.app.kursova_backend.service.ReportBuilder;
import org.r1zhok.app.kursova_backend.service.WarehouseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportBuilder reportBuilder;

    @Autowired
    private WarehouseFacade warehouseFacade;

    /**
     * Генерація звіту стану складу
     * GET /api/reports/warehouse-status
     */
    @GetMapping("/warehouse-status")
    public ResponseEntity<ReportBuilder.WarehouseStatusReport> getWarehouseStatusReport() {
        // Builder Pattern
        ReportBuilder.Report report = reportBuilder
                .ofType(ReportBuilder.ReportType.WAREHOUSE_STATUS)
                .build();

        return ResponseEntity.ok((ReportBuilder.WarehouseStatusReport) report);
    }

    /**
     * Генерація звіту динаміки руху товарів
     * GET /api/reports/movement-dynamics?start=...&end=...
     */
    @GetMapping("/movement-dynamics")
    public ResponseEntity<ReportBuilder.MovementDynamicsReport> getMovementDynamicsReport(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        // Builder Pattern
        ReportBuilder.Report report = reportBuilder
                .ofType(ReportBuilder.ReportType.MOVEMENT_DYNAMICS)
                .forDateRange(start, end)
                .build();

        return ResponseEntity.ok((ReportBuilder.MovementDynamicsReport) report);
    }

    /**
     * Генерація фінансового звіту
     * GET /api/reports/financial-summary
     */
    @GetMapping("/financial-summary")
    public ResponseEntity<ReportBuilder.FinancialSummaryReport> getFinancialSummaryReport() {
        // Builder Pattern
        ReportBuilder.Report report = reportBuilder
                .ofType(ReportBuilder.ReportType.FINANCIAL_SUMMARY)
                .build();

        return ResponseEntity.ok((ReportBuilder.FinancialSummaryReport) report);
    }

    /**
     * Повний звіт складу (Facade Pattern)
     * GET /api/reports/full-warehouse
     */
    @GetMapping("/full-warehouse")
    public ResponseEntity<WarehouseFacade.WarehouseReport> getFullWarehouseReport() {
        // Facade Pattern
        WarehouseFacade.WarehouseReport report = warehouseFacade.getFullWarehouseReport();
        return ResponseEntity.ok(report);
    }

    /**
     * Універсальна генерація звіту
     * POST /api/reports/generate
     */
    @PostMapping("/generate")
    public ResponseEntity<ReportBuilder.Report> generateReport(@RequestBody ReportRequestDTO request) {
        ReportBuilder.ReportType type = ReportBuilder.ReportType.valueOf(request.getReportType());

        // Builder Pattern з ланцюгом викликів
        ReportBuilder builder = reportBuilder.ofType(type);

        if (request.getStartDate() != null && request.getEndDate() != null) {
            builder.forDateRange(request.getStartDate(), request.getEndDate());
        }

        ReportBuilder.Report report = builder.build();
        return ResponseEntity.ok(report);
    }
}
