package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document")
@EntityListeners(AuditingEntityListener.class)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "file_id", insertable = false, updatable = false)
    private Long fileId;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "name")
    private String name;


    @NotNull
    @CreationTimestamp
    @Column(name = "upload_time")
    private LocalDate uploadTime;

    @Lob
    @NotNull
    @Column(name = "data")
    private byte[] data;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_document_car")
    private Car car;

}
