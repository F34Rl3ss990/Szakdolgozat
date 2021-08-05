package com.car_service.egea1r.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification")
@SqlResultSetMapping(
        name="getFkAndExpDateMapping",
        classes = {
                @ConstructorResult(
                        targetClass = Verification.class,
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
                        targetClass = Verification.class,
                        columns = {
                                @ColumnResult(name="verification_id", type = Long.class)
                        }
                )
        }
)
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id", insertable = false, updatable = false)
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
    @ToString.Exclude
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Verification that = (Verification) o;

        return Objects.equals(verificationId, that.verificationId);
    }

    @Override
    public int hashCode() {
        return 1780544339;
    }
}
