# OBP Scala Library - Examples

This directory contains example applications demonstrating how to use the OBP Scala Library in different environments and build systems.

## Available Examples

### [java-example-app](java-example-app/)
**Java + Maven Integration**
- Shows Java-Scala interoperability
- Maven dependency management
- Basic usage patterns from Java code
- Compatible with Java 17+

### [maven-example-app](maven-example-app/)  
**Comprehensive Maven Example**
- Full Maven integration testing
- Multiple usage patterns and examples
- Error handling demonstrations
- Performance testing
- Production-ready patterns

## Prerequisites

Before running any examples, ensure the OBP Scala Library is published locally:

```bash
# From project root
cd docker
./dev.sh dev
```

This will automatically:
- Build the library for all Scala versions
- Run tests
- Publish to both local Ivy and Maven repositories
- Make the library available for all example apps

## Quick Testing

### Test All Examples
```bash
# Java example
cd java-example-app
mvn compile exec:java

# Maven example  
cd ../maven-example-app
mvn compile exec:java
```

### Test Individual Examples
```bash
# From examples/ directory

# Java example
cd java-example-app && mvn exec:java -q

# Maven example (comprehensive)
cd maven-example-app && mvn exec:java -q
```

## Example Features Demonstrated

### Basic Usage
- Library initialization and method calls
- Version information retrieval
- Error handling patterns

### Integration Patterns
- Service wrapper patterns
- Configuration-driven usage
- Batch processing examples
- Multi-user processing

### Build System Integration
- **Maven**: Proper dependency configuration
- **Gradle**: Dependency examples (in documentation)

### Language Interoperability
- **Java ↔ Scala**: Seamless method calls and type conversion
- **Exception Handling**: Cross-language error handling
- **Performance**: Minimal interop overhead demonstration

## Troubleshooting

### Library Not Found
If any example fails with dependency resolution errors:

```bash
# Re-publish the library
cd ../docker
./dev.sh dev

# Verify local repositories
ls ~/.ivy2/local/com.openbankproject/      # For SBT
ls ~/.m2/repository/com/openbankproject/   # For Maven
```

### Compilation Issues
```bash
# Clean and rebuild examples
cd java-example-app && mvn clean compile
cd maven-example-app && mvn clean compile  
```

### Version Mismatches
All examples use version `0.1.0-SNAPSHOT`. If you see version conflicts:
```bash
# Check what's published
ls ~/.m2/repository/com/openbankproject/obp-scala-library_*/*/

# Rebuild library if needed
cd ../docker && ./dev.sh dev
```

## Development Workflow

### Adding New Examples
1. Create new directory in `examples/`
2. Add appropriate build configuration (pom.xml, build.sbt, etc.)
3. Add dependency on `com.openbankproject:obp-scala-library_*:0.1.0-SNAPSHOT`
4. Create example code demonstrating library usage
5. Update this README with the new example

### Modifying Examples
1. Make changes to example code
2. Test with: `mvn compile exec:java` or `sbt run`
3. Ensure library dependency is correct
4. Update example documentation if needed

## Example Structure

Each example follows this structure:
```
example-name/
├── README.md           # Detailed example documentation
├── pom.xml             # Maven configuration
└── src/
    └── main/
        └── java/       # Java source
```

## Next Steps

1. **Start with the Maven example** - Most comprehensive demonstration
2. **Try the Java example** - Simple integration demonstration
3. **Modify examples** - Experiment with the library features
4. **Create your own projects** - Use examples as templates

Each example directory contains its own detailed README with specific instructions, usage patterns, and troubleshooting guides.