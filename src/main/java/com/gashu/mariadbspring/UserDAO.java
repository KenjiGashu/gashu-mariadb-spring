package com.gashu.mariadbspring;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import com.gashu.mariadbspring.model.UserInfo;
@Repository
public class UserDAO {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public UserInfo getUserInfo(String username){
    	String sql = "SELECT u.username name, u.password pass, a.authority role FROM "+
	    "comp_users u INNER JOIN comp_authorities a on u.username=a.username WHERE "+
	    "u.enabled =1 and u.username = ?";
    	UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject( sql, new Object[]{username},new RowMapper<UserInfo>() {
		public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		    UserInfo user = new UserInfo();
		    user.setUsername(rs.getString("name"));
		    user.setPassword(rs.getString("pass"));
		    user.setRole(rs.getString("role"));
		    return user;
		}
	    });
    	return userInfo;
    }

	public void insertNewUser(UserInfo user) {
	    BCryptPasswordEncoder bencoder = new BCryptPasswordEncoder();
	    System.out.println("password encripted: "+ bencoder.encode(user.getPassword()));
	    jdbcTemplate.update("INSERT INTO comp_users(username,password, enabled) VALUES (?, ?,1)", user.getUsername(), bencoder.encode(user.getPassword()));
	    jdbcTemplate.update("INSERT INTO comp_authorities(username, authority) VALUES(?,?)", user.getUsername(), user.getRole());
	}

    // public void simpleInsertNewUser(User user){
    // 	SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("EMPLOYEE");
    // }
}
