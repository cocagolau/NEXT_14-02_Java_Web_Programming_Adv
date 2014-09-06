package me.dec7.dao.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.dec7.domain.users.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/*
 * spring jdbc 지원 기능을 dao 구현
 */
public class JdbcUserDao extends JdbcDaoSupport implements UserDao {
	
	private static final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);
	
	/*
	 * 따라서 UserDao라는 Class를 Spring이 Instance를 생성할 수 있도록 설정 필요
	 * 다음에 DataSource를 UserDao가 사용할 수 있도록 설정 필요
	 */
	
	/*
	 *  PostContruct라는 Annotation을 사용하면
	 *  userDao라는 Class가 spring에 의해 instance가 생성되고 초기화 작업이 필요할 때
	 *  initialize가 자동으로 호출되어 초기화 됨
	 */ 
	
	/*
	@PostConstruct
	public void initialize() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("dec7.sql"));
		DatabasePopulatorUtils.execute(populator, getDataSource());
		
		log.info("database initialized success");
	}
	*/

	/* (non-Javadoc)
	 * @see me.dec7.dao.users.IUserDao#findById(java.lang.String)
	 */
	@Override
	public User findById(String userId) {
		String sql = "select * from USERS where userId = ?";
		RowMapper<User> rowMapper = new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				return new User(
						rs.getString("userId"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("email")
					);
			}
			
		};
		
		
		try {
			
			return getJdbcTemplate().queryForObject(sql, rowMapper, userId);
			
		} catch (EmptyResultDataAccessException e) {
			
			return null;
			
		}
		
		
	}

	/* (non-Javadoc)
	 * @see me.dec7.dao.users.IUserDao#create(me.dec7.domain.users.User)
	 */
	@Override
	public void create(User user) {
		String sql = "insert into USERS values (?, ?, ?, ?)";
		
		getJdbcTemplate().update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}

	/* (non-Javadoc)
	 * @see me.dec7.dao.users.IUserDao#update(me.dec7.domain.users.User)
	 */
	@Override
	public void update(User user) {
		String sql = "update USERS set password = ?, name = ?, email = ? where userId = ?";
		
		getJdbcTemplate().update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());		
	}
	
	
	
}
