package community.provider;

import com.alibaba.fastjson.JSON;
import community.dto.AccessTokenDTO;
import community.dto.GiteeUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class GiteeProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
//                .url("https://github.com/login/oauth/access_token")
                .url("https://gitee.com/oauth/token")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
//            System.out.println(response.body().string());
//            String[] split = response.body().string().split("&");
//            System.out.println(split[0]);
//            String[] split1 = split[0].split("=");
//            System.out.println(split1[1]);
            String accessToken = response.body().string().split(",")[0].split("\"")[3];
//            System.out.println(accessToken);
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GiteeUser getUser(String accessToken){
//        OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String objString = response.body().string();
            GiteeUser user = JSON.parseObject(objString, GiteeUser.class);
//            System.out.println(user.getName());
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
