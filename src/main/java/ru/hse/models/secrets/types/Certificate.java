package ru.hse.models.secrets.types;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ru.hse.models.secrets.Secret;

@Entity
@Table(name = "_certificates")
@NoArgsConstructor
public class Certificate extends Secret {
    public Certificate(String secret) {
        super(secret);
    }
}
