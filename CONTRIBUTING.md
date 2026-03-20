# Contributing to DevUtils

Thanks for contributing.

## Development Setup

1. Use JDK 17 or newer to run Gradle.
2. Run `./gradlew test` before submitting changes.
3. Keep the library compatible with Java 8 bytecode unless the project versioning policy changes.

## Contribution Guidelines

- Prefer small, focused pull requests.
- Add or update tests for any behavior change.
- Add JavaDoc for new public classes and public methods.
- Keep utility APIs simple and dependency-light.
- Do not introduce framework-specific helpers unless they are clearly justified.
- Avoid breaking existing method contracts without documenting the change.

## Code Style

- Follow the existing package structure.
- Prefer descriptive method names over clever abstractions.
- Use `BigDecimal` for money and precision-sensitive calculations.
- Keep validation behavior explicit and deterministic.

## Pull Requests

Please include:

- what changed
- why the change is needed
- any compatibility impact
- test coverage summary
