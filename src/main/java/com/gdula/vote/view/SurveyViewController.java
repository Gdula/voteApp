package com.gdula.vote.view;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.Survey;
import com.gdula.vote.model.User;
import com.gdula.vote.model.Variant;
import com.gdula.vote.repository.SurveyRepository;
import com.gdula.vote.service.SecurityUtils;
import com.gdula.vote.service.SurveyService;
import com.gdula.vote.service.dto.*;
import com.gdula.vote.service.exception.SurveyDataInvalid;
import com.gdula.vote.service.exception.SurveyNotFound;
import com.gdula.vote.service.exception.UserDataInvalid;
import com.gdula.vote.service.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private SecurityUtils securityUtils;

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
    public String completeSurvey(@RequestBody MultiValueMap<String, String> formData, @PathVariable String id) {
        try {
            surveyService.completeSurvey(formData, id);
            return "redirect:/surveys/complete";
        } catch (SurveyNotFound | UserDataInvalid | UserNotFound e) {
            return "redirect:/complete-survey/" + id;
        }
    }

    @GetMapping("/surveys/complete")
    public ModelAndView completeSurvey() {
        ModelAndView mav = new ModelAndView("survey-complete");
        mav.addObject("hash", new String("xczxczxcz").hashCode());
        return mav;
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

    @GetMapping("/check-if-voted/{id}/")
    public String checkIfVoted(@PathVariable String id) {
        return "redirect:/survey-complete";
    }

    @PostMapping ("/check-if-voted/{id}/")
    public String check(@PathVariable String id) {
        try {
            String login = securityUtils.getUserName();
            ModelAndView mav = new ModelAndView("survey-complete");
            SurveyDto survey = surveyService.getSurveyById(id);

            List<User> users = survey.getParticipants();

            for (User user : users) {
                if (user.getLogin().equals(login)) {
                    return "redirect:/survey-complete";
                }
            }
            return "redirect:/survey-not-complete";
        } catch (SurveyNotFound surveyNotFound) {
            return "redirect:/surveys";
        }
    }

}
