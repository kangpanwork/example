package com.sanri.test.redissession.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.SessionOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class SessionController {

	@GetMapping("/sessionId")
	public String sessionId(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("user","sanri");
		return session.getId();
	}

	@Autowired
	SessionOperation sessionOperation;

	@GetMapping("/login")
	public void login(HttpServletRequest request){
		// 登录成功后
		String id = request.getSession().getId();
		sessionOperation.loginSuccess(id);
	}

}
