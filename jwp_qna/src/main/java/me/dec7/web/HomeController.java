package me.dec7.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


// 실제 controller로 인식될 수 있도록 spring annotation을 사용해야 함
@Controller
public class HomeController {
	
	// 특정 url 접근시 아래 home 메소드로 이동될 수 있도록 annotation 설정
	@RequestMapping("/")
	public String home() {

		// root url 접근시 이동할 view(jsp)의 이름
		// internal resource view resolver에 의해서 prefix와 suffix를 붙인 물리적인 jsp 파일을 검색		
		return "home";
	}
}
