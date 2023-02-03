package io.github.tinymeng.http.config;

import io.github.tinymeng.http.ApiClient;
import io.github.tinymeng.http.ApiJsonClient;
import io.github.tinymeng.http.HttpPropsConfiguration;
import io.github.tinymeng.http.core.HttpDefaultConfig;
import io.github.tinymeng.http.util.HttpJsonUtil;
import io.github.tinymeng.http.util.HttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * @author tinymeng
 * @since 2022/11/3 15:27
 */
@EnableConfigurationProperties(HttpPropsConfiguration.class)
@ConditionalOnProperty(prefix = "tinymeng.http", name = "enabled", matchIfMissing = true)
public class HttpAutoConfiguration implements InitializingBean {
    @Autowired
    HttpPropsConfiguration httpConfig;

    @PostConstruct
    public void initHttpClient() {

        HttpDefaultConfig.set(httpConfig);
        ApiClient apiClient = new ApiClient(httpConfig);
        HttpUtil.setClient(apiClient);
        ApiJsonClient apiJsonClient = new ApiJsonClient(httpConfig);
        HttpJsonUtil.setClient(apiJsonClient);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpDefaultConfig.set(httpConfig);
    }
}
