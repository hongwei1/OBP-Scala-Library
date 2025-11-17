# Java Example App - OBP Scala Library Integration

This example demonstrates how to use the **OBP Scala Library** in a Java application using Maven. The example shows proper dependency management, Java-Scala interoperability, and integration patterns.

## Quick Start (30 seconds)

```bash
# 1. Build and publish the library (with automatic Maven publishing)
cd ../../docker
./dev.sh dev

# 2. Run the Java example
cd ../examples/java-example-app
mvn compile exec:java
```

## Prerequisites

- **Java 17+** (tested with OpenJDK 17.0.15)
- **Maven 3.6+**
- **Docker** (for building the OBP library)

## Project Structure

```
java-example-app/
├── pom.xml                          # Maven configuration
├── src/main/java/com/example/app/
│   └── MainApp.java                # Main Java application
└── README.md                       # This file
```

## Maven Configuration

The `pom.xml` is configured for:

- **Java 17** compilation target
- **Scala 2.13.14** runtime (required for Java interop)
- **OBP Scala Library** dependency from local Maven repository

### Key Dependencies

```xml
<dependency>
    <groupId>com.openbankproject</groupId>
    <artifactId>obp-scala-library_2.13</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-library</artifactId>
    <version>2.13.14</version>
</dependency>
```

## Development Workflow

### 1. Build and Publish Library (Automatic Maven Publishing)

The improved Docker workflow automatically publishes to your local Maven repository:

```bash
cd ../../docker

# Full development cycle - builds, tests, and publishes to local Maven
./dev.sh dev

# Alternative: Quick build for specific Scala version
./dev.sh dev 2.13.14
```

This will:
- ✅ Compile the library for all Scala versions (2.12, 2.13, 3.x)
- ✅ Run tests to ensure quality
- ✅ Publish to Docker container's repositories
- ✅ **Automatically copy artifacts to your local Maven repository** (`~/.m2/repository/`)

### 2. Verify Library Installation

```bash
# Check if library is available locally
ls -la ~/.m2/repository/com/openbankproject/
```

You should see:
```
obp-scala-library_2.12/
obp-scala-library_2.13/  
obp-scala-library_3/
```

### 3. Run the Java Example

```bash
# Compile and run
mvn compile exec:java

# Clean build and run
mvn clean compile exec:java

# Run with explicit main class
mvn compile exec:java -Dexec.mainClass="com.example.app.MainApp"
```

Expected output:
```
Hello, Java Developer from OBP Scala Library v0.1.0-SNAPSHOT!
```

## Example Code

The `MainApp.java` demonstrates:

### Basic Library Usage
```java
import com.openbankproject.library.ObpScalaLibrary;

public class MainApp {
    public static void main(String[] args) {
        // Call Scala library from Java
        String greeting = ObpScalaLibrary.hello("Java Developer");
        System.out.println(greeting);
        
        // Get library version
        String version = ObpScalaLibrary.getVersion();
        System.out.println("Library version: " + version);
    }
}
```

### Java-Scala Interoperability
- **Seamless method calls**: Direct invocation of Scala methods from Java
- **Type compatibility**: Scala `String` → Java `String` automatically
- **Exception handling**: Standard Java try-catch works with Scala exceptions

## Usage in Your Own Java Projects

To use the OBP Scala Library in your own Java/Maven projects:

### 1. Publish the Library
```bash
cd path/to/OBP-Scala-Library/docker
./dev.sh dev
```

### 2. Add Dependencies to your `pom.xml`
```xml
<dependencies>
    <!-- OBP Scala Library -->
    <dependency>
        <groupId>com.openbankproject</groupId>
        <artifactId>obp-scala-library_2.13</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Scala Runtime (required for Java interop) -->
    <dependency>
        <groupId>org.scala-lang</groupId>
        <artifactId>scala-library</artifactId>
        <version>2.13.14</version>
    </dependency>
</dependencies>
```

### 3. Use in Your Java Code
```java
import com.openbankproject.library.ObpScalaLibrary;

public class MyBankingApp {
    public void processCustomer(String customerName) {
        String greeting = ObpScalaLibrary.hello(customerName);
        System.out.println("Processing: " + greeting);
    }
}
```

## Scala Version Support

The library is cross-compiled for multiple Scala versions:

