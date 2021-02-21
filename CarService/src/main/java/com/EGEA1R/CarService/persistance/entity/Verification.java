package com.EGEA1R.CarService.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification")
@SqlResultSetMapping(
        name="getFkAndExpDateMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Verification.class,
                        columns = {
                                @ColumnResult(name="expiry_date", type = Date.class),
                                @ColumnResult(name="fk_verification_credential_id", type = Long.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name="GetVerificationId",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.Verification.class,
                        columns = {
                                @ColumnResult(name="verification_id", type = Long.class)
                        }
                )
        }
)
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "verification_id")
    private Long verificationId;

    @Column(name = "token")
    @NotNull
    private String token;

    @Column(name = "expiry_date")
    @NotNull
    private Date expiryDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "fk_verification_credential_id")
    private Credential credential;

    @Column(name = "fk_verification_credential_id", insertable = false, updatable = false)
    private Long fkVerificationId;

    public Verification(Date expiryDate, Long fkVerificationId){
        this.expiryDate = expiryDate;
        this.fkVerificationId = fkVerificationId;
    }

    public Verification(Long verificationId){
        this.verificationId = verificationId;
    }
}
