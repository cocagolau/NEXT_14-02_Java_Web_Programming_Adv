package me.dec7.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;

public class HomeControllerTest {

	@Test
	public void home() throws Exception {
		standaloneSetup(new HomeController())
				.build()
				.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("home"));
		
	}

}
