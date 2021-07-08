package ie.ucd.RapidEyeMovement.controller;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ie.ucd.RapidEyeMovement.model.*;

@Controller
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSession userSession;
 
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/register")
    public void doRegister(String memberName, String memberEmail, String memberPassword, String memberAddress, String memberPhone, String memberBirthday, HttpServletResponse response) throws Exception
    {
        User user = new User();
        user.setName(memberName);
        user.setEmail(memberEmail);
        user.setPassword(memberPassword);
        user.setAddress(memberAddress);
        user.setPhone(memberPhone);
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Date birthday= format.parse(memberBirthday);
        user.setBirthday(birthday);
        user.setLateFee(0.0f);
        user.setRole("member");
        userRepository.save(user);
        userSession.setUser(user);
        response.sendRedirect("/");
    }

    @GetMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("error", null);

        if(userSession.isWrongRole()) {
            model.addAttribute("error", "Please use correct login portal");
            userSession.setWrongRole(false);
        }
        if(userSession.isLoginFailed())
        {
            model.addAttribute("error", "Email or password not correct");
            userSession.setLoginFailed(false);
        }
        return "login.html";
    }

    @PostMapping("/member-login")
    public void doMemberLogin(String memberEmail, String memberPassword, HttpServletResponse response) throws Exception
    {
        Optional<User> user = userRepository.findByEmailAndPassword(memberEmail, memberPassword);

        if (user.isPresent())
        {
            User member = user.get();
            if(member.getRole().equals("member")) {
                userSession.setUser(user.get());
                response.sendRedirect("/");
            }
            else {
                userSession.setWrongRole(true);
                response.sendRedirect("/login");
            }
        }
        else{
            userSession.setLoginFailed(true);
            response.sendRedirect("/login");
        }
    }

    @PostMapping("/admin-login")
    public void doAdminLogin(String adminEmail, String adminPassword, HttpServletResponse response) throws Exception
    {
        Optional<User> user = userRepository.findByEmailAndPassword(adminEmail, adminPassword);

        if (user.isPresent())
        {
            User admin = user.get();
            if(admin.getRole().equals("admin")) {
                userSession.setUser(user.get());
                response.sendRedirect("/");
            }
            else {
                userSession.setWrongRole(true);
                response.sendRedirect("/login");
            }
        }
        else{
            userSession.setLoginFailed(true);
            response.sendRedirect("/login");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws Exception{
        userSession.setUser(null);
        response.sendRedirect("/");
    }


}