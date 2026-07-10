package com.dhaval.nexusai.repository;

import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository <Message , Long> {
    List<Message> findTop20ByApiKeyOrderByCreatedAtDesc(ApiKey apiKey);
}
