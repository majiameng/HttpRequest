package http;

import com.alibaba.fastjson.JSONObject;
import http.entity.R;
import io.github.tinymeng.http.ApiClient;
import io.github.tinymeng.http.core.HttpConfig;
import io.github.tinymeng.http.core.Method;
import io.github.tinymeng.http.core.Pair;
import io.github.tinymeng.http.util.HttpJsonUtil;
import lombok.SneakyThrows;
import okhttp3.Call;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tinymeng
 * @since 2022/5/10 14:08
 */
class ApiClientTest {
    ApiClient httpRequest;

    @BeforeEach
    void init() {
        HttpConfig httpConfig = new HttpConfig();
        httpRequest = new ApiClient(httpConfig);
    }


    @Test
    void get() throws IOException {

        JSONObject object = HttpJsonUtil.get("https://github.com/search",
                Pair.of("q", "http"),
                Pair.of("username", "agonie201218"));
        System.out.println("object = " + object);

        //object = HttpJsonUtil.get("http://192.168.1.13:9100/system/dict/i18n",
        //        Pair.of("id", "介绍"),
        //        Pair.of("username", "agonie201218"));
        //System.out.println("s1 = " + object);

    }

    @Test
    void post() {
        JSONObject post = HttpJsonUtil.post("https://oapi.dingtalk.com/robot/send?access_token=37f5954ab60ea8b2e431ae9101b1289c138e85aa6eb6e3940c35ee13ff8b6335",
                "{\"msgtype\": \"text\",\"text\": {\"content\":\"【反馈提醒】我就是我, 是不一样的烟火\"}}");
        System.out.println("post = " + post);
    }

    @Test
    void postForm() {

        Map<String, Object> formParams = new HashMap<>(16);
        formParams.put("username", "admin");
        formParams.put("password", "admin123");
        JSONObject object = HttpJsonUtil.postForm("http://192.168.1.13:9100/auth/login",
                formParams
        );
        System.out.println("post = " + object);
    }

    @Test
    void postForm2() {

        Map<String, Object> formParams = new HashMap<>(16);
        formParams.put("username", "admin");
        formParams.put("password", "admin123");
        Object post = HttpJsonUtil.postForm("http://192.168.1.13:9100/auth/login",
                formParams,
                R.class
        );
        System.out.println("post = " + post);
    }

    @SneakyThrows
    @Test
    void getRouters() {
        Map<String, Object> headerParams = new HashMap<>(16);
        headerParams.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjU0NzU5MDAsInVzZXJJZCI6MX0.tcfGlq8kMfdM9RenGIfgd44osEzPslLX4dAaY9i16vc");
        String url = "http://192.168.1.13:9100/system/menu/getRouters";
        Call call = httpRequest.buildCall(url, Method.GET, null, null, headerParams);
        //   Object execute = httpRequest.execute(call, R.class);
        //System.out.println(execute);
    }


    //
    ////upload/1/test/1523977637482352642.jpg
    //
    @SneakyThrows
    @Test
    void download() {
        // String url = "http://192.168.1.12:9100/system/upload/test";

        String url = "https://img-blog.csdnimg.cn/20200709161622641.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Fnb25pZTIwMTIxOA==,size_16,color_FFFFFF,t_70";
        //HttpJsonUtil.down(url, "C:\\Users\\tinymeng\\Downloads\\");
        //ResponseBody execute = HttpUtil.getBuilder(s)
        //        .execute();
        //
        //@Cleanup InputStream inputStream = execute.byteStream();
        //@Cleanup BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        //@Cleanup FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\tinymeng\\Pictures\\12\\123.jpg");
        //byte[] b = new byte[1024];
        //while ((bufferedInputStream.read(b)) != -1) {
        //    fileOutputStream.write(b);// 写入数据
        //}
        //fileOutputStream.flush();
    }

    //
    @Test
    void upload() throws IOException {

        File file = new File("C:\\Users\\tinymeng\\Downloads\\Sql.txt");
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("key", "test");
        formParams.put("file", "file");
        formParams.put("token", "WXyUseb-D4sCum-EvTIDYL-mEehwDtrSBg-Zca7t:qgOcR2gUoKmxt-VnsNb657Oatzo=:eyJzY29wZSI6InpoYW56aGkiLCJkZWFkbGluZSI6MTY2NTMwNzUxNH0=");
        JSONObject xx = HttpJsonUtil.upload("https://upload.qiniup.com/", formParams);
        System.out.println(xx);
    }


}