package org.hlopes.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.hlopes.dtos.request.CarFilter;
import org.hlopes.dtos.response.FilterOptions;
import org.hlopes.dtos.response.PagedResult;
import org.hlopes.entity.Brand;
import org.hlopes.entity.Car;
import org.hlopes.entity.Dealership;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    private final EntityManager entityManager;

    public CarRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public FilterOptions getFilterOptions() {
        List<Brand> brands = Brand.listAll(Sort.by("name"));
        List<Dealership> dealerships = Dealership.listAll(Sort.by("name"));

        var colors = entityManager
                .createQuery("SELECT DISTINCT c.color FROM Car c ORDER BY c.color", String.class)
                .getResultList();
        var features = entityManager
                .createQuery("SELECT DISTINCT f FROM Car c JOIN c.features f ORDER BY f", String.class)
                .getResultList();

        return new FilterOptions(brands, dealerships, colors, features);
    }

    public PagedResult<Car> search(final CarFilter filter, int pageIndex, int pageSize) {
        var queryBuilder = new StringBuilder();
        var params = new Parameters();

        addCondition(filter.brandIds, "brand.id IN :brandIds", "brandIds", queryBuilder, params);
        addCondition(filter.dealershipIds, "dealership.id IN :dealershipIds", "dealershipIds", queryBuilder, params);
        addCondition(filter.colors, "color IN :colors", "colors", queryBuilder, params);
        addCondition(
                filter.features,
                "id IN (SELECT c.id FROM Car c JOIN c.features f WHERE f IN :features)",
                "features",
                queryBuilder,
                params);

        addCondition(filter.minYear, "productionYear >= :minYear", "minYear", queryBuilder, params);
        addCondition(filter.maxYear, "productionYear <= :maxYear", "maxYear", queryBuilder, params);
        addCondition(filter.minPrice, "price >= :minPrice", "minPrice", queryBuilder, params);
        addCondition(filter.maxPrice, "price <= :maxPrice", "maxPrice", queryBuilder, params);

        var conditions = !queryBuilder.isEmpty() ? queryBuilder.substring(4) : "1=1";
        var totalCount = find(conditions, params).count();
        var pagedCars = find(conditions, params).page(pageIndex, pageSize).list();

        if (pagedCars.isEmpty()) {
            return new PagedResult<>(Collections.emptyList(), 0);
        }

        var carIds = pagedCars.stream().map(car -> car.id).toList();
        var cars = find("id IN ?1", carIds)
                .withHint(
                        "jakarta.persistence.fetchgraph",
                        entityManager.getEntityGraph("Car" + ".withBrandAndDealership"))
                .list();

        return new PagedResult<>(cars, totalCount);
    }

    private void addCondition(
            final Object value,
            final String clause,
            final String paramName,
            final StringBuilder queryBuilder,
            final Parameters params) {
        if (value instanceof Collection<?> && !((Collection<?>) value).isEmpty() || value != null) {
            queryBuilder.append("AND ").append(clause).append(" ");
            params.and(paramName, value);
        }
    }
}
