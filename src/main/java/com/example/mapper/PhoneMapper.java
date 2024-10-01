package com.example.mapper;

import com.example.dto.PhoneDTO;
import com.example.domain.model.Phone;

public class PhoneMapper {

    public static PhoneDTO toDto(Phone phone) {
        if (phone == null) {
            return null;
        }

        return PhoneDTO.builder()
                .id(phone.getId())
                .number(phone.getNumber())
                .citycode(phone.getCitycode())
                .countrycode(phone.getCountrycode())
                .build();
    }

    public static Phone toEntity(PhoneDTO phoneDTO) {
        if (phoneDTO == null) {
            return null;
        }

        return Phone.builder()
                .id(phoneDTO.getId())
                .number(phoneDTO.getNumber())
                .citycode(phoneDTO.getCitycode())
                .countrycode(phoneDTO.getCountrycode())
                .build();
    }
}
