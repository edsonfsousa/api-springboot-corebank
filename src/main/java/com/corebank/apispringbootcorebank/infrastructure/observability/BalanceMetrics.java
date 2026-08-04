package com.corebank.apispringbootcorebank.infrastructure.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.Callable;

public class BalanceMetrics {

    public static final String CACHE_ACCESS_METRIC =
            "corebank.cache.access";

    public static final String CACHE_ERROR_METRIC =
            "corebank.cache.error";

    public static final String CACHE_INVALID_VALUE_METRIC =
            "corebank.cache.invalid.value";

    public static final String DATABASE_QUERY_METRIC =
            "corebank.database.query";

    public static final String REDIS_LATENCY_METRIC =
            "corebank.redis.latency";

    public static final String DATABASE_LATENCY_METRIC =
            "corebank.database.latency";

    public static final String FIND_BALANCE_OPERATION =
            "find_balance";

    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheErrorCounter;
    private final Counter invalidCacheValueCounter;
    private final Counter databaseQueryCounter;
    private final Timer redisLatencyTimer;
    private final Timer databaseLatencyTimer;

    public BalanceMetrics(MeterRegistry meterRegistry) {
        this.cacheHitCounter = Counter
                .builder(CACHE_ACCESS_METRIC)
                .description("Quantidade de cache hits nas consultas de saldo")
                .tag("result", "hit")
                .register(meterRegistry);

        this.cacheMissCounter = Counter
                .builder(CACHE_ACCESS_METRIC)
                .description("Quantidade de cache misses nas consultas de saldo")
                .tag("result", "miss")
                .register(meterRegistry);

        this.cacheErrorCounter = Counter
                .builder(CACHE_ERROR_METRIC)
                .description("Falhas de acesso ao Redis nas consultas de saldo")
                .tag("operation", FIND_BALANCE_OPERATION)
                .register(meterRegistry);

        this.invalidCacheValueCounter = Counter
                .builder(CACHE_INVALID_VALUE_METRIC)
                .description("Valores de saldo inválidos encontrados no Redis")
                .register(meterRegistry);

        this.databaseQueryCounter = Counter
                .builder(DATABASE_QUERY_METRIC)
                .description("Consultas de saldo executadas no PostgreSQL")
                .tag("operation", FIND_BALANCE_OPERATION)
                .register(meterRegistry);

        this.redisLatencyTimer = Timer
                .builder(REDIS_LATENCY_METRIC)
                .description("Latência das consultas de saldo no Redis")
                .tag("operation", FIND_BALANCE_OPERATION)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.databaseLatencyTimer = Timer
                .builder(DATABASE_LATENCY_METRIC)
                .description("Latência das consultas de saldo no PostgreSQL")
                .tag("operation", FIND_BALANCE_OPERATION)
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void recordCacheHit() {
        cacheHitCounter.increment();
    }

    public void recordCacheMiss() {
        cacheMissCounter.increment();
    }

    public void recordCacheError() {
        cacheErrorCounter.increment();
    }

    public void recordInvalidCacheValue() {
        invalidCacheValueCounter.increment();
    }

    public void recordDatabaseQuery() {
        databaseQueryCounter.increment();
    }

    public <T> T measureRedis(Callable<T> operation) {
        return record(redisLatencyTimer, operation);
    }

    public <T> T measureDatabase(Callable<T> operation) {
        return record(databaseLatencyTimer, operation);
    }

    private <T> T record(Timer timer, Callable<T> operation) {
        try {
            return timer.recordCallable(operation);
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to execute measured operation",
                    exception
            );
        }
    }
}
