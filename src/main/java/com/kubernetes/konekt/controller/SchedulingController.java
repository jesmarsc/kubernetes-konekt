package com.kubernetes.konekt.controller;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.entity.ScheduledTask;

@Controller
public class SchedulingController {
	
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@RequestMapping(value="/schedule")
	public String ShowForm(Model theModel) {
		ScheduledTask newForm = new ScheduledTask();
		//String stringT = new String();
		//stringT = newForm.toString();

		theModel.addAttribute("scheduleForm", newForm);
		//theModel.addAttribute("stringTask", stringT);
	
		return "schedule-form";
	}
	
	
	@PostMapping(value="/scheduleConfirmation")
	public String details(@Valid @ModelAttribute("scheduleForm") ScheduledTask form,
			BindingResult theBindingResult, Model model) {
		
		if(theBindingResult.hasErrors()) {
			return "schedule-form";
		}
		
		return "schedule-confirmation";
	}

}
