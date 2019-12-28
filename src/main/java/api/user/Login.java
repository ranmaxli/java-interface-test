package api.user;

import api.util.JsonUtil;
import com.github.yongchristophertang.engine.web.ResultActions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.uncommons.reportng.annotation.Case;
import supports.AppCenterTestBase;
import supports.User.User;
import supports.User.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.api;
import static com.github.yongchristophertang.engine.web.response.HttpResultHandlers.print;
import static com.github.yongchristophertang.engine.web.response.HttpResultMatchers.jsonPath;

/**
 * Created by liran on 2019/12/17 0017.
 */
public class Login extends AppCenterTestBase {
    private List<User> userList;

    @BeforeClass
    public void setup() throws Exception {
        initDb();
    }

    @DataProvider(name = "username-Provider")
    public Object[][] usernameData() {
        return new Object[][]{
                {"admin","admin"},
                {"ranmaxli","123456"}
        };
    }

    @Test(dataProvider = "username-Provider")
    @Case("正常测试")
    public void login1(String username,String password) throws Exception {
        // 发送接口请求
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).login(username,password))
                .andDo(print())
                .andExpect(jsonPath("$.code", 1))
                .andExpect(jsonPath("$.msg", "请求成功！"));

        // 验证 基础数据
        userList = jdbcTemplate.query(Constants.SQL_GET_USER, new Object[]{username,password}, Constants.rowMapperForDailyView);
        resultActions.andExpect(jsonPath("$.data.username", userList.get(0).getUsername()));
        resultActions.andExpect(jsonPath("$.data.password", userList.get(0).getPassword()));
        resultActions.andExpect(jsonPath("$.data.dispaly_name", userList.get(0).getDispaly_name()));
    }

    @Test()
    @Case("异常测试-账号为空")
    public void login2() throws Exception {
        String username = "";
        String password = "admin";
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).login(username,password))
                .andDo(print())
                .andExpect(jsonPath("$.code", -1))
                .andExpect(jsonPath("$.msg", "缺少参数！"))
                .andExpect(jsonPath("$.data", ""));
    }

    @Test()
    @Case("异常测试-密码为空")
    public void login3() throws Exception {
        String username = "admin";
        String password = "";
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).login(username,password))
                .andDo(print())
                .andExpect(jsonPath("$.code", -1))
                .andExpect(jsonPath("$.msg", "缺少参数！"))
                .andExpect(jsonPath("$.data", ""));
    }

    @Test()
    @Case("异常测试-账号密码均为空")
    public void login4() throws Exception {
        String username = "";
        String password = "";
        ResultActions resultActions = webTemplate.perform(api(CoreAPI.class).login(username,password))
                .andDo(print())
                .andExpect(jsonPath("$.code", -1))
                .andExpect(jsonPath("$.msg", "缺少参数！"))
                .andExpect(jsonPath("$.data", ""));
    }
}
