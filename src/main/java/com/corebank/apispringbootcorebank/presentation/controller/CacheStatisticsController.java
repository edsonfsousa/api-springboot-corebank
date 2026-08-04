package com.corebank.apispringbootcorebank.presentation.controller;

import com.corebank.apispringbootcorebank.infrastructure.observability.BalanceMetrics;
import com.corebank.apispringbootcorebank.presentation.dto.response.CacheStatisticsResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/internal/cache-statistics")
public class CacheStatisticsController {

    private final MeterRegistry meterRegistry;

    public CacheStatisticsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    public CacheStatisticsResponse statistics() {
        double hits = cacheAccessCount("hit");
        double misses = cacheAccessCount("miss");
        double totalCacheRequests = hits + misses;

        double hitRatio = totalCacheRequests == 0
                ? 0
                : hits / totalCacheRequests * 100;

        Timer redisTimer = meterRegistry
                .find(BalanceMetrics.REDIS_LATENCY_METRIC)
                .tag(
                        "operation",
                        BalanceMetrics.FIND_BALANCE_OPERATION
                )
                .timer();

        Timer databaseTimer = meterRegistry
                .find(BalanceMetrics.DATABASE_LATENCY_METRIC)
                .tag(
                        "operation",
                        BalanceMetrics.FIND_BALANCE_OPERATION
                )
                .timer();

        Counter databaseCounter = meterRegistry
                .find(BalanceMetrics.DATABASE_QUERY_METRIC)
                .tag(
                        "operation",
                        BalanceMetrics.FIND_BALANCE_OPERATION
                )
                .counter();

        Counter cacheErrorCounter = meterRegistry
                .find(BalanceMetrics.CACHE_ERROR_METRIC)
                .tag(
                        "operation",
                        BalanceMetrics.FIND_BALANCE_OPERATION
                )
                .counter();

        Counter invalidCacheValueCounter = meterRegistry
                .find(BalanceMetrics.CACHE_INVALID_VALUE_METRIC)
                .counter();

        return new CacheStatisticsResponse(
                (long) hits,
                (long) misses,
                round(hitRatio),
                meanInMilliseconds(redisTimer),
                meanInMilliseconds(databaseTimer),
                count(databaseCounter),
                (long) totalCacheRequests,
                count(cacheErrorCounter),
                count(invalidCacheValueCounter)
        );
    }

    private double cacheAccessCount(String result) {
        Counter counter = meterRegistry
                .find(BalanceMetrics.CACHE_ACCESS_METRIC)
                .tag("result", result)
                .counter();

        return counter == null ? 0 : counter.count();
    }

    private long count(Counter counter) {
        return counter == null ? 0 : (long) counter.count();
    }

    private double meanInMilliseconds(Timer timer) {
        if (timer == null || timer.count() == 0) {
            return 0;
        }

        return round(timer.mean(TimeUnit.MILLISECONDS));
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
