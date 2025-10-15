# OBP Scala Library - Publishing Guide

This guide covers production publishing of the OBP Scala Library to Maven Central via GitHub Actions automation.

## Overview

Publishing is fully automated through GitHub Actions workflows:

- **Snapshots**: Auto-published on push to `main` branch
- **Releases**: Triggered by creating version tags (e.g., `v1.0.0`)

## Quick Release Process

```bash
# Create and push version tag
git tag v1.0.0
git push origin v1.0.0

# GitHub Actions automatically:
# ✅ Builds all Scala versions (2.12.17, 2.13.14, 3.3.1)
# ✅ Runs complete test suite
# ✅ Signs artifacts with GPG
# ✅ Publishes to Maven Central
# ✅ Creates GitHub release with usage instructions
```

That's it! No manual steps required.

## Prerequisites (One-time Setup)

Before automated publishing works, set up these GitHub repository secrets:

### Required Secrets

Go to your GitHub repository → Settings → Secrets and variables → Actions:

1. **`PGP_SECRET`** - Base64 encoded PGP private key
2. **`PGP_PASSPHRASE`** - Passphrase for the PGP key
3. **`SONATYPE_USERNAME`** - Sonatype OSSRH username
4. **`SONATYPE_PASSWORD`** - Sonatype OSSRH password

### Setting Up Secrets

#### 1. Generate PGP Key
```bash
# Generate new PGP key
gpg --gen-key

# List keys to find your key ID
gpg --list-secret-keys --keyid-format LONG

# Export private key (replace KEY_ID with your actual key ID)
gpg --export-secret-keys KEY_ID | base64 | pbcopy
```

#### 2. Set up Sonatype Account
1. Create account at https://issues.sonatype.org/
2. Create JIRA ticket to claim `com.openbankproject` groupId
3. Get username/password for OSSRH

#### 3. Add Secrets to GitHub
Add the four secrets with their respective values.

## Version Management

### Version Numbering
Follow semantic versioning (SemVer):

- **Major** (`v2.0.0`): Breaking changes
- **Minor** (`v1.1.0`): New features, backward compatible
- **Patch** (`v1.0.1`): Bug fixes, backward compatible

### Release Types

#### Development Snapshots
**Trigger**: Push to `main` branch
**Result**: Publishes `0.1.0-SNAPSHOT` to Sonatype snapshots

#### Production Releases
**Trigger**: Push version tag (e.g., `v1.0.0`)
**Result**: Publishes `1.0.0` to Maven Central

## Published Artifacts

Each release publishes artifacts for all supported Scala versions:

- `com.openbankproject:obp-scala-library_2.12:1.0.0`
- `com.openbankproject:obp-scala-library_2.13:1.0.0`
- `com.openbankproject:obp-scala-library_3:1.0.0`

## Usage Instructions

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

## Monitoring Releases

### Check Release Status
1. **GitHub Actions**: Monitor workflow execution in Actions tab
2. **Maven Central**: Verify artifacts at https://central.sonatype.com/
3. **GitHub Releases**: Check automatically created releases

### Typical Timeline
- **Tag push**: Immediate
- **Build & test**: 5-10 minutes
- **Publish to staging**: 2-3 minutes
- **Release to Maven Central**: 10-30 minutes
- **Available for download**: 30-60 minutes

## Troubleshooting

### Common Issues

#### GPG Signing Failures
**Error**: "gpg: signing failed"
**Solution**: Verify `PGP_SECRET` is correctly base64 encoded and `PGP_PASSPHRASE` is correct

#### Sonatype Authentication Failures
**Error**: "401 Unauthorized"
**Solution**: Verify `SONATYPE_USERNAME` and `SONATYPE_PASSWORD` are correct

#### Release Already Exists
**Error**: "Version already exists"
**Solution**: Use a new version number (versions in Maven Central are immutable)

### Manual Recovery

If automated publishing fails, check the GitHub Actions logs for details. Most issues are resolved by fixing the secrets and re-running the workflow.

## Hotfix Releases

For urgent fixes:

```bash
# Create hotfix from release tag
git checkout v1.0.0
git checkout -b hotfix/1.0.1

# Make and commit your fix
git add . && git commit -m "Fix critical issue"

# Create pull request to main
# After merge, tag the hotfix
git tag v1.0.1
git push origin v1.0.1
```

## GitHub Actions Workflows

### CI Workflow (`.github/workflows/ci.yml`)
- Runs on pull requests and pushes to main/develop
- Tests all Scala versions
- Publishes snapshots on main branch

### Release Workflow (`.github/workflows/release.yml`)
- Triggered by version tags
- Publishes production releases to Maven Central
- Creates GitHub releases with usage instructions

## Best Practices

### Before Creating Release
- [ ] All tests pass: `./docker/dev.sh all`
- [ ] Documentation is up to date
- [ ] Version follows semantic versioning
- [ ] No snapshot dependencies in production releases

### Release Notes
Each GitHub release includes:
- **What's New**: New features and improvements
- **Breaking Changes**: Any incompatible changes (for major versions)
- **Usage Examples**: SBT, Maven, and Gradle dependency declarations
- **Supported Scala Versions**: List of all supported versions

## Security

- All secrets are encrypted in GitHub
- GPG signing ensures artifact integrity
- Sonatype validates all published artifacts
- Automated process eliminates human error

## Support

For publishing issues:
1. Check GitHub Actions logs for detailed error messages
2. Verify all required secrets are correctly configured
3. Consult Maven Central documentation for publishing requirements
4. Contact Sonatype support for account/permission issues

The automated publishing system provides a secure, reliable, and hands-off approach to distributing the OBP Scala Library to the broader Scala ecosystem.