package com.gdula.vote.view;

import com.gdula.vote.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VariantViewController {
    @Autowired
    private VariantService variantService;


}
