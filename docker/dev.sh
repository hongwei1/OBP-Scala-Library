#!/bin/bash

# OBP Scala Library - Simple Docker Development Script
# Provides easy commands for local Docker-based development

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Logging functions
log_info() { echo -e "${BLUE}INFO: $1${NC}"; }
log_success() { echo -e "${GREEN}SUCCESS: $1${NC}"; }
log_warning() { echo -e "${YELLOW}WARNING: $1${NC}"; }
log_error() { echo -e "${RED}ERROR: $1${NC}" >&2; }

show_usage() {
    cat << EOF
🔨 OBP Scala Library - Docker Development

Usage: $0 <command> [options]

Commands:
  build                     Build Docker image and start container
  start                     Start existing container
  stop                      Stop container
  shell                     Open bash shell in container

  # SBT commands (run inside container)
  compile [version]         Compile for specific Scala version (default: all)
  test [version]            Run tests for specific version (default: all)
  publish [version]         Publish locally + copy to host Maven repo (default: all)
  clean                     Clean build artifacts

  # Development workflow
  dev [version]             Quick cycle: compile + test + publish + copy to host
  all                       Full build: clean + compile + test + publish + copy to host

  # Container management
  logs                      Show container logs
  status                    Show container status
  rebuild                   Rebuild Docker image from scratch

Examples:
  $0 build                  # Build and start development environment
  $0 dev                    # Quick build + publish to host Maven repo
  $0 dev 2.13.14           # Quick build for Scala 2.13.14 + publish to host
  $0 compile 3.3.1         # Compile only for Scala 3.3.1
  $0 shell                  # Open interactive shell
  $0 all                    # Full build + publish to host Maven repo

Supported Scala Versions:
  2.12.20, 2.13.14, 3.3.1

EOF
}

# Check if Docker and Docker Compose are available
check_docker() {
    if ! command -v docker >/dev/null 2>&1; then
        log_error "Docker is not installed or not in PATH"
        exit 1
    fi

    if ! command -v docker-compose >/dev/null 2>&1; then
        log_error "Docker Compose is not installed or not in PATH"
        exit 1
    fi

    if ! docker info >/dev/null 2>&1; then
        log_error "Docker daemon is not running"
        exit 1
    fi
}

# Build and start container
build_container() {
    log_info "Building Docker image..."
    cd "$SCRIPT_DIR"
    docker-compose build sbt

    log_info "Starting container..."
    docker-compose up -d sbt

    log_success "Development environment ready!"
    log_info "Use '$0 shell' to open interactive shell"
}

# Start existing container
start_container() {
    cd "$SCRIPT_DIR"
    if docker-compose ps sbt | grep -q "Up"; then
        log_info "Container is already running"
    else
        log_info "Starting container..."
        docker-compose up -d sbt
        log_success "Container started"
    fi
}

# Stop container
stop_container() {
    cd "$SCRIPT_DIR"
    log_info "Stopping container..."
    docker-compose down
    log_success "Container stopped"
}

# Open shell in container
open_shell() {
    cd "$SCRIPT_DIR"
    start_container
    log_info "Opening shell in container..."
    docker-compose exec sbt bash
}

# Run SBT command in container
run_sbt() {
    local cmd="$1"
    cd "$SCRIPT_DIR"
    start_container
    docker-compose exec -T sbt sbt "$cmd"
}

# Compile command
compile_scala() {
    local version="$1"
    if [ -n "$version" ]; then
        log_info "Compiling for Scala $version..."
        run_sbt "++$version compile"
    else
        log_info "Compiling for all Scala versions..."
        run_sbt "+compile"
    fi
}

# Test command
test_scala() {
    local version="$1"
    if [ -n "$version" ]; then
        log_info "Running tests for Scala $version..."
        run_sbt "++$version test"
    else
        log_info "Running tests for all Scala versions..."
        run_sbt "+test"
    fi
}

