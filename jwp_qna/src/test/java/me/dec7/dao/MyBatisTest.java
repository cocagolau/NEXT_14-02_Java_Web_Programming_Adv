package me.dec7.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import javax.sql.DataSource;

import me.dec7.domain.users.User;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class MyBatisTest {
	private static final Logger log = LoggerFactory.getLogger(MyBatisTest.class); 
	private SqlSessionFactory sqlSessionFactory;
	
	@Before
	public void setup()  throws Exception{
		String resource = "mybatis-config-test.xml";
		InputStream is = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
		
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("dec7.sql"));
		DatabasePopulatorUtils.execute(populator, getDataSource());
		
		log.info("database initialized success!!");

	}
	
	private DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:~/dec7");
		dataSource.setUsername("sa");
		
		return dataSource;
	}

	@Test
	public void gettingStarted() {
		/*
		 * SqlSession --> Jdbc의 Connection과 비슷한 역할을 함
		 * SqlSession을 통해 DB와 연결 및 실행을 진행
		 * 따라서 작업완료 후 close() 처리
		 */
		
		/*
		SqlSession session = sqlSessionFactory.openSession();
		try {
			User user = session.selectOne("UserMapper.findById", "dec7");
			log.debug("user: {}", user);
			
		} finally {
			session.close();
		}
		
		 * 아래는
		 * jdk 7.0부터 바뀐 내용으로 try구분에서 필요한 resource를 생성하고 종료할 수 있음 <언어차원에서 지원>
		 * 
		 * 하지만 Closable interface를 반드시 구현해야만 finally를 사용해 close를 하지 않아도 자동화 가능함
		 */
		try (SqlSession session = sqlSessionFactory.openSession()) {
			User user = session.selectOne("UserMapper.findById", "dec7");
			log.debug("user: {}", user);
		}
	}
	
	@Test
	public void insert() throws Exception {
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			User user = new User("userId", "password", "name", "email@gmail.com");
			session.insert("UserMapper.create", user);
			User actual = session.selectOne("UserMapper.findById", user.getUserId());
			
			assertThat(actual, is(user));
		}
	}

}
