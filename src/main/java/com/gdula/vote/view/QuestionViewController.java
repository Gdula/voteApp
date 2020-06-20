package com.gdula.vote.view;

import com.gdula.vote.model.Question;
import com.gdula.vote.repository.QuestionRepository;
import com.gdula.vote.service.QuestionService;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.QuestionDto;
import com.gdula.vote.service.exception.QuestionDataInvalid;
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
public class QuestionViewController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping("/questions")
    public ModelAndView displayQuestionTable() {
        ModelAndView mav = new ModelAndView("questions-table");

        List<QuestionDto> allQuestions = questionService.getAllQuestions();
        mav.addObject("questions", allQuestions);

        return mav;
    }

    @GetMapping("/create-question")
    public String displayCreateQuestionForm(Model model) {
        CreateUpdateQuestionDto dto = new CreateUpdateQuestionDto();
        model.addAttribute("dto", dto);

        return "create-question-form";
    }

    @PostMapping("/create-question")
    public String createQuestion(@Valid @ModelAttribute(name = "dto") CreateUpdateQuestionDto dto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("questions", questionService.getAllQuestions());
                return "create-question-form";
        }
        try {
            questionService.createQuestion(dto);
        } catch (QuestionDataInvalid e) {
            e.printStackTrace();
            model.addAttribute("dto", dto);
            model.addAttribute("questions", questionService.getAllQuestions());

            return "create-question-form";
        }

        return "redirect:/questions";
    }

    @GetMapping("/questions-variants")
    public ModelAndView displayQuestionsAndVariants() {
        ModelAndView mav = new ModelAndView("questions-variants-table");

        List<Question> questions = questionRepository.findAll();


        List<QuestionDto> allQuestions = questionService.getAllQuestions();
        mav.addObject("questions", allQuestions);

        return mav;
    }
}
