package com.example.pastestorage.models;

import com.example.pastestorage.types.AccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "paste")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@FilterDef(
        name = "expiredFilter",
        parameters = @ParamDef(name = "currentTime", type = "Instant")
)
@Filter(name = "expiredFilter", condition = "expire_date > :currentTime")
public class Paste {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID id;

    @Column(name = "text_data")
    String textData;

    @Column(name = "expire_date")
    Instant expireDate;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "access_type")
    @Enumerated(EnumType.STRING)
    AccessType accessType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
