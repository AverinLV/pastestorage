package com.example.pastestorage.models;

import com.example.pastestorage.types.AccessType;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "paste")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Paste {
    @Id
    @Column(name = "hash_code")
    String hashCode;

    @Column(name = "text_data")
    String textData;

    @Column(name = "expire_date")
    Timestamp expireDate;

    @Column(name = "access_type")
    @Enumerated(EnumType.STRING)
    AccessType accessType;

}
