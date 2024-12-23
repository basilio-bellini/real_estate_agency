package org.example.real_estate_agency;

import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;


@Controller
public class AppController {

    private final AppartmentService service;
    private final UserService userService;

    public AppController(AppartmentService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String viewHomePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        if (loggedInUser != null) {
            model.addAttribute("userRole", loggedInUser.getRole());
        }
        return "index";
    }

    @RequestMapping("/new")
    public String showNewAppartmentForm(HttpSession session, Model model) {
        String role = (String) session.getAttribute("userRole");
        if (role == null || (!role.equals("ADMIN") && !role.equals("USER"))) {
            return "redirect:/";
        }
        Appartment appartment = new Appartment();
        model.addAttribute("appartment", appartment);
        return "new_appartment";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveAppartment(HttpSession session, @ModelAttribute("appartment") Appartment appartment) {
        String role = (String) session.getAttribute("userRole");
        if (role == null || (!role.equals("ADMIN") && !role.equals("USER"))) {
            return "redirect:/";
        }
        service.save(appartment);
        return "redirect:/catalog";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditAppartmentForm(HttpSession session, @PathVariable(name = "id") Long id) {
        String role = (String) session.getAttribute("userRole");
        if (role == null || !role.equals("ADMIN")) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mav = new ModelAndView("edit_appartment");
        Appartment appartment = service.get(id);
        mav.addObject("appartment", appartment);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteAppartment(HttpSession session, @PathVariable(name = "id") Long id) {
        String role = (String) session.getAttribute("userRole");
        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/catalog";
        }
        service.delete(id);
        return "redirect:/catalog";
    }

    @ModelAttribute
    public void addUserAttributes(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("userRole", session.getAttribute("userRole"));
    }

    @GetMapping("/api/AppartmentData")
    @ResponseBody
    public List<AppartmentData> getSessionData() {
        List<Object[]> results = service.getAppartmentAvgPriceGroupedByCategory();
        List<AppartmentData> sessionDataList = new ArrayList<>();
        for (Object[] result : results) {
            String category = (String) result[0];
            int average_price = ((Number) result[1]).intValue();
            sessionDataList.add(new AppartmentData(category, average_price));
        }
        return sessionDataList;
    }

    @RequestMapping("/statistics")
    public String viewStatisticsPage() {
        return "stats";
    }

    @RequestMapping("/catalog")
    public String viewAppartmentsPage(Model model, @Param("keyword") String keyword) {
        List<Appartment> listAppartments = service.listAll(keyword);
        model.addAttribute("listAppartments", listAppartments);
        model.addAttribute("keyword", keyword);
        return "catalog";
    }

    @RequestMapping("/users")
    public String viewUsersPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/";
        }

        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("loggedInUser", loggedInUser);
        return "users";
    }

    @RequestMapping(value = "/users/update-role", method = RequestMethod.POST)
    public String updateUserRole(HttpSession session,
                                 @RequestParam("userId") int userId,
                                 @RequestParam("role") String role) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/";
        }

        User user = userService.findById(userId);
        if (user != null) {
            user.setRole(role);
            userService.save(user);
        }

        return "redirect:/users";
    }
}

