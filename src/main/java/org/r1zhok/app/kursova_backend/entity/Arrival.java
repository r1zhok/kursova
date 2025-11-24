package org.r1zhok.app.kursova_backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}

