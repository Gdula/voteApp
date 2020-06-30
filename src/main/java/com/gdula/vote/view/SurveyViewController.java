package com.gdula.vote.view;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.Survey;
import com.gdula.vote.model.Variant;
import com.gdula.vote.repository.SurveyRepository;
import com.gdula.vote.service.SurveyService;
import com.gdula.vote.service.dto.*;
import com.gdula.vote.service.exception.SurveyDataInvalid;
import com.gdula.vote.service.exception.SurveyNotFound;
import com.gdula.vote.service.exception.UserDataInvalid;
import com.gdula.vote.service.exception.UserNotFound;
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

/**
 * class: SurveyViewController
 * Reprezentuje kontroler ankiety.
 */

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

        return "redirect:/surveys";
    }

    @GetMapping("/complete-survey/{id}")
    public ModelAndView showSurvey(@PathVariable String id) {
        try {
            SurveyDto surveyById = surveyService.getSurveyById(id);
            List<Question> questions= surveyById.getQuestions();

            ModelAndView mav = new ModelAndView("survey-table");
            mav.addObject("survey", surveyById);
            mav.addObject("questions", questions);
            mav.addObject("id", id);
            return mav;

        } catch (SurveyNotFound userNotFound) {
            return new ModelAndView("redirect:/surveys");
        }
    }

    @PostMapping("/complete-survey/{id}")
    public String completeSurvey(@ModelAttribute CreateUpdateSurveyDto createUpdateSurveyDto, @PathVariable String id) {

        try {
            surveyService.completeSurvey(createUpdateSurveyDto, id);
            return "redirect:/surveys";
        } catch (SurveyNotFound | UserDataInvalid | UserNotFound e) {
            return "redirect:/complete-survey/" + id;
        }
    }

    @GetMapping("/show-report/{id}")
    public ModelAndView showReport(@PathVariable String id) {
        try {
            SurveyDto surveyById = surveyService.getSurveyById(id);
            List<Question> questions= surveyById.getQuestions();

            ModelAndView mav = new ModelAndView("show-raport");
            mav.addObject("survey", surveyById);
            mav.addObject("questions", questions);
            mav.addObject("id", id);
            return mav;

        } catch (SurveyNotFound userNotFound) {
            return new ModelAndView("redirect:/surveys");
        }
    }

}
