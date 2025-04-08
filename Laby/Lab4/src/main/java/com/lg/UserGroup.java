package com.lg;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
            @JoinTable(
                    name = "user_groups",
                    joinColumns = @JoinColumn(name = "User_id"),
                    inverseJoinColumns = @JoinColumn(name = "UserGroup_id")
            )
    List<User> users = new ArrayList<>();
}
