package com.gdula.vote.view;

import com.gdula.vote.model.*;
import com.gdula.vote.repository.AnswerRepository;
import com.gdula.vote.repository.SurveyRepository;
import com.gdula.vote.service.SecurityUtils;
import com.gdula.vote.service.SurveyService;
import com.gdula.vote.service.dto.*;
import com.gdula.vote.service.exception.SurveyDataInvalid;
import com.gdula.vote.service.exception.SurveyNotFound;
import com.gdula.vote.service.exception.UserDataInvalid;
import com.gdula.vote.service.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class: SurveyViewController
 * Reprezentuje kontroler ankiety.
 */

@Controller
public class SurveyViewController {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * method: displaySurveyTable
     * Wyświetla ankiety
     */
    @GetMapping("/surveys")
    public ModelAndView displaySurveyTable() {
        ModelAndView mav = new ModelAndView("surveys-table");

        List<SurveyDto> allSurveys = surveyService.getAllSurveys();
        mav.addObject("surveys", allSurveys);

        return mav;
    }

    /**
     * method: mySurveys
     * Wyświetla ankiety użytkownika
     */
    @GetMapping("/surveys/my")
    public ModelAndView mySurveys(Model model) {
        ModelAndView mav = new ModelAndView("my-surveys");

        List<Survey> mySurveys = surveyService.getUserSurveys();
        mySurveys.forEach(survey -> System.out.println(survey.getName()));
        mav.addObject("surveys", mySurveys);

        return mav;
    }

    /**
     * method: displayCreateSurveyForm
     * Wyświetla formularz do tworzenia ankiety
     */
    @GetMapping("/create-survey")
    public String displayCreateSurveyForm(Model model) {
        CreateUpdateSurveyDto dto = new CreateUpdateSurveyDto();
        model.addAttribute("dto", dto);

        return "create-survey-form";
    }

    /**
     * method: createSurvey
     * Zapisuje wypełniony formularz stworzonej ankiety
     */
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

    /**
     * method: showSurvey
     * Wyświetla formularz do wypełnienia ankiety
     */
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

    /**
     * method: completeSurvey
     * Zapisuje wypełniony formularz stworzonej ankiety
     */
    @PostMapping("/complete-survey/{id}")
    public String completeSurvey(@RequestBody MultiValueMap<String, String> formData, @PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            String hash = surveyService.completeSurvey(formData, id);
            redirectAttributes.addFlashAttribute("hash", hash);
            return "redirect:/surveys/complete";
        } catch (SurveyNotFound | UserDataInvalid | UserNotFound e) {
            return "redirect:/complete-survey/" + id;
        }
    }

    /**
     * method: completeSurvey
     * Wyświetla hasz po wypełnieniu ankiety
     */
    @GetMapping("/surveys/complete")
    public ModelAndView completeSurvey(@ModelAttribute("hash") String hash) {
        ModelAndView mav = new ModelAndView("survey-complete");
        mav.addObject("hash", hash);
        return mav;
    }

    /**
     * method: openSurveys
     * Wyświetla ankiety wypełnione przez uzytkownika
     */
    @GetMapping("/surveys/my/{id}")
    public String openSurveys(@PathVariable String id, Model model) {
        SurveyOpenDto dto = new SurveyOpenDto();
        model.addAttribute("dto", dto);
        model.addAttribute("id", id);
        return "open-surveys";
    }

    /**
     * method: openSurvey
     * Znajduje wyniki ankiety za pomocą hasza wprowadzonego przez użytkownika i przekazuje je do /surveys/my/{id}/show
     */
    @PostMapping("/surveys/my/{id}")
    public String openSurvey(@PathVariable String id, @Valid @ModelAttribute(name = "hash") SurveyOpenDto dto, RedirectAttributes redirectAttributes) {
        Set<Answer> answers = answerRepository.findByAnswerIdUserIdAndAnswerIdSurveyId(dto.getHash(), id);

        if (answers.size() > 0) {
            redirectAttributes.addFlashAttribute("answers", answers);
            return "redirect:/surveys/my/{id}/show";
        }

        return "redirect:/surveys";
    }

    /**
     * method: mySurvey
     * Wyświela wyniki ankiety wypełnione przez uzytkownika
     */
    @GetMapping("/surveys/my/{id}/show")
    public ModelAndView mySurvey(@PathVariable String id, @ModelAttribute(name = "answers") Set<Answer> answers) throws
            SurveyNotFound, NoSuchAlgorithmException {
        SurveyDto survey= surveyService.getSurveyById(id);
        ModelAndView mav = new ModelAndView("my-survey");
        List<String> questionResponses = new ArrayList<>();
        Map<String, String> answerKeyValue = answers.stream().collect(Collectors.toMap(answer -> answer.getAnswerId().getQuestionId(), Answer::getAnswerKey));
        survey.getQuestions().forEach(question -> {
            StringBuilder stringBuilder = new StringBuilder(question.getQuestionText());
            stringBuilder.append(" Answer: ");
            if (answerKeyValue.containsKey(question.getId())) {
                question.getVariants().stream().filter(variant -> variant.getId().equals(answerKeyValue.get(question.getId())))
                         .findFirst()
                        .ifPresent(variant -> stringBuilder.append(variant.getVariant()));

            } else {
                stringBuilder.append("MISSING");
            }
            questionResponses.add(stringBuilder.toString());
        });

        List<String> answersValueList = new ArrayList<>();

        for (Map.Entry<String, String> entry : answerKeyValue.entrySet()) {
            answersValueList.add(entry.getValue());
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String answer : answersValueList) {
            stringBuilder.append(answer);
        }

        String answersInOneString = stringBuilder.toString();
        System.out.println(answersInOneString + " survey vc");

        String plaintext = answersInOneString;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        String hash = hashtext;

        mav.addObject("hash", hash);
        mav.addObject("survey", survey);
        mav.addObject("questionResponses", questionResponses);
        return mav;
    }

    /**
     * method: showReport
     * Wyświela wyniki ankiety wypełnione przez uzytkowników
     */
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

    /**
     * method: checkIfVoted
     * Sprawdza czy użytkownik zagłosował
     */
    @GetMapping("/check-if-voted/{id}/")
    public String checkIfVoted(@PathVariable String id) {
        return "redirect:/survey-complete";
    }

    /**
     * method: check
     * Sprawdza czy użytkownik zagłosował
     */
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
