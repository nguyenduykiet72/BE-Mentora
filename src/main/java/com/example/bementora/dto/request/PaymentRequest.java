package com.example.bementora.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentRequest implements Serializable {
    private String status;
    private String message;
    private String URL;
}
