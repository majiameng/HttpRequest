package io.github.tinymeng.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tinymeng
 * @since 2022/11/3 16:14
 */
@ConfigurationProperties(prefix = "tinymeng.http")
public class HttpPropsConfiguration extends io.github.tinymeng.http.core.HttpConfig {
    /**
     * 是否启用
     */
    private boolean enabled = true;
}
