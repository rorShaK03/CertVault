package ru.hse.models.secrets;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
public abstract class Secret {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "version_id")
    @Getter
    protected UUID versionId;

    @Column(name = "secret")
    @Getter
    @Setter
    protected String secret;

    public Secret(String secret) {
        this.secret = secret;
    }
}
