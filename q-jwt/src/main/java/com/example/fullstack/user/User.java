package com.example.fullstack.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {
    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    public Long id;

    @Column(unique = true, nullable = false)
    public String name;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    public ZonedDateTime created;
    @Version
    public int version;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role")
    public List<String> roles;
    @Column(nullable = false)
    String password;

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }
}
