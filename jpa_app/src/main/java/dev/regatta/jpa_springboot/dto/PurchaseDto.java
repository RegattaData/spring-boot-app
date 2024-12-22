package dev.regatta.jpa_springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PurchaseDto {
    private Long purchaseId;
    private Long customerId;
    private String item;
    private BigDecimal price;
    private LocalDate purchaseDate;
}
