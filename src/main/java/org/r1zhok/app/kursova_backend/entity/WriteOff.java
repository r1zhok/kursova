package org.r1zhok.app.kursova_backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("WRITE_OFF")
public class WriteOff extends ProductMovement {
    @ManyToOne
    @JoinColumn(name = "act_id")
    private Act act;

    private String reason;

    @Override
    public void execute() {
        getProduct().updateQuantity(-getQuantity());
    }

    @Override
    public MovementType getMovementType() {
        return MovementType.WRITE_OFF;
    }

    public Act getAct() { return act; }
    public void setAct(Act act) { this.act = act; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
