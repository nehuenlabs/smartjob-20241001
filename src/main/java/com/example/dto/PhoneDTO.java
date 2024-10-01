package com.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDTO {
    private Long id;
    private String number;
    private String citycode;
    private String countrycode;
}