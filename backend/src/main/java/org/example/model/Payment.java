package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Payment {
    private long id;
    private long user_id;
    private double amount;
    private OffsetDateTime date;
    private String type;
}
