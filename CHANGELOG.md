# toolarium-jwebserver

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [ 1.2.9 ] - 2026-04-25

## [ 1.2.8 ] - 2026-04-25
### Changed
- Updated dependencies.
- Max entity size set to 25 MB via UndertowOptions.
- Reuse single auth-wrapped handler instance for all route registrations.
- Replaced string concatenation with parameterized logging across codebase.

### Added
- Graceful shutdown hook for SIGTERM/SIGINT signals.
- Support for `env:VAR_NAME` prefix in `--basicauth` to read credentials from environment variables.
- Port range validation for port and securePort configuration.
- Path traversal protection with canonicalization and `..` rejection in PathResourceManager and ClassPathResourceManager.
- Extension resolution cache (bounded ConcurrentHashMap) in PathResourceManager and ClassPathResourceManager.
- Warning log when trust-all certificate mode is enabled.
- Authorization header redaction in access logs to prevent credential leakage.
- Health endpoint is now protected by basic authentication when configured.

### Fixed
- Constant-time password comparison using MessageDigest.isEqual() to prevent timing attacks.
- Proxy SSL fallback now logs explicit warning with host name when falling back to unencrypted connection.
- Proxy URI validation: hosts with missing scheme or host are skipped with a warning.
- Thread-safe synchronized SSLContext lazy initialization.
- Credential properties (basicAuthentication, keyStorePassword) no longer go through environment variable expansion.
- Double expansion bug fixed in ConfigurationUtil integer conversion.
- SSL port initialization failure now sets hasError flag.

## [ 1.2.7 ] - 2025-03-18
### Fixed
- Switch resolveParentResourceIfNotFound in jwebserver.properties.

## [ 1.2.6 ] - 2025-03-16
### Fixed
- Switch --disableResolveParentResourceIfNotFound added to disable resource resolution of parent resources if the requested resource can't be found.

## [ 1.2.5 ] - 2025-02-26
### Added
- Switch --disableResolveParentResourceIfNotFound added to disable resource resolution of parent resources if the requested resource can't be found.

## [ 1.2.4 ] - 2025-01-14
### Changed
- Added support of hierarchy backward climbing.

## [ 1.2.3 ] - 2025-01-01
### Changed
- Updated dependencies.

## [ 1.2.2 ] - 2024-08-26
### Fixed
- Support of HEAD operation in case of resource delivery.

## [ 1.2.1 ] - 2024-07-28
### Added
- Added support to expand/resolve proxy hostname list by environment variables or java system properties.

## [ 1.2.0 ] - 2024-07-28
### Added
- Added proxy functionality with SSL suport.

### Changed
- Configuration refactoring.

## [ 1.1.6 ] - 2024-06-29
### Changed
- Updated dependencies.

## [ 1.1.5 ] - 2023-12-31
### Changed
- Updated dependencies.

## [ 1.1.4 ] - 2023-12-31
### Changed
- Updated dependencies.

## [ 1.1.3 ] - 2023-04-25
### Added
- Support file extensions.

### Fixed
- Proper handling resources with or without ending slash.

## [ 1.1.2 ] - 2023-04-24
### Changed
- Updated dependencies.
- Enhanced test cases.

### Fixed
- In some cases directory handling is not properly working.

## [ 1.1.1 ] - 2022-12-29
### Fixed
- Directory access issue with missing slahes by reading from classpah.

## [ 1.1.0 ] - 2022-12-28
### Added
- Access log support.
- Support for sub-resources.

## [ 1.0.1 ] - 2022-12-21
### Fixed
- lockback configuration.

## [ 1.0.0 ] - 2022-12-21
### Changed
- Stable version.
- Disabled access log.

## [ 0.9.0 ] - 2022-12-16
### Changed
- Refactoring base implementation.

## [ 0.3.0 ] - 2022-12-15
### Added
- Support of jwebserver.properties.
- Support of load from classpath.

## [ 0.2.0 ] - 2022-12-02
### Changed
- CLI support improved.

## [ 0.1.0 ] - 2022-11-25
### Added
- Support of load from local directory.
- Support for listing.
- Setup initial version.
