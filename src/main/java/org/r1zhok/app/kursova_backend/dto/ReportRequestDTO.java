package org.r1zhok.app.kursova_backend.dto;

import java.time.LocalDateTime;

public class ReportRequestDTO {
    private String reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}