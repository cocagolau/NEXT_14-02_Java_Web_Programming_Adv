package me.dec7.dao.users;

import me.dec7.domain.users.User;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBatisUserDao implements UserDao {
	
	private static final Logger log = LoggerFactory.getLogger(MyBatisUserDao.class);
	
	private SqlSession sqlSession;
	
	/*
	 * flyway migration으로 필요 없어짐
	 * 
	private DataSource dataSource;
	
	@PostConstruct
	public void initialize() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("dec7.sql"));
		DatabasePopulatorUtils.execute(populator, dataSource);
		
		log.info("database initialized success!!");
	}
	*/
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/*
	 * 자바지기님께서는
	 * SqlSession이 DataSource와 동등하다고 하였으나
	 * Database를 초기화하기 위해 DataSource를 필요한 상황이 발생함.
	 * 고칠 필요가 있음 
	 */
	/*
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	*/
	
	@Override
	public User findById(String userId) {
		
		return sqlSession.selectOne("UserMapper.findById", userId);
	}

	@Override
	public void create(User user) {
		sqlSession.insert("UserMapper.create", user);
	}

	@Override
	public void update(User user) {
		sqlSession.insert("UserMapper.update", user);
	}

}
