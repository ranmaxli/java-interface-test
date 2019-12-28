package supports.User;

import org.springframework.jdbc.core.RowMapper;
import supports.User.User;

/**
 * Created by liran on 2019/12/17 0017.
 */
public class Constants {
    public static final String SQL_GET_USER = "select * from user where username = ? and password =? ";
    public static final RowMapper<User> rowMapperForDailyView = (rs, rowNum) -> {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setDispaly_name(rs.getString("display_name"));
        return user;
    };
}
