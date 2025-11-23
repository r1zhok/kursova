package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.entity.Act;
import org.r1zhok.app.kursova_backend.entity.User;
import org.r1zhok.app.kursova_backend.entity.WriteOff;
import org.r1zhok.app.kursova_backend.repository.ActRepository;
import org.r1zhok.app.kursova_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ActService extends DocumentProcessor<Act> {

    @Autowired
    private ActRepository actRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovementService movementService;

    /**
     * Створення нового акту з використанням Template Method
     */
    public Act createAct(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдено"));

        Act act = new Act();
        act.setCreatedBy(user);

        // Використовуємо Template Method
        return processDocument(act);
    }

    /**
     * Додавання списання до акту
     */
    public Act addWriteOffToAct(Long actId, Long productId, Integer quantity,
                                Long userId, String reason) {
        Act act = actRepository.findById(actId)
                .orElseThrow(() -> new RuntimeException("Акт не знайдено"));

        WriteOff writeOff = movementService.createWriteOff(productId, quantity, userId, reason);
        writeOff.setAct(act);

        act.getWriteOffs().add(writeOff);
        act.calculateTotal();

        return actRepository.save(act);
    }

    public List<Act> getAllActs() {
        return actRepository.findAll();
    }

    public Act getActById(Long id) {
        return actRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Акт не знайдено"));
    }

    // ========== Template Method Implementation ==========

    @Override
    protected void validate(Act act) {
        if (act.getCreatedBy() == null) {
            throw new IllegalArgumentException("Автор акту не вказаний");
        }
    }

    @Override
    protected void setDocumentNumber(Act act, String number) {
        act.setNumber(number);
    }

    @Override
    protected void calculateTotals(Act act) {
        act.calculateTotal();
    }

    @Override
    protected Act saveDocument(Act act) {
        return actRepository.save(act);
    }

    @Override
    protected String getDocumentPrefix() {
        return "ACT";
    }

    @Override
    protected long getDocumentCount() {
        return actRepository.count();
    }

    @Override
    protected void sendNotifications(Act act) {
        // Hook method
        System.out.println("📋 Акт списання " + act.getNumber() + " створено. " +
                "Списано на суму: " + act.getTotalAmount());
    }
}
