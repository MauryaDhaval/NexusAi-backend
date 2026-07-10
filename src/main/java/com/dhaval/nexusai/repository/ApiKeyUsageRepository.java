package com.dhaval.nexusai.repository;

import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.ApiKeyUsage;
import com.dhaval.nexusai.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiKeyUsageRepository extends JpaRepository<ApiKeyUsage , Long> {

    Long countByApiKey (ApiKey apiKey);

    long countByApiKeyAndStatusCode (ApiKey apiKey , Integer statusCode);

    List<ApiKeyUsage> findTop10ByApiKeyOrderByCreatedAtDesc (ApiKey apiKey);

    @Query("""
       SELECT AVG(a.responseTime)
       FROM ApiKeyUsage a
       WHERE a.apiKey = :apiKey
       """)
    Double findAverageResponseTime(ApiKey apiKey);

    // Counts by traversing: ApiKeyUsage -> ApiKey -> User
    Long countByApiKey_User(User user);

    // Counts success calls by traversing and matching status code
    Long countByApiKey_UserAndStatusCode(User user, Integer statusCode);

    @Query("SELECT AVG(u.responseTime) FROM ApiKeyUsage u WHERE u.apiKey.user = :user")
    Double findAverageResponseTimeByUser(@Param("user") User user);

    // Finds top 10 logs ordered by creation timestamp
    List<ApiKeyUsage> findTop10ByApiKey_UserOrderByCreatedAtDesc(User user);

}
