package com.example.test.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
    @GetMapping("/Test")
    public String homeIntro(Model model) {
//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "Test";
    }


}
