package me.dec7.dao.users;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
public class MyBatisUserDaoTest {

	private static final Logger log = LoggerFactory.getLogger(MyBatisUserDaoTest.class);
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void findById() {
		User user = userDao.findById("dec7");
		
		log.debug("User: {}", user);	
	}
	
	@Test
	public void create() throws Exception {
		User user = new User("dec8", "pw", "dec", "dec@gmail.com");
		userDao.create(user);
		User actual = userDao.findById(user.getUserId());
		
		assertThat(actual, is(user));
	}

}
