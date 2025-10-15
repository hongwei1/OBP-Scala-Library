package com.example;

import static org.junit.Assert.*;

import com.openbankproject.library.ObpScalaLibrary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Suite for OBP Scala Library Maven Integration
 *
 * This test class verifies that the OBP Scala Library works correctly
 * when integrated into a Maven project with proper dependency management.
 *
 * Test Coverage:
 * - Basic functionality (hello method)
 * - Version information retrieval
 * - Input validation and edge cases
 * - Java-Scala interoperability
 * - Performance characteristics
 */
public class ObpLibraryTest {

    private static final String EXPECTED_VERSION = "0.1.0-SNAPSHOT";
    private static final String TEST_USER = "TestUser";

    @Before
    public void setUp() {
        System.out.println("Setting up OBP Library test...");
    }

    @After
    public void tearDown() {
        System.out.println("OBP Library test completed.");
    }

    /**
     * Test basic hello functionality
     */
    @Test
    public void testHelloBasicFunctionality() {
        // Act
        String result = ObpScalaLibrary.hello(TEST_USER);

        // Assert
        assertNotNull("Result should not be null", result);
        assertFalse("Result should not be empty", result.isEmpty());
        assertTrue(
            "Result should contain user name",
            result.contains(TEST_USER)
        );
        assertTrue(
            "Result should contain library identifier",
            result.contains("OBP Scala Library")
        );
        assertTrue(
            "Result should start with 'Hello'",
            result.startsWith("Hello")
        );
    }

    /**
     * Test version information methods
     */
    @Test
    public void testVersionInformation() {
        // Act
        String versionMethod = ObpScalaLibrary.getVersion();
        String versionField = ObpScalaLibrary.version();

        // Assert
        assertNotNull("Version method should not return null", versionMethod);
        assertNotNull("Version field should not return null", versionField);
        assertFalse(
            "Version method should not be empty",
            versionMethod.isEmpty()
        );
        assertFalse(
            "Version field should not be empty",
            versionField.isEmpty()
        );
        assertEquals(
            "Both version accessors should return the same value",
            versionMethod,
            versionField
        );
        assertEquals(
            "Version should match expected value",
            EXPECTED_VERSION,
            versionMethod
        );
    }

    /**
     * Test with different input types
     */
    @Test
    public void testVariousInputTypes() {
        // Test with regular string
        String result1 = ObpScalaLibrary.hello("John Doe");
        assertTrue("Should handle regular names", result1.contains("John Doe"));

        // Test with single character
        String result2 = ObpScalaLibrary.hello("A");
        assertTrue("Should handle single character", result2.contains("A"));

        // Test with numbers in name
        String result3 = ObpScalaLibrary.hello("User123");
        assertTrue(
            "Should handle names with numbers",
            result3.contains("User123")
        );

        // Test with special characters
        String result4 = ObpScalaLibrary.hello("User@Company.com");
        assertTrue(
            "Should handle special characters",
            result4.contains("User@Company.com")
        );
    }

    /**
     * Test edge cases
     */
    @Test
    public void testEdgeCases() {
        // Test with empty string
        String result1 = ObpScalaLibrary.hello("");
        assertNotNull("Empty string should not return null", result1);
        assertTrue(
            "Empty string should still produce valid greeting",
            result1.contains("Hello")
        );

        // Test with whitespace
        String result2 = ObpScalaLibrary.hello("   ");
        assertNotNull("Whitespace should not return null", result2);
        assertTrue(
            "Whitespace should still produce valid greeting",
            result2.contains("Hello")
        );

        // Test with very long string
        String longName = "A".repeat(1000);
        String result3 = ObpScalaLibrary.hello(longName);
        assertNotNull("Long string should not return null", result3);
        assertTrue("Long string should be handled", result3.contains(longName));
    }

    /**
     * Test null input handling
     */
    @Test
    public void testNullInputHandling() {
        // The library handles null gracefully by converting it to string "null"
        String result = ObpScalaLibrary.hello(null);
        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should contain 'null' string",
            result.contains("null")
        );
        assertTrue(
            "Result should still be a valid greeting",
            result.contains("Hello")
        );
    }

    /**
     * Test method consistency
     */
    @Test
    public void testMethodConsistency() {
        String user = "ConsistencyTest";

        // Call the same method multiple times
        String result1 = ObpScalaLibrary.hello(user);
        String result2 = ObpScalaLibrary.hello(user);
        String result3 = ObpScalaLibrary.hello(user);

        // Results should be identical
        assertEquals(
            "Multiple calls should return identical results",
            result1,
            result2
        );
        assertEquals(
            "Multiple calls should return identical results",
            result2,
            result3
        );
    }

    /**
     * Test performance characteristics
     */
    @Test
    public void testPerformance() {
        int iterations = 1000;
        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            String result = ObpScalaLibrary.hello("PerformanceTest" + i);
            assertNotNull("Performance test result should not be null", result);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        long averageTime = totalTime / iterations;

        // Performance assertion (should complete in reasonable time)
        assertTrue(
            "Performance test should complete in reasonable time",
            totalTime < 10_000_000_000L
        ); // 10 seconds max

        System.out.println(
            String.format(
                "Performance: %d iterations in %d ms (avg: %d ns/call)",
                iterations,
                totalTime / 1_000_000,
                averageTime
            )
        );
    }

    /**
     * Test Java-Scala interoperability
     */
    @Test
    public void testJavaScalaInterop() {
        // Test that we can call Scala object methods from Java
        String greeting = ObpScalaLibrary.hello("InteropTest");
        String version = ObpScalaLibrary.getVersion();

        // Verify types are correctly converted
        assertTrue(
            "Greeting should be Java String",
            greeting instanceof String
        );
        assertTrue("Version should be Java String", version instanceof String);

        // Verify no Scala-specific types leak through
        assertFalse(
            "Should not contain Scala Option types",
            greeting.contains("Some(")
        );
        assertFalse("Should not contain Scala None", greeting.equals("None"));
    }

    /**
     * Test library integration in service pattern
     */
    @Test
    public void testServicePattern() {
        LibraryService service = new LibraryService();

        String greeting = service.createGreeting("ServiceTest");
        String version = service.getLibraryVersion();
        boolean isValidVersion = service.validateVersion(EXPECTED_VERSION);

        assertNotNull("Service greeting should not be null", greeting);
        assertTrue(
            "Service greeting should contain user name",
            greeting.contains("ServiceTest")
        );
        assertEquals(
            "Service should return correct version",
            EXPECTED_VERSION,
            version
        );
        assertTrue("Version validation should pass", isValidVersion);
    }

    /**
     * Helper class for testing service integration pattern
     */
    private static class LibraryService {

        public String createGreeting(String name) {
            return ObpScalaLibrary.hello(name);
        }

        public String getLibraryVersion() {
            return ObpScalaLibrary.getVersion();
        }

        public boolean validateVersion(String expectedVersion) {
            return expectedVersion.equals(ObpScalaLibrary.getVersion());
        }
    }

    /**
     * Test suite info
     */
    @Test
    public void testSuiteInfo() {
        System.out.println(
            "=== OBP Scala Library Maven Integration Test Suite ==="
        );
        System.out.println("Library Version: " + ObpScalaLibrary.getVersion());
        System.out.println(
            "Test Environment: Java " + System.getProperty("java.version")
        );
        System.out.println("Maven Integration: SUCCESS");
        System.out.println("Java-Scala Interop: SUCCESS");
        System.out.println(
            "========================================================"
        );

        // This test always passes - it's just for information
        assertTrue("Test suite info displayed", true);
    }
}
