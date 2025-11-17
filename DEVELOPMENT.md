# OBP Scala Library - Development Guide

This guide covers Docker-based development for the OBP Scala Library. The Docker approach ensures system independence and consistent development environments.



## Development Commands

### Container Management
```bash
./dev.sh build      # Build Docker image and start container (first time)
./dev.sh start      # Start existing container
./dev.sh stop       # Stop container
./dev.sh shell      # Open interactive bash shell
./dev.sh status     # Show container status
./dev.sh rebuild    # Rebuild Docker image from scratch
```

### Development Workflow
```bash
./dev.sh dev        # Quick build: compile + test + publish (all versions)
./dev.sh dev 2.13.14    # Build specific Scala version
./dev.sh all        # Full build: clean + compile + test + publish
```

### SBT Operations
```bash
./dev.sh compile           # Compile all versions
./dev.sh compile 2.12.20   # Compile specific version
./dev.sh test              # Run tests for all versions
./dev.sh test 3.3.1        # Run tests for specific version
./dev.sh publish           # Publish locally (all versions)
./dev.sh clean             # Clean build artifacts
```

## Daily Development Cycle

### 1. Start Development Environment
```bash
cd docker
./dev.sh build    # Only needed once or after image changes
```

### 2. Make Your Changes
Edit files in `../src/main/scala/` using your favorite IDE or editor.

### 3. Quick Test and Publish
```bash
./dev.sh dev      # Compile, test, publish, and copy to local Maven repo
```

### 4. Use in Your Projects
The library is automatically published to both local repositories:
- **Local Ivy**: `~/.ivy2/local/com.openbankproject/`
- **Local Maven**: `~/.m2/repository/com/openbankproject/`

**SBT:**
```scala
libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"
```

