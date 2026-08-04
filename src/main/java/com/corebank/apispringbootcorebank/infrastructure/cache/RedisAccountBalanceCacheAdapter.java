package com.corebank.apispringbootcorebank.infrastructure.cache;

import com.corebank.apispringbootcorebank.domain.gateway.AccountBalanceCacheGateway;
import com.corebank.apispringbootcorebank.infrastructure.observability.BalanceMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
public class RedisAccountBalanceCacheAdapter
        implements AccountBalanceCacheGateway {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    RedisAccountBalanceCacheAdapter.class
            );

    private static final String KEY_PREFIX =
            "corebank:account:balance:";

    private final StringRedisTemplate redisTemplate;
    private final Duration accountBalanceTtl;
    private final BalanceMetrics balanceMetrics;

    public RedisAccountBalanceCacheAdapter(
            StringRedisTemplate redisTemplate,
            @Value("${application.cache.account-balance-ttl:5s}")
            Duration accountBalanceTtl,
            BalanceMetrics balanceMetrics
    ) {
        this.redisTemplate = redisTemplate;
        this.accountBalanceTtl = accountBalanceTtl;
        this.balanceMetrics = balanceMetrics;
    }

    @Override
    public Optional<BigDecimal> findBalance(UUID accountId) {
        String key = buildKey(accountId);

        try {
            String cachedBalance = balanceMetrics.measureRedis(
                    () -> redisTemplate
                            .opsForValue()
                            .get(key)
            );

            if (cachedBalance == null || cachedBalance.isBlank()) {
                balanceMetrics.recordCacheMiss();

                LOGGER.debug(
                        "Account balance cache miss. accountId={}",
                        accountId
                );

                return Optional.empty();
            }

            BigDecimal balance = new BigDecimal(cachedBalance);

            balanceMetrics.recordCacheHit();

            LOGGER.debug(
                    "Account balance cache hit. accountId={}",
                    accountId
            );

            return Optional.of(balance);
        } catch (NumberFormatException exception) {
            balanceMetrics.recordInvalidCacheValue();

            LOGGER.warn(
                    "Invalid account balance stored in Redis. " +
                            "The cache entry will be removed. accountId={}",
                    accountId,
                    exception
            );

            evict(accountId);

            return Optional.empty();
        } catch (DataAccessException exception) {
            balanceMetrics.recordCacheError();

            LOGGER.warn(
                    "Unable to read account balance from Redis. " +
                            "The application will fall back to PostgreSQL. accountId={}",
                    accountId,
                    exception
            );

            return Optional.empty();
        }
    }

    @Override
    public void saveBalance(
            UUID accountId,
            BigDecimal balance
    ) {
        String key = buildKey(accountId);

        try {
            redisTemplate
                    .opsForValue()
                    .set(
                            key,
                            balance.toPlainString(),
                            accountBalanceTtl
                    );

            LOGGER.debug(
                    "Account balance saved in Redis. accountId={}, ttl={}",
                    accountId,
                    accountBalanceTtl
            );
        } catch (DataAccessException exception) {
            LOGGER.warn(
                    "Unable to save account balance in Redis. " +
                            "The request will continue normally. accountId={}",
                    accountId,
                    exception
            );
        }
    }

    @Override
    public void evict(UUID accountId) {
        String key = buildKey(accountId);

        try {
            Boolean deleted = redisTemplate.delete(key);

            LOGGER.debug(
                    "Account balance cache eviction executed. " +
                            "accountId={}, deleted={}",
                    accountId,
                    deleted
            );
        } catch (DataAccessException exception) {
            LOGGER.warn(
                    "Unable to evict account balance from Redis. accountId={}",
                    accountId,
                    exception
            );
        }
    }

    private String buildKey(UUID accountId) {
        return KEY_PREFIX + accountId;
    }
}