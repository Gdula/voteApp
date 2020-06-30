package com.gdula.vote.view;

import com.gdula.vote.service.mapper.UserDtoMapper;
import com.gdula.vote.service.UserService;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UpdateUserDto;
import com.gdula.vote.service.dto.UserDto;
import com.gdula.vote.service.exception.UserAlreadyExists;
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
 * class: UserViewController
 * Reprezentuje kontroler użytkownika.
 */


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

    @GetMapping("/create-user")
    public String displayCreateUserForm(Model model) {
        List<UserDto> allUsers = userService.getAllUsers();

        /*

        CreateUpdateCarDto dto = new CreateUpdateCarDto();
        dto.setHp(100);
        dto.setYear(2000);
        model.addAttribute("dto", dto);
        model.addAttribute("users", allUsers);
        */
        CreateUserDto dto = new CreateUserDto();
        model.addAttribute("dto", dto);

        return "create-user-form";
    }

    @PostMapping("/create-user")
    public String createUser(@Valid @ModelAttribute(name = "dto") CreateUserDto dto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            return "create-user-form";
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

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id) {
        try {
            userService.deleteUserById(id);
        } catch (UserNotFound userNotFound) {
            userNotFound.printStackTrace();
        }

        return "redirect:/users";
    }

    @GetMapping("/update-user/{id}")
    public ModelAndView displayUpdateUserForm(@PathVariable String id) {

        try {
            UserDto userById = userService.getUserById(id);

            UpdateUserDto updateUserDto = userDtoMapper.toUpdateDto(userById);
            ModelAndView mav = new ModelAndView("update-user-form");
            mav.addObject("dto", updateUserDto);
            mav.addObject("id", id);
            return mav;

        } catch (UserNotFound userNotFound) {
            return new ModelAndView("redirect:/users");
        }
    }

    @PostMapping("/update-user/{id}")
    public String updateUser(@ModelAttribute UpdateUserDto dto, @PathVariable String id) {

        try {
            userService.updateUser(dto, id);
            return "redirect:/users";
        } catch (UserDataInvalid | UserNotFound e) {
            return "redirect:/update-user/" + id;
        }
    }

}
