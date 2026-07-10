package com.dhaval.nexusai.entity;

import com.dhaval.nexusai.entity.types.MessageRoles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key")
    private ApiKey apiKey;

    @Column(nullable = false)
    private MessageRoles role;

    @Column(nullable = false , columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
