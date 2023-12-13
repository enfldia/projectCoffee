package projectCoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentsController {

    @GetMapping("/payments/complete")
    public String complete(){
        return "/payments/complete";
    }
}
