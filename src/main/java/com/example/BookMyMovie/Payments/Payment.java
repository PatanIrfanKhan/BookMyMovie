package com.example.BookMyMovie.Payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {

    @Id
    private String orderId;   // PayPal's Order ID

    private String captureId; // PayPal capture ID
    private String status;    // CREATED / APPROVED / COMPLETED / FAILED
    private String currency;
    private String amount;
    private String payerEmail;

    private Instant createdAt;
    private Instant updatedAt;

    public void setCreated() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setUpdated() {
        this.updatedAt = Instant.now();
    }
}
