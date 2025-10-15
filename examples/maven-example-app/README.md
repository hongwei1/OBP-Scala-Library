# Maven Example App - OBP Scala Library Integration

This example demonstrates how to use the **OBP Scala Library** in a Maven project with Java 17+. The example shows proper dependency management, Java-Scala interoperability, and various integration patterns.

## Quick Start (30 seconds)

```bash
# 1. Build and publish the library
cd ../../docker
./dev.sh dev

# 2. Run the Maven example
cd ../examples/maven-example-app
mvn compile exec:java
```

## Prerequisites

- **Java 17+** (tested with OpenJDK 17.0.15)
- **Maven 3.6+**
- **Docker** (for building the OBP library)

## Project Structure

```
maven-example-app/
├── pom.xml                          # Maven configuration
├── src/main/java/com/example/
│   └── MavenObpExample.java        # Main example application
├── src/test/java/com/example/
│   └── ObpLibraryTest.java         # Unit tests
└── README.md                        # This file
```

## Maven Configuration

The `pom.xml` is configured for:

- **Java 17** compilation target
- **Scala 2.13.14** runtime (configurable via profiles)
- **OBP Scala Library** dependency from local Maven repository
- Multiple Scala version profiles (2.12, 2.13, 3.x)

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

### 1. Build and Publish Library

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
- ✅ **Copy artifacts to your local Maven repository** (`~/.m2/repository/`)

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

### 3. Run the Example

```bash
# Compile and run with default arguments
mvn compile exec:java

# Run with custom user names
mvn compile exec:java -Dexec.args="Alice Bob Charlie"

# Clean build and run
mvn clean compile exec:java
```

## Example Features

The `MavenObpExample.java` demonstrates:

### 1. **Basic Functionality**
- Simple library method calls
- String processing and validation
- Error handling patterns

### 2. **Version Information**  
- Library version retrieval
- Version consistency checks
- Runtime validation

### 3. **Batch Processing**
- Multiple user processing
- Error handling for individual items
- Success rate tracking

### 4. **Integration Patterns**
- Service wrapper pattern
- Configuration-driven usage  
- Batch processing pattern

### 5. **Error Handling**
- Null input handling
- Empty string processing
- Special character support

## Running with Different Scala Versions

### Scala 2.12
```bash
mvn compile exec:java -Pscala-2.12
```

### Scala 3.x
```bash  
mvn compile exec:java -Pscala-3
```

## Testing

```bash
# Run unit tests
mvn test

# Run tests with specific profile
mvn test -Pscala-2.12
```

## Dependency Analysis

```bash
# View dependency tree
mvn dependency:tree

# Analyze dependencies
mvn dependency:analyze
```

## Troubleshooting

### Library Not Found

**Problem:** `Could not resolve dependency: com.openbankproject:obp-scala-library_2.13`

**Solution:**
```bash
# Re-publish the library
cd ../../docker
./dev.sh dev

# Verify local repository
ls -la ~/.m2/repository/com/openbankproject/
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
cd ../examples/maven-example-app && mvn clean compile
```

## Advanced Usage

### Custom Maven Repository

If you need to use a different Maven repository:

```xml
<repositories>
    <repository>
        <id>custom-repo</id>
        <url>https://your-custom-repo.com/maven2</url>
    </repository>
</repositories>
```

### IDE Integration

For **IntelliJ IDEA** or **Eclipse**:

1. Import as Maven project
2. Ensure Java 17 is configured
3. Maven will automatically download dependencies
4. Run `MavenObpExample.main()` directly

### Docker Integration

To run the Maven example inside Docker:

```bash
# From the docker directory  
./dev.sh shell

# Inside container
cd /workspace/maven-example-app
mvn compile exec:java
```

## Performance Notes

- **First run** may be slower due to Maven dependency downloads
- **Subsequent runs** use cached dependencies and are much faster
- **Docker builds** use persistent volumes for optimal performance

## Production Deployment

For production use:

1. Replace `0.1.0-SNAPSHOT` with a stable version
2. Use Maven Central or your corporate repository
3. Configure appropriate logging and monitoring
4. Add proper exception handling

## Integration Examples

### Spring Boot Integration

```java
@Service
public class BankingService {
    public String processCustomer(String customerName) {
        return ObpScalaLibrary.hello(customerName);
    }
}
```

### REST API Integration

```java
@RestController
public class BankingController {
    @GetMapping("/greet/{name}")
    public ResponseEntity<String> greet(@PathVariable String name) {
        String greeting = ObpScalaLibrary.hello(name);
        return ResponseEntity.ok(greeting);
    }
}
```

## Benefits

- ✅ **Seamless Java-Scala Interop**: Direct method calls from Java
- ✅ **Maven Standard**: Uses familiar Maven build lifecycle
- ✅ **Local Development**: No external repository dependencies
- ✅ **Multi-version Support**: Works with Scala 2.12, 2.13, and 3.x
- ✅ **Production Ready**: Proper error handling and logging
- ✅ **IDE Friendly**: Full IntelliJ/Eclipse integration

## Next Steps

1. **Explore the Java code** in `src/main/java/com/example/MavenObpExample.java`
2. **Run the tests** with `mvn test`  
3. **Modify the example** to suit your use case
4. **Check out other examples** in `../java-example-app/` and `../scala-example-app/` for different approaches
5. **Deploy your Maven applications**

Happy coding! 🚀