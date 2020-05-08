package com.gdula.vote.service;

import com.gdula.vote.model.User;
import com.gdula.vote.model.Variant;
import com.gdula.vote.service.dto.CreateUpdateVariantDto;
import com.gdula.vote.service.dto.VariantDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VariantDtoMapper {
    public VariantDto toDto(Variant variant) {
        return new VariantDto(variant.getId(), variant.getVariant(), variant.getQuestion());
    }

    public Variant toModel(CreateUpdateVariantDto dto) {
        String randomId = UUID.randomUUID().toString();

        return  new Variant(randomId, dto.getVariant(), dto.getQuestion());
    }
}
