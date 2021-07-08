package ie.ucd.RapidEyeMovement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ie.ucd.RapidEyeMovement.model.UserSession;

@Controller
public class MyErrorController implements ErrorController  {
    @Autowired
    private UserSession userSession;
    
    @RequestMapping("/error")
    public String error(Model model) {
        model.addAttribute("user", userSession.getUser());
        return "error";
    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
}