package org.hlopes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

/**
 * Example JPA entity.
 *
 * To use it, get access to a JPA EntityManager via injection.
 *
 * {@code
 *
 * @Inject EntityManager em;
 *
 * public void doSomething() { MyEntity entity1 = new MyEntity(); entity1.field = "field-1"; em.persist(entity1);
 *
 * List<MyEntity> entities = em.createQuery("from MyEntity", MyEntity.class).getResultList(); } }
 */
@Entity
public class Greeting extends PanacheEntity {

    public String name;
    public String message;
}
