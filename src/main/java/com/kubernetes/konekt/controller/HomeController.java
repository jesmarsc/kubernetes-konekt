package com.kubernetes.konekt.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
    
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws IOException {
		return "home";
	}
	@RequestMapping(value = "/java-script", method = RequestMethod.GET)
	public String javaScriptDemo(Model model) throws IOException {
		return "java-script-demo";
	}
}
