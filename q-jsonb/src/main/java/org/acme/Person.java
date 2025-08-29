package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;


@Entity
public class Person extends PanacheEntity {

    public String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private MyJson json;

    public MyJson getJson() {
        return json;
    }

    public void setJson(MyJson json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "Person{" +
            "name='" + name + '\'' +
            ", json=" + json.toString() +
            '}';
    }
}
