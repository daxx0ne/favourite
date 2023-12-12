package yu.favourite.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/home/main";
    }

    @GetMapping("/home/main")
    public String showMainPage() {
        return "home/main";
    }
}

