package io.github.tinymeng.http;

import com.alibaba.fastjson.JSON;
import io.github.tinymeng.http.core.*;
import io.github.tinymeng.http.factory.HttpClientFactory;
import lombok.experimental.Accessors;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.InputStream;
import java.util.Map;

/**
 * @author tinymeng
 * @since 2022/4/21 11:27
 */
@Accessors
public class ApiClient extends AbstractHttpRequest {
    public ApiClient() {
        super();
    }

    public ApiClient(HttpConfig httpConfig) {
        okHttpClient = HttpClientFactory.okHttpClient(httpConfig);
        this.followRedirects = httpConfig.isFollowRedirects();
        headerMap.put(HttpHeaderKey.USER_AGENT, httpConfig.getUserAgent());
        headerMap.put(HttpHeaderKey.REFERER, httpConfig.getReferer());
    }

    @Override
    public String serializeJSON(Object obj) {
        return JSON.toJSONString(obj);
    }


    //=============== request ===============
    public Response get(String path, Map<String, Object> queryMap) {
        return get(path, queryMap, null);
    }

    public Response get(String path, Pair<?>... queryParams) {
        return get(path, null, queryParams);
    }


    public Response postForm(String url, Map<String, Object> formParams) {

        return post(url, MediaTypeEnum.FORM, null, formParams, null);
    }

    public void asyncPostForm(String url, Map<String, Object> formParams, Callback callback) {

        asyncPost(url, MediaTypeEnum.FORM, null, formParams, null, callback);
    }

    public Response postFormData(String url, Map<String, Object> formParams) {

        return post(url, MediaTypeEnum.FORM_DATA, null, formParams, null);
    }

    public void asyncPostFormData(String url, Map<String, Object> formParams, Callback callback) {

        asyncPost(url, MediaTypeEnum.FORM_DATA, null, formParams, null, callback);
    }

    public Response post(String url, Object body) {

        return post(url, MediaTypeEnum.JSON, body, null, null);
    }

    public void asyncPost(String url, Object body, Callback callback) {

        asyncPost(url, MediaTypeEnum.JSON, body, null, null, callback);
    }

    public InputStream down(String url) {
        return executeByteStream(url, Method.GET, null, null, null, null, null, null);
    }
}
