package com.example.stocks.Config;

import com.example.stocks.Model.Portfolio;
import com.example.stocks.Respository.PortfolioRepository;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class appConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public ApplicationRunner createDeafultPortfolio(PortfolioRepository portfolioRepository){
        return args -> {
            if (portfolioRepository.count() == 0){
                Portfolio portfolio = new Portfolio();
                portfolio.setName("MyPortfolio");
                portfolioRepository.save(portfolio);
            }
        };
    }
/*
    @Bean
    public CacheManager caffeineCacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(1000)
        );
        return cacheManager;
    }*/

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory){

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

  /*  @Bean
    @Primary
    public CacheManager cacheManager(
            CaffeineCacheManager caffeineCacheManager,
            RedisCacheManager redisCacheManager){

        CompositeCacheManager cacheManager =
                new CompositeCacheManager(caffeineCacheManager, redisCacheManager);

        cacheManager.setFallbackToNoOpCache(false);

        return cacheManager;
    }*/

    @Bean(name = "marketDataExecutor")
    public Executor marketDataExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("market-data-");
        executor.initialize();
        return executor;
    }
}
