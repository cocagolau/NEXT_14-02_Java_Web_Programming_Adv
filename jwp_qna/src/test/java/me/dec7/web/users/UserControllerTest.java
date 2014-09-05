package me.dec7.web.users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import me.dec7.dao.users.UserDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

/*
 * Mockito도 JUnitRunner를 가지고 있음
 * 아래처럼 지정하면
 * 밑에 설정되어 있는 Annotation을 기반으로 하여 Class간 의존관계 설정을 자동으로 해줌
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {	
	/*
	 * 가짜 instance를 만드는 것.
	 * 실제로 DB와 연동하지 않지만 userDao를 사용할 수 있음.
	 */
	@Mock
	private UserDao userDao;
	
	/*
	 * test할 대상의 코드 <UserController>와
	 * test대상 코드에서 사용하는 코드<UserDao>의 의존관계를 다음처럼 엮을 수 있음
	 * 
	 * UserController가 생성이 될 때
	 * UserDao Mock을 만들어서 두 클래스간의 의존관계를 연결할 수 있음
	 */
	@InjectMocks
	private UserController userController;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
//		this.mockMvc = standaloneSetup(new UserController())
		this.mockMvc = standaloneSetup(userController)
				.alwaysExpect(status().isMovedTemporarily())
				.build();
	}
	
	@Test
	public void createWhenValid() throws Exception{
		
		/* 
		 * 이 상황에서는 user에 대한 정보를 받을 수 없기 때문에 null point exception이 발생할 수 밖에 없다.
		 * 이 문제를 해결하기 위해서 Mock Framework를 사용하여 해결한다 
		 * Mock Framework는 가짜 객체를 만들어주는 Test Framework임.
		 * 
		 * 다양한 Mock Framework 중 Mockito를 사용할 거임
		 */
		this.mockMvc.perform(
				post("/users")
				.param("userId", "dec7")
				.param("password", "password")
				.param("name", "동규")
				.param("email", "dec7@gmail.com"))
			.andDo(print())
			.andExpect(status().isMovedTemporarily())
			.andExpect(redirectedUrl("/"));
	}
	
	@Test
	public void createWhenInvalid() throws Exception{
		
		this.mockMvc.perform(
				post("/users")
				.param("userId", "dec7")
				.param("password", "password")
				.param("name", "동규")
				.param("email", "dec7"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/users/form"));
	}
	
	

}
