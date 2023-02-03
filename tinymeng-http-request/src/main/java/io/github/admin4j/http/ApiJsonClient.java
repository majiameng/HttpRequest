package io.github.tinymeng.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.tinymeng.http.core.*;
import io.github.tinymeng.http.exception.HttpException;
import io.github.tinymeng.http.factory.HttpClientFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author tinymeng
 * @since 2022/10/9 16:53
 */
public class ApiJsonClient extends AbstractHttpRequest {

    public ApiJsonClient() {
        super();
    }

    public ApiJsonClient(HttpConfig config) {
        okHttpClient = HttpClientFactory.okHttpClient(config);
        headerMap.put(HttpHeaderKey.USER_AGENT, config.getUserAgent());
        headerMap.put(HttpHeaderKey.REFERER, config.getReferer());
        this.followRedirects = config.isFollowRedirects();
    }


    @Override
    public String serializeJSON(Object obj) {
        return JSON.toJSONString(obj);
    }

    public <T> T deserializeJSON(InputStream in, Class<T> tClass) throws IOException {
        return JSON.parseObject(in, charset, tClass);
    }

    public <T> T execute(Call call, Class<T> tClass) throws HttpException {
        try {
            Response response = call.execute();
            return handleResponse(response, tClass);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }


    public <T> void executeAsync(Call call, final Class<T> tClass, final HttpCallback<T> callback) {

        call.enqueue(new Callback() {


            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                T result;
                try {
                    result = (T) handleResponse(response, tClass);
                } catch (Exception e) {
                    callback.onFailure(e, response.code(), response.headers().toMultimap());
                    return;
                }
                callback.onSuccess(result, response.code(), response.headers().toMultimap());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(e, 0, null);
            }
        });
    }


    // ========== handleResponse =============

    /**
     * Handle the given response, return the deserialized object when the response is successful.
     *
     * @param <T>      Type
     * @param response Response
     * @param tClass   Return type
     * @return Type
     * @throws HttpException If the response has a unsuccessful status code or
     *                       fail to deserialize the response body
     */
    protected <T> T handleResponse(Response response, Class<T> tClass) throws HttpException {

        response = handleResponse(response);

        if (response.isSuccessful()) {
            try {
                if (tClass == null) {
                    // returning null if the returnType is not defined,
                    // or the status code is 204 (No Content)

                    if (response.body() != null || response.code() == 204) {
                        response.body().close();
                    }
                    return null;
                } else {
                    return handleSuccessResponse(response, tClass);
                }
            } catch (IOException e) {
                throw new HttpException(response.message(), e, response.code(), response.headers().toMultimap());
            }
        } else {

            T o = handleFailResponse(response, tClass);
            if (o != null) {
                return o;
            }
            String respBody = null;
            if (response.body() != null) {
                try {
                    respBody = response.body().string();

                } catch (IOException e) {
                    throw new HttpException(response.message(), e, response.code(), response.headers().toMultimap());
                }
            }
            throw new HttpException(response.message(), response.code(), response.headers().toMultimap(), respBody);
        }
    }

    /**
     * 服务器返回成功，解析成JSON
     *
     * @param response
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T handleSuccessResponse(Response response, Class<T> tClass) throws IOException {
        return deserializeJSON(response.body().byteStream(), tClass);
    }

    /**
     * 服务器返回S失败，解析成失败的，或者抛出错误
     *
     * @param response
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T handleFailResponse(Response response, Class<T> tClass) {
        return null;
    }

    protected JSONObject serialize(Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            throw new HttpException("response body is null");
        }
        try {
            return JSONObject.parseObject(body.string());
        } catch (IOException e) {

            throw new HttpException(e);
        }
    }

    protected JSONArray serializeList(Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            throw new HttpException("response body is null");
        }
        try {
            return JSON.parseArray(body.string());
        } catch (IOException e) {

            throw new HttpException(e);
        }
    }

    //=============== request ===============
    public <T> T get(String path, Class<T> tClass, Pair<?>... queryParams) {
        Call call = buildGet(path, null, queryParams);
        return execute(call, tClass);
    }

    public <T> T get(String path, Map<String, Object> queryMap, Class<T> tClass) {
        Call call = buildGet(path, queryMap, (Pair<?>) null);
        return execute(call, tClass);
    }


    public <T> T postForm(String url, Map<String, Object> formParams, Class<T> tClass) {

        Response response = post(url, MediaTypeEnum.FORM, null, formParams, null);
        return handleResponse(response, tClass);
    }

    public <T> T postFormData(String url, Map<String, Object> formParams, Class<T> tClass) {

        Response response = post(url, MediaTypeEnum.FORM_DATA, null, formParams, null);
        return handleResponse(response, tClass);
    }

    public <T> T post(String url, Object body, Class<T> tClass) {

        Response response = post(url, MediaTypeEnum.JSON, body, null, null);
        return handleResponse(response, tClass);
    }


    public JSONObject get(String path, Pair<?>... queryParams) {
        Response response = get(path, (Map<String, Object>) null, queryParams);
        return handleResponse(response, JSONObject.class);
    }

    public JSONObject get(String path, Map<String, Object> queryMap) {
        Response response = get(path, queryMap, (Pair<?>[]) null);
        return handleResponse(response, JSONObject.class);
    }

    public JSONArray getList(String path, Pair<?>... queryParams) {
        Response response = get(path, (Map<String, Object>) null, queryParams);
        return handleResponse(response, JSONArray.class);
    }

    public JSONArray getList(String path, Map<String, Object> queryMap) {
        Response response = get(path, queryMap, (Pair<?>[]) null);
        return handleResponse(response, JSONArray.class);
    }


    public JSONObject postForm(String url, Map<String, Object> formParams) {

        Response response = post(url, MediaTypeEnum.FORM, null, formParams, null);
        return handleResponse(response, JSONObject.class);
    }

    public JSONObject postFormData(String url, Map<String, Object> formParams) {

        Response response = post(url, MediaTypeEnum.FORM_DATA, null, formParams, null);
        return handleResponse(response, JSONObject.class);
    }

    public JSONObject post(String url, Object body) {

        Response response = post(url, MediaTypeEnum.JSON, body, null, null);
        return handleResponse(response, JSONObject.class);
    }
}
