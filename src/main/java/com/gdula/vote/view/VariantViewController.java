package com.gdula.vote.view;

import com.gdula.vote.model.Variant;
import com.gdula.vote.repository.VariantRepository;
import com.gdula.vote.service.exception.VariantNotFound;
import com.gdula.vote.service.mapper.QuestionDtoMapper;
import com.gdula.vote.service.QuestionService;
import com.gdula.vote.service.mapper.VariantDtoMapper;
import com.gdula.vote.service.VariantService;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.CreateUpdateVariantDto;
import com.gdula.vote.service.dto.QuestionDto;
import com.gdula.vote.service.dto.VariantDto;
import com.gdula.vote.service.exception.QuestionNotFound;
import com.gdula.vote.service.exception.VariantDataInvalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class VariantViewController {
    @Autowired
    private VariantService variantService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionDtoMapper questionDtoMapper;

    @Autowired
    private VariantDtoMapper variantDtoMapper;

    @Autowired
    private VariantRepository variantRepository;

    @GetMapping("/variants")
    public ModelAndView displayVariantTable() {
        ModelAndView mav = new ModelAndView("variants-table");

        List<VariantDto> allVariants = variantService.getAllVariants();
        mav.addObject("variants", allVariants);

        return mav;
    }

    @GetMapping("/create-variant")
    public String displayCreateVariantForm(Model model) {
        CreateUpdateVariantDto dto = new CreateUpdateVariantDto();
        model.addAttribute("dto", dto);

        return "create-variant-form";
    }

    @PostMapping("/create-variant")
    public String createVariant (@Valid @ModelAttribute(name = "dto") CreateUpdateVariantDto dto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("variants", variantService.getAllVariants());
            return "create-variant-form";
        }
        try {
            variantService.createVariant(dto);
        } catch (VariantDataInvalid e) {
            e.printStackTrace();
            model.addAttribute("dto", dto);
            model.addAttribute("variants", variantService.getAllVariants());

            return "create-variant-form";
        }

        return "redirect:/variants";
    }

    @GetMapping("/add-variant/{id}")
    public ModelAndView displayAddVariantForm(@PathVariable String id, @ModelAttribute CreateUpdateVariantDto dto) {
        try {
            QuestionDto questionById = questionService.getQuestionById(id);

            CreateUpdateQuestionDto updateQuestionDto = questionDtoMapper.toUpdateDto(questionById);
            ModelAndView mav = new ModelAndView("add-variant-form");
            mav.addObject("dto",dto);
            mav.addObject("id",id);
            return mav;

        } catch (QuestionNotFound e) {
            return new ModelAndView("redirect:/questions");

        }
    }

    @PostMapping("/add-variant/{id}")
    public String addVariant(@ModelAttribute CreateUpdateVariantDto dto, @PathVariable String id) {
        try {
            questionService.addQuestionVariant(dto, id);
            return "redirect:/questions";
        } catch (QuestionNotFound | VariantDataInvalid e) {
            return "redirect:/add-variant/" + id;
        }
    }

    @PostMapping("/variant/increment/{id}/{ids}")
    public String incrementVariant(@PathVariable String id, @PathVariable String ids) {
        try {
            variantService.incrementVariant(id);
            return "redirect:/complete-survey/" + ids;
        } catch (VariantNotFound e) {
            return "redirect:/variant/increment/" + id;
        }
    }

    @GetMapping("/variant/increment/{id}/{ids}")
    public String displaySurvey(@PathVariable String id, @PathVariable String ids) {
        try {
            variantService.incrementVariant(id);
            return "redirect:/complete-survey/" + ids;
        } catch (VariantNotFound e) {
            return "redirect:/variant/increment/" + id;
        }
    }

}
