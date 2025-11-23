package org.r1zhok.app.kursova_backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ARRIVAL")
public class Arrival extends ProductMovement {
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Override
    public void execute() {
        getProduct().updateQuantity(getQuantity());
    }

    @Override
    public MovementType getMovementType() {
        return MovementType.ARRIVAL;
    }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}

