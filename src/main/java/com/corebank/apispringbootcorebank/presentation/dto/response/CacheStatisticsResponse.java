package com.corebank.apispringbootcorebank.presentation.dto.response;

public record CacheStatisticsResponse(
        long cacheHits,
        long cacheMisses,
        double hitRatioPercentage,
        double redisAverageLatencyMs,
        double postgresqlAverageLatencyMs,
        long databaseQueries,
        long totalCacheRequests,
        long cacheErrors,
        long invalidCacheValues
) {
}
