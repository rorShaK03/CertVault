package ru.hse.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_rights")
public class Right {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "secret_id")
    private UUID secretId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    public Right(UUID userId, UUID secretId, Role role) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.secretId = secretId;
        this.role = role;
    }
}
