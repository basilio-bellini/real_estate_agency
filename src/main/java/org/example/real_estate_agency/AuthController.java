package org.example.real_estate_agency;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               @RequestHeader(value = "Referer", required = false) String referer) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("registrationError", "Ошибка регистрации. Проверьте введенные данные.");
            return "redirect:" + (referer != null ? referer : "/");
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("registrationError", "Пользователь с таким именем уже существует.");
            return "redirect:" + (referer != null ? referer : "/");
        }

        userService.save(user);

        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("actionMessage",
                "Регистрация прошла успешно");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user,
                            HttpSession session,
                            @RequestHeader(value = "Referer", required = false) String referer,
                            RedirectAttributes redirectAttributes) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && userService.checkPassword(existingUser, user.getPassword())) {
            session.setAttribute("loggedInUser", existingUser);
            session.setAttribute("userRole", existingUser.getRole());
            redirectAttributes.addFlashAttribute("actionMessage", "Вход выполнен успешно");

            if (referer != null && referer.contains("/login")) {
                return "redirect:/";
            }

            return "redirect:" + (referer != null ? referer : "/");
        } else {
            redirectAttributes.addFlashAttribute("loginError", "Неверное имя пользователя или пароль");
            return "redirect:" + (referer != null ? referer : "/");
        }
    }

    @GetMapping("/login")
    public String loginPage(@RequestHeader(value = "Referer", required = false) String referer,
                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginError", "Доступ только через форму входа");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session,
                             RedirectAttributes redirectAttributes,
                             @RequestHeader(value = "Referer", required = false) String referer) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("actionMessage", "Вы успешно вышли из системы");
        return "redirect:" + (referer != null ? referer : "/");
    }

}