### Scala 2.12
```xml
<dependency>
    <groupId>com.openbankproject</groupId>
    <artifactId>obp-scala-library_2.12</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-library</artifactId>
    <version>2.12.20</version>
</dependency>
```

### Scala 3.x
```xml
<dependency>
    <groupId>com.openbankproject</groupId>
    <artifactId>obp-scala-library_3</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala3-library_3</artifactId>
    <version>3.3.1</version>
</dependency>
```

## IDE Integration

### IntelliJ IDEA
1. Import as Maven project
2. Ensure Java 17 is configured in Project Settings
3. Maven will automatically download dependencies
4. Run `MainApp.main()` directly from the IDE

### Eclipse
1. Import → Existing Maven Projects
2. Select the `java-example-app` directory
3. Eclipse will configure the project automatically
4. Right-click → Run As → Java Application

### VS Code
1. Install Java Extension Pack
2. Open the `java-example-app` directory
3. VS Code will detect Maven and configure classpath
4. Use integrated terminal for Maven commands

## Troubleshooting

### Library Not Found

**Problem:** `Could not resolve dependency: com.openbankproject:obp-scala-library_2.13`

**Solution:**
```bash
# Re-publish the library with automatic Maven copying
cd ../docker
./dev.sh dev

# Verify local repository
ls -la ~/.m2/repository/com/openbankproject/
```

### ClassNotFoundException

**Problem:** `java.lang.ClassNotFoundException: scala.Product`

**Solution:** Ensure Scala runtime is included:
```xml
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-library</artifactId>
    <version>2.13.14</version>
</dependency>
```

### Compilation Errors

**Problem:** Java compilation fails with Scala interop issues

**Solution:**
```bash
# Clean and rebuild
mvn clean compile

# Check Java and Maven versions
java -version
mvn -version
```

### Version Mismatches

**Problem:** Runtime version differs from expected

**Solution:**
```bash  
# Check which version is installed
mvn dependency:tree | grep obp-scala-library

# Rebuild with latest version
cd ../../docker && ./dev.sh dev
cd ../examples/java-example-app && mvn clean compile
```

## Advanced Features

### Maven Profiles

Create profiles for different Scala versions:

```xml
<profiles>
    <profile>
        <id>scala-2.12</id>
        <dependencies>
            <dependency>
                <groupId>com.openbankproject</groupId>
                <artifactId>obp-scala-library_2.12</artifactId>
                <version>0.1.0-SNAPSHOT</version>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Usage: `mvn compile exec:java -Pscala-2.12`

### Spring Boot Integration

```java
@RestController
public class BankingController {
    
    @GetMapping("/greet/{name}")
    public ResponseEntity<String> greet(@PathVariable String name) {
        String greeting = ObpScalaLibrary.hello(name);
        return ResponseEntity.ok(greeting);
    }
    
    @GetMapping("/version")
    public ResponseEntity<String> version() {
        String version = ObpScalaLibrary.getVersion();
        return ResponseEntity.ok("Library version: " + version);
    }
}
```

## Performance Notes

- **First run** may be slower due to Maven dependency downloads
- **Subsequent runs** use cached dependencies and are much faster
- **Docker builds** use persistent volumes for optimal performance
- **Java-Scala interop** has minimal overhead

## Production Deployment

For production use:

1. **Use stable versions**: Replace `0.1.0-SNAPSHOT` with stable versions like `1.0.0`
2. **Maven Central**: Use published versions from Maven Central instead of local
3. **Proper logging**: Configure logging frameworks appropriately
4. **Exception handling**: Add comprehensive error handling
5. **Security**: Follow security best practices for banking applications

## Benefits

- ✅ **Seamless Java-Scala Interop**: Direct method calls from Java
- ✅ **Maven Standard**: Uses familiar Maven build lifecycle  
- ✅ **Local Development**: No external repository dependencies for development
- ✅ **Multi-version Support**: Works with Scala 2.12, 2.13, and 3.x
- ✅ **IDE Friendly**: Full IntelliJ/Eclipse/VS Code integration
- ✅ **Spring Boot Ready**: Easy integration with Spring ecosystem

## Next Steps

1. **Explore the Java code** in `src/main/java/com/example/app/MainApp.java`
2. **Modify the example** to suit your banking use case
3. **Check out other examples** in `../maven-example-app/` and `../scala-example-app/` for different approaches
4. **Integrate with your projects** using the dependency configuration above

Happy coding! 🚀