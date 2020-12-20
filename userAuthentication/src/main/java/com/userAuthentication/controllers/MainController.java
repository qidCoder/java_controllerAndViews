package com.userAuthentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.userAuthentication.models.User;
import com.userAuthentication.services.AppService;

@Controller
public class MainController {
	
	private final AppService appService;
    
    public MainController (AppService appService) {
        this.appService = appService;
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/registration";
    }

    //display form to register a user
    @GetMapping("/registration")
    public String showRegistrationForm(@ModelAttribute("user") User user) {
        return "registrationPage.jsp";
    }

    //registering a new user to the database
    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
    	
    	// if result has errors, return and re-render the registration page
    	if(result.hasErrors()) {
			return "registrationPage.jsp";
		}
    	// else, save the user in the database
		else {
			User newUser = appService.registerUser(user);
			session.setAttribute("user_id", newUser.getId());//save the user id in session
			return "redirect:/home";//redirect them to the /home route
		}
    }
    

    // display login page
    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute("user") User user) {
        return "loginPage.jsp";
    }
    
    @PostMapping("/login")
    public String loginUser(
    		@RequestParam("email") String email, 
    		@RequestParam("password") String password, 
    	 	Model model, 
    		HttpSession session) {
    	
    	// check if the user is authenticated 
    	if(this.appService.authenticateUser(email, password)) {
    		User loggedInUser = this.appService.getUserByEmail(email);
    		session.setAttribute("user_id",loggedInUser.getId());//save their user id in session
    		return "redirect:/home";
    	}
    	// else, add error messages and re-render the login page
    	else {
    		model.addAttribute("error", "Invalid credentials. Please try again!");
    		return "loginPage.jsp";
    	}
    	
    }
    

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
      
    	Long userId = (Long) session.getAttribute("user_id");//get user from session
    	User user = appService.getUserById(userId);//save them in the model
    	model.addAttribute("user",user);
    	return "homePage.jsp";//return the home page
    }

    
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        // invalidate session
    	session.invalidate();
        // redirect to login page
    	return "loginPage.jsp";
    }
}
