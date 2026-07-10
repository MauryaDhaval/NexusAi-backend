package com.dhaval.nexusai.repository;

import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey , Long> {
    Optional<List<ApiKey>> findByUser (User user);
    Optional<ApiKey> findByKey (String key);
}
