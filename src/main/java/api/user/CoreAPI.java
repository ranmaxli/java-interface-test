package api.user;

import com.github.yongchristophertang.engine.web.annotations.Host;
import com.github.yongchristophertang.engine.web.annotations.POST;
import com.github.yongchristophertang.engine.web.annotations.Path;
import com.github.yongchristophertang.engine.web.annotations.QueryParam;
import com.github.yongchristophertang.engine.web.request.RequestBuilder;


/**
 * Created by liran on 2017/3/1
 */
@Host(value = "127.0.0.1", port = 8080)
@Path("/user")
public interface CoreAPI {

    @POST
    @Path("/login")
    RequestBuilder login(@QueryParam("username") String username,
                                   @QueryParam("password") String password);

    @POST
    @Path("/insertUser")
    RequestBuilder insertUser(@QueryParam("username") String username,
                                   @QueryParam("password") String password,
                                   @QueryParam("displayName") String displayName
    );

    @POST
    @Path("/updatePassword")
    RequestBuilder updatePassword(@QueryParam("username") String username,
                                  @QueryParam("password") String password,
                                  @QueryParam("oldPassword") String oldPassword
    );

    @POST
    @Path("/deleteUser")
    RequestBuilder deleteUser(@QueryParam("username") String username,
                              @QueryParam("password") String password,
                              @QueryParam("displayName") String displayName
    );

}



