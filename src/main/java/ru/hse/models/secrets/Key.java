package ru.hse.models.secrets;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "_keys")
@NoArgsConstructor
public class Key implements Versionable{
    @Column(name = "id")
    protected UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "version_id")
    protected UUID versionId;

    @Column(name = "secret")
    @Setter
    protected String secret;

    public Key(String secret) {
        this.secret = secret;
        this.id = UUID.randomUUID();
    }
}
