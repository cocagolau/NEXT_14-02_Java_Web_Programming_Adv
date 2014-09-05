package me.dec7.web.users;

import me.dec7.dao.users.UserDao;
import me.dec7.domain.users.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
/*
 * class에 requestMapping을 적용하는 경우 모든 하위 mehthod에 대해서도 적용 가능
 */
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	/*
	 * UserDao와 의존관계를 가지도록 설정
	 */
	@Autowired
	private UserDao userDao;
	
	/*
	 * spring은 java reflection 기능을 이용해 DI기능 제공
	 * private field에 대해서도 적용되며
	 * 만약 이것이 싫다면 setter method를 생성 후 DI
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	 */
	
//	@RequestMapping("/users/form")
	@RequestMapping("/form")
	public String form(Model model) {
		
		
		/*
		 * spring form tag를 사용시 path element의 값은 DTO의 setter/getter와 연결되어야 함
		 */
		/*
		 * 매개변수 model을 불러와 user라는 이름으로 아무런 값이 없는 새로는 User객체를 생성 후 연결
		 * 
		 * servlet/jsp에서 처럼 request에 addAttribute를 할때처럼 데이터를 jsp에 전달하는 역할을 함.
		 * spring에서는 model class를 사용함
		 * 
		 * user라는 이름으로 User 객체를 전달
		 */
		model.addAttribute("user", new User());
		
		
		return "users/form";
	}
	
	/*
	 * method는 post방식이고, 구별을 위해 mapping은 value로 적용
	 */
//	@RequestMapping(value="/users", method=RequestMethod.POST)
	@RequestMapping(value="", method=RequestMethod.POST)
/*
 * 	public String create(String userId, String password ... ) {
 * 	등 매개변수를 통해 입력데이터를 받아올 수도 있으나
 * 	전달 받는 데이터의 양이 많아지거나 매개변수의 이름이 변경되는 경우 자동적으로 처기하기 어려우므로 다른 방식을 이용함
 */
	public String create(User user) {
		log.debug("User: {}", user);
		
		userDao.create(user);
		log.debug("Database: {}", userDao.findById(user.getUserId()));
		
		return "users/form";
	}

}