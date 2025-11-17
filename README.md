# OBP Scala Library

A comprehensive Scala library for banking operations by the Open Bank Project.

## Quick Start

### Docker Development (Recommended)

```bash
# 1. Clone and setup
git clone https://github.com/OpenBankProject/OBP-Scala-Library.git
cd OBP-Scala-Library/docker

# 2. Build development environment
./dev.sh build

# 3. Build library for all Scala versions
./dev.sh dev

# 4. Use in your projects
# libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"
```

### Production Release

```bash
# Create release (publishes automatically to Maven Central)
git tag v1.0.0
git push origin v1.0.0
```

## Supported Scala Versions

- **2.12.20** - LTS version
- **2.13.14** - Current stable (default)
- **3.3.1** - Latest Scala 3

## Usage

### SBT Projects
```scala
libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "1.0.0"
```

### Maven Projects
```xml
<dependency>
    <groupId>com.openbankproject</groupId>
    <artifactId>obp-scala-library_2.13</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle Projects
```gradle
implementation 'com.openbankproject:obp-scala-library_2.13:1.0.0'
```

## Development Commands

```bash
cd docker

# Container management
./dev.sh build      # Build and start development environment
./dev.sh start      # Start existing container
./dev.sh stop       # Stop container
./dev.sh shell      # Interactive shell

# Development workflow
./dev.sh dev        # Quick build: compile + test + publish (all versions)
./dev.sh dev 2.13.14    # Build specific Scala version
./dev.sh all        # Full build: clean + compile + test + publish

# SBT operations
./dev.sh compile    # Compile all versions
./dev.sh test       # Run tests for all versions
./dev.sh publish    # Publish locally
./dev.sh clean      # Clean build artifacts
```

## Why Docker?

- ✅ **System Independent**: Works on any OS with Docker
- ✅ **No Dependencies**: Don't need Java/SBT/Scala installed
- ✅ **Consistent Environment**: Same setup for all developers
- ✅ **Fast Setup**: Pre-built images with dependencies cached
- ✅ **Any Scala Version**: Easy to add/test new versions

## Example Development Workflow

```bash
# 1. Start development (one time)
cd docker
./dev.sh build

# 2. Make changes to ../src/main/scala/...

# 3. Quick test and publish
./dev.sh dev

# 4. Library now available locally
# Use in other projects: libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"
```

## Project Structure

```
OBP-Scala-Library/
├── src/main/scala/           # Library source code
├── src/test/scala/           # Test source code
├── examples/                 # Usage examples
│   ├── java-example-app/     # Java + Maven example
│   └── maven-example-app/    # Comprehensive Maven example
├── docker/
│   ├── dev.sh               # Development script
│   ├── docker-compose.yml   # Container configuration
│   └── sbt/Dockerfile       # SBT container
├── .github/
│   ├── workflows/          # CI/CD automation
│   └── PUBLISHING.md       # Production publishing guide
├── build.sbt               # Build configuration
├── README.md               # This file
└── DEVELOPMENT.md          # Detailed development guide
```

## Requirements

- **Docker** and **Docker Compose**
- That's it! Everything else runs in containers.

## Troubleshooting

```bash
# Container won't start
./dev.sh rebuild

# Compilation errors
./dev.sh clean
./dev.sh dev

# Library not found in projects
./dev.sh publish
ls ~/.ivy2/local/com.openbankproject/

# Performance issues
./dev.sh stop
docker system prune
./dev.sh build
```

## Documentation

- **[DEVELOPMENT.md](DEVELOPMENT.md)** - Complete development guide
- **[.github/PUBLISHING.md](.github/PUBLISHING.md)** - Production publishing guide

## Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature-name`
3. Make changes and test: `./docker/dev.sh dev`
4. Commit changes: `git commit -m "Add feature"`
5. Push branch: `git push origin feature-name`
6. Create Pull Request

## License

AGPL-3.0 - see [LICENSE](LICENSE) file for details.

## Support

- **GitHub Issues**: Report bugs and request features
- **Documentation**: See DEVELOPMENT.md and .github/PUBLISHING.md
- **Community**: [Open Bank Project](https://www.openbankproject.com/)