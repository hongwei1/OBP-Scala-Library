# Multi-stage build for better caching and smaller final image
FROM openjdk:17-slim as base

# Install essential packages
RUN apt-get update && \
    apt-get install -y curl git gnupg apt-transport-https && \
    rm -rf /var/lib/apt/lists/*

# Add SBT repository
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add -

# Install SBT
RUN apt-get update && \
    apt-get install -y sbt=1.9.8 && \
    rm -rf /var/lib/apt/lists/*

# Pre-warm SBT and Scala dependencies
FROM base as sbt-warmed
WORKDIR /tmp/warmup

# Create minimal build.sbt to download SBT dependencies
RUN echo 'scalaVersion := "2.13.14"' > build.sbt && \
    echo 'crossScalaVersions := Seq("2.12.17", "2.13.14", "3.3.1")' >> build.sbt && \
    echo 'name := "warmup"' >> build.sbt && \
    echo 'libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test' >> build.sbt

# Set Coursier cache location to ensure it's created
ENV COURSIER_CACHE=/root/.cache/coursier

# Pre-download SBT and Scala compilers for all versions
RUN sbt +update +compile && \
    rm -rf /tmp/warmup

# Final stage
FROM base
WORKDIR /workspace

# Copy SBT global configuration and plugins (this always exists after sbt run)
COPY --from=sbt-warmed /root/.sbt /root/.sbt

# Copy Coursier cache (modern SBT dependency storage)
COPY --from=sbt-warmed /root/.cache /root/.cache

# Set SBT options for better performance and consistent cache location
ENV SBT_OPTS="-Xmx2G -XX:+UseG1GC -Dsbt.coursier.home=/root/.cache/coursier"
ENV COURSIER_CACHE=/root/.cache/coursier

# Create useful aliases
RUN echo 'alias ll="ls -la"' >> /root/.bashrc && \
    echo 'alias sbt-all="sbt +clean +compile +test +publishLocal"' >> /root/.bashrc && \
    echo 'alias sbt-quick="sbt compile test publishLocal"' >> /root/.bashrc

# Default command opens interactive bash
CMD ["/bin/bash"]
