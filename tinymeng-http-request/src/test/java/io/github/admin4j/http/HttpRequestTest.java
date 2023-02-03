package io.github.tinymeng.http;

import io.github.tinymeng.http.core.HttpHeaderKey;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

/**
 * @author tinymeng
 * @since 2022/11/8 16:23
 */
class HttpRequestTest {

    @Test
    void get() {
        Response response = HttpRequest.get("https://search.gitee.com/?skin=rec&type=repository")
                .queryMap("q", "tinymeng")
                .header(HttpHeaderKey.USER_AGENT, "tinymeng")
                .execute();
        System.out.println("response = " + response);
    }

    @Test
    void post() {
        Response response = HttpRequest.get("http://192.168.1.13:9100/auth/login")
                .queryMap("q", "tinymeng")
                .header(HttpHeaderKey.USER_AGENT, "tinymeng")
                .form("username", "admin")
                .form("password", "admin123")
                .execute();
        System.out.println("response = " + response);
    }
}