package org.r1zhok.app.kursova_backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DocumentProcessor<T> {

    public final T processDocument(T document) {
        validate(document);
        String number = generateNumber();
        setDocumentNumber(document, number);
        calculateTotals(document);
        T saved = saveDocument(document);
        sendNotifications(saved);
        return saved;
    }

    protected abstract void validate(T document);
    protected abstract void setDocumentNumber(T document, String number);
    protected abstract void calculateTotals(T document);
    protected abstract T saveDocument(T document);

    protected void sendNotifications(T document) {
    }

    protected String generateNumber() {
        String prefix = getDocumentPrefix();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = getDocumentCount() + 1;
        return String.format("%s-%s-%04d", prefix, date, count);
    }

    protected abstract String getDocumentPrefix();
    protected abstract long getDocumentCount();
}
