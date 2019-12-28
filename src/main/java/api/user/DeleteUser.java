package api.user;

import com.github.yongchristophertang.engine.web.ResultActions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.uncommons.reportng.annotation.Case;
import supports.AppCenterTestBase;
import supports.User.Constants;
import supports.User.User;

import java.util.List;

import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.api;
import static com.github.yongchristophertang.engine.web.response.HttpResultHandlers.print;
import static com.github.yongchristophertang.engine.web.response.HttpResultMatchers.jsonPath;

/**
 * Created by liran on 2019/12/17 0017.
 */
public class DeleteUser extends AppCenterTestBase {

    private List<User> userList;

    @BeforeClass
    public void setup() throws Exception {
        initDb();
    }

    @DataProvider(name = "username-Provider")
    public Object[][] usernameData() {
        return new Object[][]{
                {"admin","admin","admin"},
                {"ranmaxli","123456","github"}
        };
    }

    @Test(dataProvider = "username-Provider")
    @Case("正常测试")
    public void deleteUser1(String username,String password,String displayName) throws Exception {
        // 发送接口请求
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).deleteUser(username,password,displayName))
                .andDo(print())
                .andExpect(jsonPath("$.code", 1))
                .andExpect(jsonPath("$.msg", "请求成功！"));

        // 验证 基础数据
        userList = jdbcTemplate.query(Constants.SQL_GET_USER, new Object[]{username,password}, Constants.rowMapperForDailyView);
        Assert.assertEquals(0,userList.size());
    }

    @Test()
    @Case("异常测试-账号为空")
    public void deleteUser2() throws Exception {
        String username = "";
        String password = "admin";
        String displayName = "admin";
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).deleteUser(username,password,displayName))
                .andDo(print())
                .andExpect(jsonPath("$.code", -1))
                .andExpect(jsonPath("$.msg", "缺少参数！"))
                .andExpect(jsonPath("$.data", ""));
    }

    @Test()
    @Case("异常测试-密码为空")
    public void deleteUser3() throws Exception {
        String username = "admin";
        String password = "";
        String displayName = "admin";
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).deleteUser(username,password,displayName))
                .andDo(print())
                .andExpect(jsonPath("$.code", -1))
                .andExpect(jsonPath("$.msg", "缺少参数！"))
                .andExpect(jsonPath("$.data", ""));
    }

    @Test()
    @Case("异常测试-昵称为空")
    public void deleteUser4() throws Exception {
        String username = "admin";
        String password = "admin";
        String displayName = "";
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).deleteUser(username,password,displayName))
                .andDo(print())
                .andExpect(jsonPath("$.code", -1))
                .andExpect(jsonPath("$.msg", "缺少参数！"))
                .andExpect(jsonPath("$.data", ""));
    }
}
