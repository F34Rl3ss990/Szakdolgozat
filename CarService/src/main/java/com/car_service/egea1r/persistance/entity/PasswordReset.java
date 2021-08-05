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
@Table(name = "passwordreset")
@SqlResultSetMapping(
        name="GetExpDateByToken",
        classes = {
                @ConstructorResult(
                        targetClass = PasswordReset.class,
                        columns = {
                                @ColumnResult(name="expiry_date", type = Date.class)
                        }
                )
        }
)
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passwordreset_id", insertable = false, updatable = false)
    private Long passwordResetId;

    @Column(name = "token")
    @javax.validation.constraints.NotNull
    private String token;

    @Column(name = "expiry_date")
    @NotNull
    private Date expiryDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "fk_passwordreset_credential_id")
    private Credential credential;

    public PasswordReset(Date expiryDate){
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PasswordReset that = (PasswordReset) o;

        return Objects.equals(passwordResetId, that.passwordResetId);
    }

    @Override
    public int hashCode() {
        return 627565262;
    }
}
