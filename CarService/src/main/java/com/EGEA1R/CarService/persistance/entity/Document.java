package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javassist.bytecode.ByteArray;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@SqlResultSetMapping(
        name="GetDocuments",
        entities = {
                @EntityResult(entityClass = com.EGEA1R.CarService.persistance.entity.Document.class)
        }
)
@SqlResultSetMapping(
        name="GetDocumentForDownload",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Document.class,
                        columns = {
                                @ColumnResult(name="name", type = String.class),
                                @ColumnResult(name="file_type", type = String.class),
                                @ColumnResult(name="location", type = String.class),
                                @ColumnResult(name="upload_time", type = LocalDate.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetDocumentsToShow",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.web.DTO.DocumentDTO.class,
                        columns = {

                                @ColumnResult(name="file_id", type = Long.class),
                                @ColumnResult(name="file_type", type = String.class),
                                @ColumnResult(name="upload_time", type = Date.class),
                                @ColumnResult(name="name", type = String.class),
                                @ColumnResult(name="size", type = String.class),
                                @ColumnResult(name="location", type = String.class),
                                @ColumnResult(name="fk_document_car", type = Long.class),
                                @ColumnResult(name="fk_document_service_data", type = Long.class),
                                @ColumnResult(name="brand", type = String.class),
                                @ColumnResult(name="type", type = String.class),
                                @ColumnResult(name="license_plate_number", type = String.class),
                                @ColumnResult(name="mileage", type = String.class)
                        }
                )
        }
)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "file_id", insertable = false, updatable = false)
    private Long fileId;

    @NotNull
    @Column(name = "file_type")
    private String fileType;

    @NotNull
    @Column(name = "name")
    private String name;


    @NotNull
    @CreationTimestamp
    @Column(name = "upload_time")
    private LocalDate uploadTime;

    @Column(name = "size")
    private String size;

    @NotNull
    @Column(name = "location")
    private String location;

    @Column(name = "fk_document_car", updatable = false, insertable = false)
    private Long fkDocumentCarId;

    @Column(name = "fk_document_service_data", updatable = false, insertable = false)
    private Long fkDocumentServiceData;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_document_car")
    private Car car;

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_document_service_data")
    private ServiceData serviceData;

    public Document(String name, String fileType, String location, LocalDate uploadTime){
        this.name = name;
        this.fileType = fileType;
        this.location = location;
        this.uploadTime = uploadTime;

    }

}
