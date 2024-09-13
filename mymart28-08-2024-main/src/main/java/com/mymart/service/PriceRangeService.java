package com.mymart.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mymart.model.PriceRange;
import com.mymart.repository.PriceRangeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PriceRangeService {

    private final PriceRangeRepository priceRangeRepository;

    @Autowired
    public PriceRangeService(PriceRangeRepository priceRangeRepository) {
        this.priceRangeRepository = priceRangeRepository;
    }

    public List<Map<String, String>> getPriceRanges() {
        List<PriceRange> priceRanges = priceRangeRepository.findAll();
        return priceRanges.stream()
            .map(priceRange -> Map.of(
                "value", priceRange.getValue(),
                "label", priceRange.getLabel()
            ))
            .collect(Collectors.toList());
    }
    
    public List<PriceRange> getAllPriceRanges() {
        return priceRangeRepository.findAll();
    }

    public void savePriceRange(PriceRange priceRange) {
        priceRangeRepository.save(priceRange);
    }

   

    public PriceRange getPriceRangeById(Long id) {
        return priceRangeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("PriceRange not found"));
    }

    public void deletePriceRange(long id) {
        priceRangeRepository.deleteById(id);
    }
}
