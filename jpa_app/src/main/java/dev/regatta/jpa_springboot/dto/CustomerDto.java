package dev.regatta.jpa_springboot.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private Long customerId;
    private String name;
    private String email;
    private String phone;
}
