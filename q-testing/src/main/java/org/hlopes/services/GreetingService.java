package org.hlopes.services;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
    
    public String greet() {
        return "hello";
    }
}
