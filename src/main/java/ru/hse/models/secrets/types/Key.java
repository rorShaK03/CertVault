package ru.hse.models.secrets.types;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ru.hse.models.secrets.Secret;

@Entity
@Table(name = "_keys")
@NoArgsConstructor
public class Key extends Secret {
    public Key(String secret) {
        super(secret);
    }
}
