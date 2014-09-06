package me.dec7.web.users;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import me.dec7.dao.users.UserDao;
import me.dec7.domain.users.Authenticate;
import me.dec7.domain.users.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

@Controller
/*
 * @Scope("session")
 * 
 * applicatioContext.xml에 되어있는 설정처럼
 * bean 등록 및 component-scan 직후 session은 기본적으로 singleton으로 설정됨
 * 그 상황에서 controller를 특정 scope로 설정할 수 있음
 */
@RequestMapping("/users")
/*
 * class에 requestMapping을 적용하는 경우 모든 하위 mehthod에 대해서도 적용 가능
 */
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	/*
	 * UserDao와 의존관계를 가지도록 설정
	 */
	/*
	 * UserController가 UserDao에 대한 의존성이 큼.
	 * 만약 MyBatis를 이용해서 Dao interface로만 의존한다면 서로간의 의존성이 크게 낮아질 것임
	 */
//	@Autowired
//	@Resource(name="myBatisUserDao")
	@Resource(name="userDao")
	/*
	 * 즉 bean에 등록되지 않더라도 해당 class 이름이 자동적으로 resource name <bean id>가 됨
	 * 이것이 싫다면 @Repository("name") 다음 처럼 annotation에 특정 이름을 지정하여 id로 사용할 수 있음
	 */
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
	
	@Autowired
	private MessageSource messageSource;
	
//	@RequestMapping("/users/form")
	@RequestMapping("/form")
	public String createform(Model model) {
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
		
		return "/users/form";
	}
	
	@RequestMapping("login/form")
	public String loginForm(Model model) {
		model.addAttribute("authenticate", new Authenticate());
		
		return "/users/login";
	}
	
	@RequestMapping("{userId}/form")
	/*
	 * 만약 pathvariable과 매개변수의 이름이 동일할 경우 새로운 이름지정을 생략해도 됨
	public String updateForm(@PathVariable("userId") String userId, Model model) {
	
	*/
	public String updateForm(@PathVariable String userId, Model model) {
		if (userId == null) {
			throw new IllegalAnnotationException(messageSource.getMessage("User.userId.null", null, Locale.KOREA));
		}
		
		User user = userDao.findById(userId);
		model.addAttribute("user", user);
		
		return "/users/form";
	}
	
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userId");
		
		return "redirect:/";
	}
	
	
	/*
	 * User와 다른 Validation을 처리하기 Authenticate를 생성 
	 */
	@RequestMapping("login")
//	public String login(@Valid Authenticate authenticate, BindingResult bindingResult, Model model) {
	
	/*
	 * session에 접근하길 원할 경우, 인자에 HttpSession을 매개변수로 받으면 됨
	 */
	public String login(@Valid Authenticate authenticate, BindingResult bindingResult, HttpSession session, Model model) {
		if (bindingResult.hasErrors()) {
			return "/users/login";
		}
		
		User user = userDao.findById(authenticate.getUserId());
		if (user == null) {
			model.addAttribute("errorMessage", messageSource.getMessage("User.userId.null", null, Locale.KOREA));
			return "/users/login";
		}
		
		/*
		 * 문제
		 * 1. 보통 이런방식으로 비교를 많이 하지만 코드가 조금 지저분함
		 * 2. user와 authenticate class 2개나 만들었지만 사용 안함.
		 * 3. login method 내부에 여러가지 분기분이 존재하여 개별 test어려움 
		 *  --> 이러한 로직을 따로 빼기보다는 만든 class에 담아서 처리하는 방법을 권유
		 *  
		 *  데이터를 담은 class 들이 가능한 많은 일을 할 수 있도록 책임을 분할 / 객체지향적인 접근
		 *  
		if (!user.getPassword().equals(authenticate.getPassword())) {
			// TODO error 처리 - 비밀번호가 틀린 경우
		}
		
		 */
		 if (!user.matchPassword(authenticate)) {
			 model.addAttribute("errorMessage", messageSource.getMessage("User.password.mismatch", null, Locale.KOREA));
				return "/users/login";
		 }
		
		// TODO session에 사용자 정보 저장
		 session.setAttribute("userId", user.getUserId());
		
		return "redirect:/";
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
	public String create(@Valid User user, BindingResult bindingResult) {
		/*
		 * create method에서 validation 필요
		 * 
		 * @Valid Annotation을 이용하면 User Class에 설정한 대로 유효성 검증이 이루어지고
		 * 결과는 BindingResult 객체로 반환됨
		 */
		
		// bindingResult가 error인 경우		
		if (bindingResult.hasErrors()) {
			log.debug("Binding Result has error");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				log.debug("error[{}]: {}", error.getCode(), error.getDefaultMessage());
			}
			
			return "/users/form";
		}
		
		log.debug("User: {}", user);
		
		/*
		 * UserControllerTest.create()에서 Mock을 사용하지 않는 경우
		 * userDao instance를 전달하지 않기 때문에 null point exception이 발생할 수 밖에 없음
		 */
		userDao.create(user);
		log.debug("Database: {}", userDao.findById(user.getUserId()));
		
		/*
		 * test시 userDao에 대한 instance를 전달하지 않음
		 */
		return "redirect:/";
	}
	
	
	@RequestMapping(value="", method=RequestMethod.PUT)
	public String update(@Valid User user, BindingResult bindingResult, HttpSession session) {
		
		if (bindingResult.hasErrors()) {
			log.debug("Binding Result has error");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				log.debug("error[{}]: {}", error.getCode(), error.getDefaultMessage());
			}
			
			return "/users/" + user.getUserId() + "/form";
		}
		
		/*	
		 * 보안1
		 * userId가 null인 경우 정상적인 접근이 아니므로 무시
		 * (방어코드) 잘못된 접근이므로 사용자에게 메시지를 전달하지 않아도 됨 
		 */
		Object temp = session.getAttribute("userId");
		if (temp == null) {
			throw new NullPointerException();
		}
		
		/*
		 * 보안2
		 * 수정이기 때문에 로그인 된 사용자만 수정할 수 있어야함.
		 */
		/*
		 * refactoring 후 user class의 update method내에 하위 내용을 담기 때문에 더 이상 필요 없어짐
		 * 
		 * 특정 데이터를 담는 class내에 비지니스 로직을 위임해야만 controller가 줄어들 수 있음.
		 * 훨씬 객체지향적인 개발방식임
		
		String userId = (String) temp;
		if (!user.matchUserId(userId)) {
			throw new NullPointerException();
		}
		*/
		
		userDao.update(user);
		log.debug("Database: {}", userDao.findById(user.getUserId()));
		
		return "redirect:/";
	}

}