package com.gdula.vote.view;

import com.gdula.vote.service.UserDtoMapper;
import com.gdula.vote.service.UserService;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UserDto;
import com.gdula.vote.service.exception.UserAlreadyExists;
import com.gdula.vote.service.exception.UserDataInvalid;
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
public class UserViewController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @GetMapping("/users")
    public ModelAndView displayUsersTable() {
        ModelAndView mav = new ModelAndView("users-table");

        List<UserDto> allUsers = userService.getAllUsers();
        mav.addObject("users", allUsers);

        return mav;
    }

    @PostMapping("/create-user")
    public String createUser(@Valid @ModelAttribute(name = "dto") CreateUserDto dto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("users", userService.getAllUsers());
            return "create-car-form";
        }
        try {
            userService.createUser(dto);
        } catch (UserAlreadyExists | UserDataInvalid e) {
            // powrót do formularza z tymi samymi danymi
            e.printStackTrace();
            model.addAttribute("dto", dto);
            model.addAttribute("users", userService.getAllUsers());

            return "create-user-form";
        }

        return "redirect:/users";
    }


}
