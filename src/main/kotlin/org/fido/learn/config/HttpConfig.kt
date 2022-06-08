package org.fido.learn.config

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
@Configuration
class HttpConfig {
    @Bean
    fun pool(): ConnectionPool {
        return ConnectionPool(200, 1, TimeUnit.MINUTES)
    }

    @Bean
    fun okHttpClient(): OkHttpClient? {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .connectionPool(pool())
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .build()
    }
}