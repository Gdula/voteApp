package com.gdula.vote.service;

import com.gdula.vote.model.Variant;
import com.gdula.vote.repository.VariantRepository;
import com.gdula.vote.service.dto.CreateUpdateVariantDto;
import com.gdula.vote.service.dto.VariantDto;
import com.gdula.vote.service.exception.VariantNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VariantService {
    private VariantRepository variantRepository;
    private VariantDtoMapper mapper;

    @Autowired
    public VariantService(VariantRepository variantRepository, VariantDtoMapper mapper) {
        this.variantRepository = variantRepository;
        this.mapper = mapper;
    }

    public List<VariantDto> getAllVariants() {
        return variantRepository.findAll()
                .stream()
                .map(q -> mapper.toDto(q))
                .collect(Collectors.toList());
    }

    public VariantDto getVariantById(String id) throws VariantNotFound {
        return variantRepository.findById(id)
                .map(q -> mapper.toDto(q)).orElseThrow(() -> new VariantNotFound());
    }

    public VariantDto deleteVariantById(String id) throws VariantNotFound {
        Variant variant = variantRepository.findById(id).orElseThrow(() -> new VariantNotFound());
        variantRepository.delete(variant);
        return mapper.toDto(variant);
    }

    public VariantDto createVariant(CreateUpdateVariantDto dto) {
        Variant variantToSave = mapper.toModel(dto);
        Variant savedVariant = variantRepository.save(variantToSave);

        return mapper.toDto(savedVariant);
    }

    public VariantDto updateVariant(CreateUpdateVariantDto dto, String id) throws VariantNotFound {
        Variant variant = variantRepository.findById(id).orElseThrow(() -> new VariantNotFound());

        variant.setQuestion(dto.getQuestion());
        variant.setVariant(dto.getVariant());

        return mapper.toDto(variant);
    }
}
