package com.lg;

import com.sun.xml.bind.v2.model.core.ID;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Role> roles = new ArrayList<>();

    @ManyToMany
    List<UserGroup> groups = new ArrayList<>();

    @Lob
    @Column(length = 100000)
    private byte[] image;
    public enum Sex {
        MALE,
        FEMALE
    }
}
