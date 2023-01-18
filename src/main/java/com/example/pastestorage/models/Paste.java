package com.example.pastestorage.models;

import com.example.pastestorage.types.AccessType;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "paste")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Paste {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID id;

    @Column(name = "text_data")
    String textData;

    @Column(name = "expire_date")
    Instant expireDate;

    @Column(name = "access_type")
    @Enumerated(EnumType.STRING)
    AccessType accessType;

}
