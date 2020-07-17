package com.gdula.vote.service;

import com.gdula.vote.model.Variant;
import com.gdula.vote.repository.VariantRepository;
import com.gdula.vote.service.dto.CreateUpdateVariantDto;
import com.gdula.vote.service.dto.VariantDto;
import com.gdula.vote.service.exception.VariantDataInvalid;
import com.gdula.vote.service.exception.VariantNotFound;
import com.gdula.vote.service.mapper.VariantDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.stream.Collectors;

/**
 * class: VariantService
 * Reprezentuje serwis wariantu.
 */

@Service
public class VariantService {
    private VariantRepository variantRepository;
    private VariantDtoMapper mapper;

    @Autowired
    public VariantService(VariantRepository variantRepository, VariantDtoMapper mapper) {
        this.variantRepository = variantRepository;
        this.mapper = mapper;
    }

    /**
     * method: getAllVariants
     * Zwraca wszystkie warianty
     */
    public List<VariantDto> getAllVariants() {
        return variantRepository.findAll()
                .stream()
                .map(q -> mapper.toDto(q))
                .collect(Collectors.toList());
    }

    /**
     * method: getVariantById
     * Zwraca wariant po ID
     */
    public VariantDto getVariantById(String id) throws VariantNotFound {
        return variantRepository.findById(id)
                .map(q -> mapper.toDto(q)).orElseThrow(() -> new VariantNotFound());
    }

    /**
     * method: deleteVariantById
     * Usuwa wariant po ID
     */
    public VariantDto deleteVariantById(String id) throws VariantNotFound {
        Variant variant = variantRepository.findById(id).orElseThrow(() -> new VariantNotFound());
        variantRepository.delete(variant);
        return mapper.toDto(variant);
    }

    /**
     * method: createVariant
     * Tworzy wariant
     */
    public VariantDto createVariant(CreateUpdateVariantDto dto) throws VariantDataInvalid {
        if(dto.getVariant().isEmpty()) {
            throw new VariantDataInvalid();
        }
        Variant variantToSave = mapper.toModel(dto);
        Variant savedVariant = variantRepository.save(variantToSave);

        return mapper.toDto(savedVariant);
    }

    /**
     * method: updateVariant
     * Aktualizuje wariant
     */
    public VariantDto updateVariant(CreateUpdateVariantDto dto, String id) throws VariantNotFound {
        Variant variant = variantRepository.findById(id).orElseThrow(() -> new VariantNotFound());

        variant.setQuestion(dto.getQuestion());
        variant.setVariant(dto.getVariant());

        return mapper.toDto(variant);
    }

    /**
     * method: incrementVariant
     * Zwiększa wartość variantu
     */
    public VariantDto incrementVariant(String id) throws VariantNotFound {
        Variant variant = variantRepository.findById(id).orElseThrow(() -> new VariantNotFound());

        if (variant.getVoteAmount() == null) {
            variant.setVoteAmount(0);
        }

        variant.setVoteAmount(variant.getVoteAmount()+1);
        Variant savedVariant = variantRepository.save(variant);

        return mapper.toDto(savedVariant);
    }

}
