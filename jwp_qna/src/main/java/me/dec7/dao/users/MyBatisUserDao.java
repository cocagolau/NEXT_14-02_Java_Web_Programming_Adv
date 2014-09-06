package me.dec7.dao.users;

import javax.annotation.Resource;

import me.dec7.domain.users.User;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/*
 * backend에서 lookup을 annotation 기반으로 설정하기 위함
 * DB 접근 로직임을 알리기 위해서 Repository를 사용
 */
// @Repository
@Repository("userDao") // id이름을 직접 지정할 수 있음
public class MyBatisUserDao implements UserDao {
	
	private static final Logger log = LoggerFactory.getLogger(MyBatisUserDao.class);
	
	/*
	 * @Autowired
	 * autowired annotation은 type을 기준으로 자동적으로 injection 됨
	 * 하지만 명확하게 bean id를 기준으로 injection 하기 위해서 Resource annotation을 사용할 수 있음
	 */
	@Resource(name="sqlSession")
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
	
	/*
	 * autowired를 사용함으로써 setter injection을 필요없게 됨
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	*/
	
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
