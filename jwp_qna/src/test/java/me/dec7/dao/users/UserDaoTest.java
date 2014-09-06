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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
// transaction 설정
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
/*
 *  해당 class를 transaction을 처리하기 위한 annotation
 *  해당 class의 method를 모두 실행한 다음 / 해당 데이터들은 자동적으로 rollback이 됨
 */
// @Transactional
public class UserDaoTest {

	private static final Logger log = LoggerFactory.getLogger(UserDaoTest.class);
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void findById() {
		User user = userDao.findById("dec7");
		
		log.debug("User: {}", user);	
	}
	
	@Test
	/*
	 * transactional level를 class 단위가 아닌 method 단위에서도 설정 가능
	 */
	@Transactional
	public void create() throws Exception {
		User user = new User("dec888", "pw", "dec", "dec@gmail.com");
		userDao.create(user);
		User actual = userDao.findById(user.getUserId());
		
		assertThat(actual, is(user));
	}

}
