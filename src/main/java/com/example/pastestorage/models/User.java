package com.example.pastestorage.models;

import com.example.pastestorage.types.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "service_user")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
    @Id
    String username;
    String password;
    @Column(name = "birth_date")
    Date birthDate;
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    UserRole userRole;
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @Filter(name = "expiredFilter", condition = "expire_date > :currentTime")
    private List<Paste> pastes;
}