# Publish locally
publish_scala() {
    local version="$1"
    if [ -n "$version" ]; then
        log_info "Publishing locally for Scala $version..."
        run_sbt "++$version publishLocal"
    else
        log_info "Publishing locally for all Scala versions..."
        run_sbt "+publishLocal"
    fi

    # Copy to local Maven repository
    copy_to_local_maven
}

# Clean build
clean_build() {
    log_info "Cleaning build artifacts..."
    run_sbt "clean"
}

# Copy artifacts from Docker container to local Maven repository
copy_to_local_maven() {
    log_info "Publishing to local Maven repository inside container..."
    run_sbt "+publishM2"

    log_info "Copying artifacts to host local Maven repository..."
    local host_m2_dir="$HOME/.m2/repository"

    # Create local Maven directory if it doesn't exist
    mkdir -p "$host_m2_dir"

    # Copy the artifacts from container to host
    if docker cp obp-sbt:/root/.m2/repository/com/openbankproject "$host_m2_dir/com/" 2>/dev/null; then
        log_success "Artifacts copied to local Maven repository!"
        echo
        log_info "Library is now available in both:"
        echo "  • Local Ivy: ~/.ivy2/local/com.openbankproject/"
        echo "  • Local Maven: ~/.m2/repository/com/openbankproject/"
        echo
        log_info "Use in Maven projects:"
        echo '  <dependency>'
        echo '    <groupId>com.openbankproject</groupId>'
        echo '    <artifactId>obp-scala-library_2.13</artifactId>'
        echo '    <version>0.1.0-SNAPSHOT</version>'
        echo '  </dependency>'
        echo
        log_info "Use in SBT projects:"
        echo '  libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"'
    else
        log_warning "Failed to copy to local Maven repository, but library is available in container"
        log_info "Library is available in container at:"
        echo '  libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"'
    fi
}

# Development workflow - quick iteration
dev_workflow() {
    local version="$1"
    if [ -n "$version" ]; then
        log_info "Quick development build for Scala $version..."
        run_sbt "++$version; compile; test; publishLocal"
        log_success "Development build complete for Scala $version!"
    else
        log_info "Quick development build for all Scala versions..."
        run_sbt "+compile; +test; +publishLocal"
        log_success "Development build complete for all versions!"
    fi

    # Copy to local Maven repository
    copy_to_local_maven
}

# Full build workflow
full_workflow() {
    log_info "Running full build for all Scala versions..."
    run_sbt "+clean; +compile; +test; +publishLocal"
    log_success "Full build complete!"

    # Copy to local Maven repository
    copy_to_local_maven
}

# Show container logs
show_logs() {
    cd "$SCRIPT_DIR"
    docker-compose logs sbt
}

# Show container status
show_status() {
    cd "$SCRIPT_DIR"
    echo "Container status:"
    docker-compose ps sbt
    echo
    echo "Docker images:"
    docker images | grep -E "(REPOSITORY|obp-sbt|openjdk)"
}

# Rebuild container from scratch
rebuild_container() {
    log_info "Rebuilding Docker image from scratch..."
    cd "$SCRIPT_DIR"
    docker-compose down
    docker-compose build --no-cache sbt
    docker-compose up -d sbt
    log_success "Container rebuilt successfully!"
}

# Main command processing
main() {
    if [ $# -eq 0 ]; then
        show_usage
        exit 1
    fi

    check_docker

    case "$1" in
        build)
            build_container
            ;;
        start)
            start_container
            ;;
        stop)
            stop_container
            ;;
        shell)
            open_shell
            ;;
        compile)
            compile_scala "$2"
            ;;
        test)
            test_scala "$2"
            ;;
        publish)
            publish_scala "$2"
            ;;
        clean)
            clean_build
            ;;
        dev)
            dev_workflow "$2"
            ;;
        all)
            full_workflow
            ;;
        logs)
            show_logs
            ;;
        status)
            show_status
            ;;
        rebuild)
            rebuild_container
            ;;
        --help|-h)
            show_usage
            ;;
        *)
            log_error "Unknown command: $1"
            echo
            show_usage
            exit 1
            ;;
    esac
}

# Run main function with all arguments
main "$@"
