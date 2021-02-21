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
@SqlResultSetMapping(
        name="GetExpDateByToken",
        classes = {
                @ConstructorResult(
                        targetClass = com.EGEA1R.CarService.persistance.entity.PasswordReset.class,
                        columns = {
                                @ColumnResult(name="expiry_date", type = Date.class)
                        }
                )
        }
)
@Table(name = "passwordreset")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "passwordreset_id")
    private Long passwordResetId;

    @Column(name = "token")
    @NotNull
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

}