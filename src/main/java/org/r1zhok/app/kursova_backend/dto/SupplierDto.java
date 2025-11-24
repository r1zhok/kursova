package org.r1zhok.app.kursova_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.r1zhok.app.kursova_backend.entity.Supplier;

import java.io.Serializable;

@Data
@Builder
public class SupplierDto implements Serializable {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;

    @JsonIgnore
    public static SupplierDto from(Supplier supplier) {
        return SupplierDto.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .build();
    }

}
