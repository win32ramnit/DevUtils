# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog and this project follows Semantic Versioning.

## [1.0.0] - 2026-03-20

### Added

- core utility modules for validation, text, numbers, collections, IO, formatting, time, config, crypto, and banking calculations
- `BigDecimal`-based banking helpers including EMI, interest calculations, and amount-to-words conversion
- Indian business validators including PAN, IFSC, Aadhaar, GSTIN, and mobile-number validation
- masking utilities for email, mobile numbers, and account numbers
- hashing helpers for SHA-256, SHA-512, and HMAC-SHA256
- JavaDoc across the main library surface
- GitHub Actions CI workflow for automated test runs

### Changed

- Gradle wrapper upgraded to `9.3.1`
- project documentation rewritten for public GitHub use
- test suite aligned with masking and checksum behavior
