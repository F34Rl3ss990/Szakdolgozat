package com.car_service.egea1r.persistance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CarMileage {

    @NotNull
    private String mileage;

    private Date dateOfSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CarMileage that = (CarMileage) o;

        if (!Objects.equals(mileage, that.mileage)) return false;
        return Objects.equals(dateOfSet, that.dateOfSet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(mileage);
        result = 31 * result + (Objects.hashCode(dateOfSet));
        return result;
    }
}
