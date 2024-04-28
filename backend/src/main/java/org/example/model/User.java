package org.example.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private double balance;
}
