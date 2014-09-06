package me.dec7.dao.users;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import me.dec7.domain.users.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class JdbcUserDaoTest {
	
	private static final Logger log = LoggerFactory.getLogger(JdbcUserDaoTest.class);
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void findById() {
		User user = userDao.findById("javajigi");
		
		log.debug("User: {}", user);	
	}
	
	
	@Test
	public void create() throws Exception {
		
		User user = new User("dec7", "pw", "dec", "dec@gmail.com");
		userDao.create(user);
		User actual = userDao.findById(user.getUserId());
		
		assertThat(actual, is(user));

	}
}
