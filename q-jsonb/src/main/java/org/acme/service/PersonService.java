package org.acme.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.acme.MyJson;
import org.acme.Person;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class PersonService {
    EntityManager entityManager;
    
    private AtomicLong counter = new AtomicLong(0);
    private Random random = new Random();

    public PersonService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Retry example - will retry up to 3 times with a 200ms delay if an exception occurs
     */
    @Retry(maxRetries = 3, delay = 200, delayUnit = ChronoUnit.MILLIS)
    public List<Person> getAllPersonsWithRetry() {
        // Simulate occasional failure
        if (random.nextInt(4) == 0) {
            System.out.println("Simulating database connection failure...");
            throw new RuntimeException("Database connection failed");
        }
        
        return Person.listAll();
    }

    /**
     * Timeout example - will throw TimeoutException if execution takes longer than 500ms
     */
    @Timeout(value = 500, unit = ChronoUnit.MILLIS)
    public List<Person> getAllPersonsWithTimeout() {
        // Simulate occasional slow response
        try {
            if (random.nextInt(4) == 0) {
                System.out.println("Simulating slow database query...");
                Thread.sleep(700); // This will cause a timeout
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return Person.listAll();
    }

    /**
     * Circuit Breaker example - will open circuit after 3 failures, preventing further calls
     * for 1 second, then allowing a test call to see if the system has recovered
     */
    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 1000,
        delayUnit = ChronoUnit.MILLIS,
        successThreshold = 1
    )
    public List<Person> getAllPersonsWithCircuitBreaker() {
        // Simulate system under stress
        long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 >= 2) {
            System.out.println("Simulating service failure...");
            throw new RuntimeException("Service unavailable");
        }
        
        return Person.listAll();
    }

    /**
     * Fallback example - provides alternative result when the primary method fails
     */
    @Fallback(fallbackMethod = "fallbackGetPerson")
    public Person getPersonById(Long id) {
        // Simulate occasional failure
        if (random.nextInt(3) == 0) {
            System.out.println("Simulating failure in getPersonById...");
            throw new RuntimeException("Failed to get person");
        }
        
        return Person.findById(id);
    }
    
    public Person fallbackGetPerson(Long id) {
        // Return a default person when the main method fails
        Person fallbackPerson = new Person();
        fallbackPerson.name = "Fallback Person";
        // Create a simple JSON structure
        MyJson json = new org.acme.MyJson();
        json.setValue("fallback-value");
        fallbackPerson.setJson(json);
        return fallbackPerson;
    }

    /**
     * Bulkhead example - limits concurrent calls to 2 with a queue size of 2
     */
    @Bulkhead(value = 2, waitingTaskQueue = 2)
    public List<Person> getAllPersonsWithBulkhead() {
        // Simulate a time-consuming operation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return Person.listAll();
    }
}