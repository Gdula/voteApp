package com.gdula.vote.view;

import com.gdula.vote.repository.SurveyRepository;
import com.gdula.vote.service.SurveyService;
import com.gdula.vote.service.dto.CreateUpdateSurveyDto;
import com.gdula.vote.service.dto.SurveyDto;
import com.gdula.vote.service.exception.SurveyDataInvalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SurveyViewController {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyService surveyService;

    @GetMapping("/surveys")
    public ModelAndView displaySurveyTable() {
        ModelAndView mav = new ModelAndView("surveys-table");

        List<SurveyDto> allSurveys = surveyService.getAllSurveys();
        mav.addObject("surveys", allSurveys);

        return mav;
    }

    @GetMapping("/create-survey")
    public String displayCreateSurveyForm(Model model) {
        CreateUpdateSurveyDto dto = new CreateUpdateSurveyDto();
        model.addAttribute("dto", dto);

        return "create-survey-form";
    }

    @PostMapping("/create-survey")
    public String createSurvey(@Valid @ModelAttribute(name = "dto") CreateUpdateSurveyDto dto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("surveys", surveyService.getAllSurveys());
            return "create-survey-form";
        }
        try {
            surveyService.createSurvey(dto);
        } catch (SurveyDataInvalid e) {
            e.printStackTrace();
            model.addAttribute("dto", dto);
            model.addAttribute("surveys", surveyService.getAllSurveys());

            return "create-survey-form";
        }

        return "redirect:/survey";
    }
}
