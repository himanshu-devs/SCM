package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.services.EmailService;

@SpringBootTest
class ScmApplicationTests {

	// @Test
	// void contextLoads() {
	// }

	@Autowired 
	private EmailService emailService;

	@Test 
	void sendEmailTest(){
		emailService.sendEmail("himanshurajput1109@gmail.com", "Testing my project email service using mailStrap", "This is scm project working on email service");
	}

}
