package com.ecommerce.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = {"relationshipFields"}) // Exclude lazy/circular fields
@EqualsAndHashCode(exclude = {"relationshipFields"}) // Exclude collections
public class Address {
    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode + ", " + country;
    }
}