**Maven:**
```xml
<dependency>
    <groupId>com.openbankproject</groupId>
    <artifactId>obp-scala-library_2.13</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Gradle:**
```gradle
implementation 'com.openbankproject:obp-scala-library_2.13:0.1.0-SNAPSHOT'
```

## Supported Scala Versions

- **2.12.20** - LTS version, widely used in enterprise
- **2.13.14** - Current stable version (default)
- **3.3.1** - Latest Scala 3 version

All versions are built and tested automatically.

## Docker Configuration

### Services
- **sbt**: SBT container with JDK 17 and all Scala versions pre-installed

### Volumes
- **sbt-cache**: SBT global settings and plugins
- **ivy-cache**: Ivy dependency cache
- **coursier-cache**: Coursier dependency cache
- **Project source**: Mounted from parent directory

### Performance Optimizations
- Pre-warmed SBT with all Scala versions
- Dependency caches persisted in volumes
- Optimized JVM settings for development

## Interactive Development

### Open Shell for Manual Commands
```bash
./dev.sh shell
```

Inside the container, you can run any SBT command:
```bash
# Inside container
sbt                          # Start interactive SBT
sbt +compile                 # Compile all versions
sbt "++2.13.14" test        # Test specific version
sbt +publishLocal           # Publish all versions locally
sbt "show crossScalaVersions"  # Show configured versions
```

### Hot Reloading
```bash
# Inside container shell
sbt
> ~compile    # Auto-recompile on file changes
> ~test       # Auto-test on file changes
```

## Testing Your Changes

### Unit Tests
```bash
./dev.sh test              # Run tests for all Scala versions
./dev.sh test 2.13.14     # Run tests for specific version
```

### Integration Testing with Other Projects

1. **Publish locally:**
   ```bash
   ./dev.sh dev
   ```

2. **In your test project, add dependency:**
   ```scala
   libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"
   ```

3. **Reload your test project:**
   ```bash
   sbt reload
   ```

4. Your changes are immediately available!

## Adding New Scala Versions

### 1. Update build.sbt
```scala
crossScalaVersions := Seq("2.12.20", "2.13.14", "3.3.1", "3.4.0")  // Add new version
```

### 2. Rebuild Docker image
```bash
./dev.sh rebuild    # Rebuilds with new Scala version
```

### 3. Test new version
```bash
./dev.sh dev 3.4.0  # Test specific new version
./dev.sh dev        # Test all versions including new one
```

## IDE Integration

### IntelliJ IDEA
1. Open the project root directory (not the docker directory)
2. IntelliJ will detect the `build.sbt` file
3. Import as SBT project
4. Use the Docker container for building/testing
5. Edit files normally in IntelliJ

### VS Code
1. Install Metals extension for Scala
2. Open project root directory
3. Metals will use the `build.sbt` configuration
4. Use Docker container for builds via integrated terminal

### Any Editor + Terminal
1. Edit files in `src/main/scala/` with any editor
2. Use `./dev.sh dev` in terminal for building
3. Perfect for vim, emacs, or any text editor

## Performance Tips

### 1. Keep Container Running
```bash
# Start once, use many times
./dev.sh start
./dev.sh dev        # Fast since container is already running
```

### 2. Use Version-Specific Builds
```bash
# During development, test only the version you need
./dev.sh dev 2.13.14    # Much faster than all versions
```

### 3. Persistent Caches
Docker volumes cache SBT dependencies between container restarts:
- `sbt-cache`: SBT global settings and plugins
- `ivy-cache`: Ivy dependency cache
- `coursier-cache`: Coursier dependency cache

## Troubleshooting

### Container Won't Start
```bash
./dev.sh rebuild    # Rebuild Docker image from scratch
```

### Compilation Errors
```bash
./dev.sh clean      # Clean build artifacts
./dev.sh dev        # Rebuild everything
```

### Library Not Found in Other Projects
```bash
./dev.sh publish    # Ensure library is published locally
ls ~/.ivy2/local/com.openbankproject/   # Verify Ivy artifacts exist
ls ~/.m2/repository/com/openbankproject/  # Verify Maven artifacts exist
```

### Performance Issues
```bash
./dev.sh stop
docker system prune  # Clean up Docker resources
./dev.sh build
```

### Cache Issues
```bash
./dev.sh shell
# Inside container:
rm -rf ~/.ivy2/cache ~/.sbt
exit
./dev.sh rebuild
```

## Advanced Usage

### Custom SBT Commands
```bash
./dev.sh shell
# Inside container:
sbt "testOnly com.openbankproject.MySpecificTest"
sbt "runMain com.openbankproject.MyApp"
sbt dependencyTree
```

### Publishing for Production
```bash
./dev.sh shell
# Inside container (requires proper credentials):
sbt +publishSigned    # For Maven Central publishing
```

## Why Docker?

- ✅ **System Independent**: Works on any OS with Docker
- ✅ **No Dependencies**: Don't need Java/SBT/Scala installed
- ✅ **Consistent Environment**: Same setup for all developers
- ✅ **Fast Setup**: Pre-built images with dependencies cached
- ✅ **Any Scala Version**: Easy to add/test new versions
- ✅ **Isolation**: No conflicts with system installations

## Local Artifacts Location

The improved development flow automatically publishes artifacts to both:
- **Ivy repository**: `~/.ivy2/local/com.openbankproject/obp-scala-library_*/`
- **Maven repository**: `~/.m2/repository/com/openbankproject/obp-scala-library_*/` (copied from Docker container)

This enables seamless integration with both SBT and Maven projects on your host system.

## Example Development Session

```bash
# Start development
cd docker
./dev.sh build

# Make changes to ../src/main/scala/MyLibrary.scala

# Quick test
./dev.sh dev 2.13.14

# Full test before committing
./dev.sh all

# Test in another project
# Add dependency and reload project
```

## Requirements

- **Docker** and **Docker Compose**
- That's it! Everything else runs in containers.



For production publishing details, see [.github/PUBLISHING.md](.github/PUBLISHING.md).