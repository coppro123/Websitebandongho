package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String customerAddress;

    private String phoneNumber;
    private String customerEmail;
    private String note;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}

