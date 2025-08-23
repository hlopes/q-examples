package org.lopes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class MonthlyRevenue extends PanacheEntity {
    public String period; // e.g., "2023-01"
    public double revenue;
}
