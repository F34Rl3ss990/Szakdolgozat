package com.EGEA1R.CarService.entity;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "e-mail")
})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "user_id")
    private Long user_id;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]")
    @Size(max = 60)
    @Column(name = "first_name")
    private String first_name;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]")
    @Size(max = 40)
    @Column(name = "last_name")
    private String last_name;

    @NotNull
    @ValidEmail
    @Column(name = "e-mail")
    private String email;

    @NotNull
    @Pattern(regexp = "^{11}[0-9]")
    @Size(max = 11)
    @Column(name = "phone_number")
    private Integer phone_number;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_credential")
    private Credential credential;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Car> car = new HashSet<>();
}
