package dev.regatta.jpa_springboot.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "customers")
@ToString(exclude = "purchases")
public class Customer {

  @Id @Column(name = "customerid") private Long customerId;

  @Column(length = 100, nullable = false) private String name;

  @Column(length = 100, nullable = false) private String email;

  @Column(length = 15) private String phone;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,
             orphanRemoval = true)
  @JsonManagedReference
  private List<Purchase> purchases = new ArrayList<>();
}
