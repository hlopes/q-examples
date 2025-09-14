package org.hlopes.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedEntityGraph(name = "Car.withBrandAndDealership", attributeNodes = {
        @NamedAttributeNode("brand"), @NamedAttributeNode("dealership")
})
public class Car extends PanacheEntity {
    public String model;

    @ManyToOne(fetch = FetchType.LAZY)
    public Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    public Brand dealership;

    public Integer productionYear;
    public String color;
    public BigDecimal price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "car_features", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "feature")
    public Set<String> features = new HashSet<>();
}
