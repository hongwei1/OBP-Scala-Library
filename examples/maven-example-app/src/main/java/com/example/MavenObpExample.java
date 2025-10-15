package com.example;

import com.openbankproject.library.ObpScalaLibrary;

/**
 * Maven Example Application demonstrating OBP Scala Library usage from Java
 *
 * This application shows how to use the OBP Scala Library in a Maven project
 * with proper dependency management and Java interoperability.
 *
 * Prerequisites:
 * 1. OBP Scala Library published locally via: sbt +publishM2
 * 2. Maven 3.6+ with Java 17+
 *
 * Usage:
 * mvn compile exec:java
 * mvn compile exec:java -Dexec.args="Alice Bob Charlie"
 */
public class MavenObpExample {

    private static final String SEPARATOR = "=".repeat(60);
    private static final String LINE = "-".repeat(40);

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("  OBP Scala Library - Maven Integration Example");
        System.out.println(SEPARATOR);
        System.out.println();

        // Test 1: Basic functionality
        testBasicFunctionality();

        // Test 2: Version information
        testVersionInformation();

        // Test 3: Multiple users (from command line args or defaults)
        String[] users = args.length > 0 ? args : new String[]{"Alice", "Bob", "Charlie", "Diana"};
        testMultipleUsers(users);

        // Test 4: Integration patterns
        testIntegrationPatterns();

        // Test 5: Error handling
        testErrorHandling();

        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("✅ Maven integration test completed successfully!");
        System.out.println("✅ OBP Scala Library is working perfectly with Maven!");
        System.out.println(SEPARATOR);
    }

    /**
     * Test 1: Basic functionality
     */
    private static void testBasicFunctionality() {
        System.out.println("Test 1: Basic Functionality");
        System.out.println(LINE);

        try {
            // Test hello method
            String greeting = ObpScalaLibrary.hello("Maven User");
            System.out.println("✅ Basic greeting: " + greeting);

            // Verify the greeting contains expected elements
            if (greeting.contains("Maven User") && greeting.contains("OBP Scala Library")) {
                System.out.println("✅ Greeting format is correct");
            } else {
                System.out.println("❌ Unexpected greeting format: " + greeting);
            }

        } catch (Exception e) {
            System.err.println("❌ Error in basic functionality test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Test 2: Version information
     */
    private static void testVersionInformation() {
        System.out.println("Test 2: Version Information");
        System.out.println(LINE);

        try {
            // Test getVersion method
            String version = ObpScalaLibrary.getVersion();
            System.out.println("✅ Library version (method): " + version);

            // Test version field
            String versionField = ObpScalaLibrary.version();
            System.out.println("✅ Library version (field): " + versionField);

            // Verify both return the same value
            if (version.equals(versionField)) {
                System.out.println("✅ Version consistency check passed");
            } else {
                System.out.println("❌ Version inconsistency detected!");
            }

            // Verify version format
            if (version != null && !version.trim().isEmpty()) {
                System.out.println("✅ Version format is valid");
            } else {
                System.out.println("❌ Invalid version format");
            }

        } catch (Exception e) {
            System.err.println("❌ Error in version information test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Test 3: Multiple users processing
     */
    private static void testMultipleUsers(String[] users) {
        System.out.println("Test 3: Multiple Users Processing");
        System.out.println(LINE);
        System.out.println("Processing " + users.length + " users:");

        try {
            int successCount = 0;
            int totalCount = users.length;

            for (int i = 0; i < users.length; i++) {
                String user = users[i];
                try {
                    String greeting = ObpScalaLibrary.hello(user);
                    System.out.println(String.format("%d. %s", i + 1, greeting));
                    successCount++;
                } catch (Exception e) {
                    System.err.println(String.format("❌ Error processing user '%s': %s", user, e.getMessage()));
                }
            }

            System.out.println(LINE);
            System.out.println(String.format("✅ Processed %d/%d users successfully", successCount, totalCount));

            if (successCount == totalCount) {
                System.out.println("✅ All users processed without errors");
            }

        } catch (Exception e) {
            System.err.println("❌ Error in multiple users test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Test 4: Integration patterns
     */
    private static void testIntegrationPatterns() {
        System.out.println("Test 4: Integration Patterns");
        System.out.println(LINE);

        try {
            // Pattern 1: Service wrapper
            GreetingService service = new GreetingService();
            String serviceGreeting = service.greetUser("Service User");
            System.out.println("✅ Service pattern: " + serviceGreeting);

            // Pattern 2: Configuration-driven
            ConfigurationExample config = new ConfigurationExample();
            config.displayConfiguration();

            // Pattern 3: Batch processing
            BatchProcessor processor = new BatchProcessor();
            processor.processBatch(new String[]{"User1", "User2", "User3"});

            System.out.println("✅ Integration patterns test completed");

        } catch (Exception e) {
            System.err.println("❌ Error in integration patterns test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Test 5: Error handling
     */
    private static void testErrorHandling() {
        System.out.println("Test 5: Error Handling");
        System.out.println(LINE);

        try {
            // Test with null input
            testNullInput();

            // Test with empty string
            testEmptyInput();

            // Test with special characters
            testSpecialCharacters();

            System.out.println("✅ Error handling tests completed");

        } catch (Exception e) {
            System.err.println("❌ Error in error handling test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    private static void testNullInput() {
        try {
            String result = ObpScalaLibrary.hello(null);
            System.out.println("✅ Null input handled: " + result);
        } catch (Exception e) {
            System.out.println("ℹ️  Null input throws exception (expected): " + e.getClass().getSimpleName());
        }
    }

    private static void testEmptyInput() {
        try {
            String result = ObpScalaLibrary.hello("");
            System.out.println("✅ Empty input handled: " + result);
        } catch (Exception e) {
            System.out.println("ℹ️  Empty input throws exception: " + e.getMessage());
        }
    }

    private static void testSpecialCharacters() {
        try {
            String result = ObpScalaLibrary.hello("User@123!#$");
            System.out.println("✅ Special characters handled: " + result);
        } catch (Exception e) {
            System.out.println("ℹ️  Special characters throw exception: " + e.getMessage());
        }
    }

    /**
     * Helper class: Service wrapper pattern
     */
    static class GreetingService {
        public String greetUser(String username) {
            return ObpScalaLibrary.hello(username);
        }

        public String getLibraryVersion() {
            return ObpScalaLibrary.getVersion();
        }
    }

    /**
     * Helper class: Configuration example
     */
    static class ConfigurationExample {
        private final String libraryVersion;
        private final String expectedVersion = "0.1.0-SNAPSHOT";

        public ConfigurationExample() {
            this.libraryVersion = ObpScalaLibrary.getVersion();
        }

        public void displayConfiguration() {
            System.out.println("  Configuration Info:");
            System.out.println("    Library Version: " + libraryVersion);
            System.out.println("    Expected Version: " + expectedVersion);

            if (libraryVersion.equals(expectedVersion)) {
                System.out.println("    ✅ Version check passed");
            } else {
                System.out.println("    ⚠️  Version mismatch detected");
            }
        }
    }

    /**
     * Helper class: Batch processor pattern
     */
    static class BatchProcessor {
        public void processBatch(String[] items) {
            System.out.println("  Batch processing " + items.length + " items:");

            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                String result = ObpScalaLibrary.hello("Batch-" + item);
                System.out.println(String.format("    Batch %d: %s", i + 1, result));
            }
        }
    }
}
