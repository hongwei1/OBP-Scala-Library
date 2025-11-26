name := "obp-scala-library"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.14"

crossScalaVersions := Seq("2.12.20", "2.13.14", "3.3.1")

// Organization and metadata
ThisBuild / organization := "com.openbankproject"
ThisBuild / organizationName := "Open Bank Project"
ThisBuild / organizationHomepage := Some(url("https://www.openbankproject.com/"))

// Dependencies
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.10",
  "io.circe" %% "circe-parser" % "0.14.10",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

// Development: Simple local publishing
// Use: sbt publishLocal
// Then in other projects: libraryDependencies += "com.openbankproject" %% "obp-scala-library" % "0.1.0-SNAPSHOT"

// Production: Publishing to Maven Central via GitHub Actions
// Triggered by Git tags (v1.0.0, v1.1.0, etc.)
publishTo := {
  val sonatype = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at sonatype + "content/repositories/snapshots")
  else
    Some("releases" at sonatype + "service/local/staging/deploy/maven2")
}

// Publishing configuration
publishMavenStyle := true
Test / publishArtifact := false
pomIncludeRepository := { _ => false }

// License and project info
ThisBuild / licenses := List("AGPL-3.0" -> url("https://www.gnu.org/licenses/agpl-3.0.en.html"))
ThisBuild / homepage := Some(url("https://github.com/OpenBankProject/OBP-Scala-Library"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/OpenBankProject/OBP-Scala-Library"),
    "scm:git@github.com:OpenBankProject/OBP-Scala-Library.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "obp-team",
    name = "Open Bank Project Team",
    email = "contact@openbankproject.com",
    url = url("https://github.com/OpenBankProject")
  )
)

// Project description (will be used in generated POM)
description := "Open Bank Project Scala Library - A comprehensive library for banking operations"

// Cross-compilation settings
crossPaths := true
autoScalaLibrary := true

// Version scheme
ThisBuild / versionScheme := Some("early-semver")

// Credentials for publishing (used in GitHub Actions)
credentials ++= {
  val sonatypeUser = sys.env.get("SONATYPE_USERNAME")
  val sonatypePass = sys.env.get("SONATYPE_PASSWORD")

  (sonatypeUser, sonatypePass) match {
    case (Some(user), Some(pass)) =>
      Seq(Credentials("Sonatype Nexus Repository Manager", "s01.oss.sonatype.org", user, pass))
    case _ =>
      Seq.empty
  }
}

// GPG signing (for Maven Central)
Global / useGpgPinentry := true
