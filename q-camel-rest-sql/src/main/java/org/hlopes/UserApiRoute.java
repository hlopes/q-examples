package org.hlopes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class UserApiRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        from("platform-http:/users?httpMethodRestrict=GET").to("sql:select * from users").marshal().json();
        from("platform-http:/users?httpMethodRestrict=GET").to("sql:select name, city from users order by name")
                .marshal()
                .json();
    }
}
