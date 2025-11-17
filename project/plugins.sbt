// project/plugins.sbt

// sbt-pgp for signing - updated to compatible version
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.1")

// sbt-release for release automation - updated to compatible version
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")

// sbt-ci-release for CI/CD release automation - updated to compatible version
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.5.12")

// scalafmt for consistent formatting checks
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

// Add airframe-log to suppress AirframeLogManager warning from sbt-ci-release
libraryDependencies += "org.wvlet.airframe" %% "airframe-log" % "24.5.0"
