package me.dec7.domain.users;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTest {
	
	private static Validator validator;
	
	private static final Logger log = LoggerFactory.getLogger(UserTest.class);
	
	@BeforeClass
	/*
	 * junit에서
	 * @BeforeClass와 @Before가 하는 일 학습하기
	 */
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	
	@Test
	public void userIdWhenIsEmpty() {
		
		// 특정 정보를 가진 User 생성		
		User user = new User("", "password", "name", "dec7@gmail.com");
		
		// user 객체의 유효성 검사를 체크한 다음 결과를 constraintViolations에 저장		
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		
		assertThat(constraintViolations.size(), is(2));
		
		
		// validation 결과 출력
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	
	@Test
	public void matchPassword() throws Exception {
		String password = "password";
		User user = new User("userId", password, "name", "dec7@gmail.com");
		
		Authenticate authenticate = new Authenticate("userId", password);
		assertThat(user.matchPassword(authenticate), is(true));
		
		authenticate = new Authenticate("userId", "password2");
		assertFalse(user.matchPassword(authenticate));
	}
	
	

}





