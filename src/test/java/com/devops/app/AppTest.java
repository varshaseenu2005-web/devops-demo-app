package com.devops.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void greetingUsesDefaultName() {
        assertTrue(App.greeting(null).startsWith("Hello, DevOps!"));
    }

    @Test
    void greetingUsesGivenName() {
        assertTrue(App.greeting("Varsha").startsWith("Hello, Varsha!"));
    }

    @Test
    void versionIsSet() {
        assertEquals("1.0.0", App.VERSION);
    }
}
