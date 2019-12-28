package supports.User;

import java.io.Serializable;

/**
 * Created by liran on 2019/12/17 0017.
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String display_name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDispaly_name() {return display_name;}

    public void setDispaly_name(String display_name) {this.display_name = display_name;}

}
