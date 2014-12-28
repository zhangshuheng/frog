package com.frog.app.controller;


import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class TestController {
	static Logger logger = LogManager.getLogger(TestController.class.getName());

	@RequestMapping("/index")
	@ResponseBody
	public Object index(){
		logger.debug("index");
		return new Date();
	}
}
