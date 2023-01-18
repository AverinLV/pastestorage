package com.example.pastestorage.models;

import com.example.pastestorage.types.AccessType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "paste")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Paste {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "text_data")
    String textData;

    @Column(name = "expire_date")
    Instant expireDate;

    @Column(name = "access_type")
    @Enumerated(EnumType.STRING)
    AccessType accessType;

}
