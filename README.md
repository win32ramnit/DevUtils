# DevUtils

DevUtils is an open Java utility library for common production-oriented application code. It
collects small, reusable helpers for validation, text processing, numeric work, banking
calculations, formatting, date/time handling, configuration lookup, IO, collections, and crypto so
projects do not need to reimplement the same utility layer repeatedly.

## Why This Repository Exists

This repository is intended to be a practical reference and reusable utility pack for Java
developers. The focus is straightforward:

- keep common helper logic consistent
- prefer simple, testable APIs over framework-heavy abstractions
- use `BigDecimal` where financial precision matters
- document behavior clearly so utilities are safe to reuse in production code

## Project Details

- Group: `org.manish.utilix`
- Version: `1.0.0`
- Build plugin: `java-library`
- Gradle wrapper: `9.3.1`
- Artifact output: main jar, sources jar, and javadoc jar
- Bytecode target: Java 8 via `options.release = 8`
- Build runtime: JDK 17 or newer is recommended for Gradle 9.x

## Modules

### `org.manish.utilix.validation`

- `Checks`: argument validation helpers such as `notNull`, `notBlank`, `positive`, and `nonNegative`
- `PANValidator`: Indian PAN format validation
- `EmailValidator`: pragmatic business-email validation
- `IndianMobileValidator`: Indian mobile number validation with common country-code forms
- `IFSCValidator`: Indian Financial System Code validation
- `AadhaarValidator`: Aadhaar validation with checksum verification
- `GSTINValidator`: GSTIN format and checksum validation

### `org.manish.utilix.text`

- `StringUtils`: null-safe string helpers, blank checks, trim helpers, abbreviation, snake case, and kebab case conversion
- `MaskingUtils`: masking helpers for emails, mobile numbers, account numbers, and generic strings

### `org.manish.utilix.numbers`

- `NumberUtils`: parsing for `int`, `long`, `double`, and `BigDecimal`
- primitive range clamping
- rounding helpers for both `double` and `BigDecimal`

### `org.manish.utilix.banking`

`BankingCalculations` provides `BigDecimal`-based helpers for:

- percentage calculations
- markup and discount calculations
- simple interest
- compound amount and compound interest
- EMI calculation
- total repayment and total interest
- decimal-to-words conversion
- amount-to-words conversion in rupees and paise

### `org.manish.utilix.formatting`

- `FormatUtils`: formatting helpers for decimal values, `LocalDate`, and `Instant`

### `org.manish.utilix.time`

- `DateTimeUtils`: ISO parsing/formatting, epoch conversion, and UTC-oriented helpers

### `org.manish.utilix.collections`

- `CollectionUtils`: null-safe collection/map checks and shallow defensive copies

### `org.manish.utilix.io`

- `IOUtils`: stream copy plus byte-array and string extraction helpers

### `org.manish.utilix.crypto`

- `AES256Encryption`: AES encryption/decryption with env or system-property configuration
- `AES512Encryption`: deprecated legacy API name retained for backward compatibility
- `HashUtils`: SHA-256, SHA-512, and HMAC-SHA256 helpers

### `org.manish.utilix.config`

- `ConfigUtils`: environment-variable and system-property lookup helpers with parsing support

### `org.manish.utilix.conversion`

- `PrecisionExample`: sample program showing the recommended rounding approach
- `RoundDouble`: deprecated legacy wrapper around `NumberUtils.round(...)`

## Quick Start

Clone the repository and run the test suite:

```bash
git clone <your-repo-url>
cd DevUtils
./gradlew test
```

Build jars and generated docs:

```bash
./gradlew clean build
./gradlew javadoc
```

## Usage Example

```java
import java.math.BigDecimal;
import org.manish.utilix.banking.BankingCalculations;
import org.manish.utilix.config.ConfigUtils;
import org.manish.utilix.formatting.FormatUtils;
import org.manish.utilix.numbers.NumberUtils;
import org.manish.utilix.text.MaskingUtils;
import org.manish.utilix.text.StringUtils;
import org.manish.utilix.validation.EmailValidator;

BigDecimal emi = BankingCalculations.emi(
    new BigDecimal("2500000.00"),
    new BigDecimal("8.75"),
    240
);

String amountInWords = BankingCalculations.amountToWords(new BigDecimal("1234567.89"));
BigDecimal rounded = NumberUtils.round(new BigDecimal("10.345"), 2);
String slug = StringUtils.toKebabCase("Customer Account Statement");
boolean validEmail = EmailValidator.isValidEmail("ops@example.com");
String maskedMobile = MaskingUtils.maskMobileNumber("+91 98765 43210");
boolean featureEnabled = ConfigUtils.getBooleanEnvOrProperty("FEATURE_ENABLED", false);
String formattedDate = FormatUtils.formatLocalDate(
    java.time.LocalDate.of(2026, 3, 20),
    "dd/MM/yyyy"
);
```

Example output:

- `emi`: `21998.18`
- `amountInWords`: `Twelve Lakh Thirty Four Thousand Five Hundred Sixty Seven Rupees and Eighty Nine Paise Only`
- `rounded`: `10.35`
- `slug`: `customer-account-statement`
- `validEmail`: `true`
- `maskedMobile`: `+** ***** *3210`
- `featureEnabled`: `false`

## Quality and Testing

- Test framework: JUnit 5
- Runtime launcher: `org.junit.platform:junit-platform-launcher`
- CI: GitHub Actions workflow included in `.github/workflows/ci.yml`
- Current local status: `./gradlew test` passes

## Crypto Configuration

`AES256Encryption` and `AES512Encryption` resolve configuration from system properties for testing
and environment variables for deployment.

Supported keys:

- `ENCRYPTION_215_MODE`
- `AES_SECRETE_KEY` (legacy)
- `AES_SECRET_KEY`

Example encryption modes:

- `AES/CBC/PKCS5Padding`
- `AES/GCM/NoPadding`

For explicit wiring, use the overloads that accept `secretKey` and `encryptionMode` directly.

## Contributing

Contributions are welcome. Please read `CONTRIBUTING.md` before opening a pull request so changes
stay consistent with the project structure and testing expectations.

## Security

If you find a security issue, please follow `SECURITY.md` instead of opening a public issue first.

## License

This repository is licensed under the Apache License 2.0. See `LICENSE` for details.

## Notes

- `AES512Encryption` is a deprecated legacy name. It still derives a SHA-256-based AES key and does not represent a true 512-bit AES algorithm.
- `conversion` contains legacy or example classes. For real application code, prefer `NumberUtils` and `BankingCalculations`.
- Financial calculations are implemented with `BigDecimal` to avoid floating-point drift in banking-style workflows.
