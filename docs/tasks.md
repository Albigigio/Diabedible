# Diabedible – Improvement Tasks Checklist

Date generated: 2025-09-16 09:40

Note: Each task is actionable and ordered for incremental adoption. Check items off as you complete them.

1. [x] Establish coding standards and linters (Checkstyle/SpotBugs/EditorConfig) and add their configs to the repo.
2. [x] Replace System.out/err prints with a logging framework (SLF4J + Logback) and define a basic logging policy.
3. [x] Centralize application configuration (e.g., application.properties) and a Config class to read it.
4. [x] Fix FXML path case mismatch: resource folder uses `Views/Admin` while `FXMLPaths` constant is `admin` – align naming consistently.
5. [x] Remove CSS loading duplication: choose only ViewManager or Main to load CSS, not both.
6. [x] Introduce a Role concept for users (ADMIN/DOCTOR/DIABETIC) instead of username prefixes to route to views.
7. [x] Introduce a simple dependency injection approach (manual module or lightweight DI like Guice/Spring-lite) to construct controllers and services.
8. [x] Define service interfaces (e.g., AuthService) and decouple controllers from concrete implementations.
9. [x] Abstract persistence behind repository interfaces (e.g., UserRepository) instead of reading a flat users.txt file directly.
10. [x] Replace users.txt with a more secure storage (e.g., in-memory repo now, then H2/SQLite later) and provide data migration path.
11. [ ] Strengthen password hashing (use salted, iterated hashing via PBKDF2/BCrypt/Argon2) and store salt + hash.
12. [ ] Remove debug prints from LoginService; add structured logs and meaningful exceptions.
13. [ ] Add validation utilities for user input (non-empty, min/max length, allowed chars) and apply in LoginController before calling service.
14. [ ] Define a global error handling policy for UI (alerts) and service (exceptions) layers; centralize alert creation in a small UI helper.
15. [ ] Ensure all controller actions run UI updates on the JavaFX Application Thread (Platform.runLater if needed for async ops).
16. [ ] Extract magic numbers/strings to constants (e.g., chart thresholds in HomeDiabeticController, labels, time slots).
17. [ ] Introduce a domain model for readings (e.g., BloodSugarReading {LocalDate date, TimeSlot slot, double value}) and collections.
18. [ ] Refactor HomeDiabeticController to use the domain model + a ViewModel (MVVM-like) to separate UI from logic.
19. [ ] Add a TimeSlot enum (MORNING, AFTERNOON) with localized display names instead of raw strings ("Mattina", "Pomeriggio").
20. [ ] Convert dummy/sample data generation into a dedicated DemoDataProvider that can be toggled by config.
21. [ ] Normalize date handling and formatting via a DateTimeUtil with locale-specific patterns.
22. [ ] Add i18n support (resource bundles) for UI text and messages; externalize Italian strings from controllers.
23. [ ] Create a Navigation model: single source of truth for route names, FXML paths, window titles, and sizes.
24. [ ] Make ViewManager responsible only for navigation; remove business logic (e.g., hard-wired logout LoginService creation) via DI.
25. [ ] Add a ControllerFactory that injects dependencies into FXML controllers (via FXMLLoader.setControllerFactory).
26. [ ] Add nullability annotations (e.g., JetBrains @NotNull/@Nullable) and enforce in code where appropriate.
27. [ ] Ensure robust resource loading with clear fallbacks and logging when resources are missing.
28. [ ] Expand the User model (id, username, role, displayName, optional metadata) and avoid relying on prefixes.
29. [ ] Create a session/context object (e.g., AppSession) to store the current user and shared state.
30. [ ] Implement logout flow that clears session and returns to login reliably; remove duplicated safeguards in controllers.
31. [ ] Add unit tests for LoginService (valid user, invalid user, corrupted file, empty file) using JUnit 5 and Mockito.
32. [ ] Add unit tests for HashUtils (hash determinism, salt/BCrypt transition) or new PasswordHasher abstraction.
33. [ ] Add unit tests for ViewManager navigation (using TestFX or mocked FXMLLoader factory) where feasible.
34. [ ] Add UI tests for login flow (valid/invalid) using TestFX.
35. [ ] Add integration test for end-to-end login + navigation to role-specific home view (smoke test).
36. [ ] Configure Maven Surefire/FailSafe plugins to run unit and integration tests properly.
37. [ ] Add CI (GitHub Actions) to run build, linters, and tests on push/PR.
38. [ ] Add Maven Shade or JavaFX maven plugin config for packaging a runnable artifact.
39. [ ] Review module-info.java: ensure required opens/exports for JavaFX reflection and controller packages.
40. [ ] Introduce a lightweight event bus or observer for cross-component notifications (e.g., checklist updates).
41. [ ] Replace ad-hoc checklist UI population with a model-driven list and bindings; persist completion state per day.
42. [ ] Improve charting: compute average/min/max series in a service; handle missing data and outliers; add tooltips.
43. [ ] Validate reading inputs in HomeDiabeticController (ranges, numeric parsing, duplicates per timeslot) with user feedback.
44. [ ] Add accessibility considerations: focus traversal, labels for inputs, high-contrast themes.
45. [ ] Extract CSS class names into a style guide; consolidate styles.css and remove inline styles from code.
46. [ ] Add resource management utilities to close streams and handle IO exceptions gracefully.
47. [ ] Introduce DTOs and mappers if/when introducing persistence to avoid leaking DB types to UI.
48. [ ] Add a simple repository for blood sugar readings (in-memory for now) and wire controllers to it.
49. [ ] Add a seed command or dev profile to populate demo users/readings on dev builds.
50. [ ] Replace magic window sizes with a centralized WindowLayout config; support remembering last window size/state.
51. [ ] Add consistent exception hierarchy (AppException, ValidationException, NotFoundException) and usage guidelines.
52. [ ] Implement a simple feature flag mechanism (e.g., via config) for toggling demo features.
53. [ ] Document the architecture (layers, responsibilities) in README or docs/architecture.md.
54. [ ] Add CONTRIBUTING.md with development setup, code style, commit conventions, and PR checklist.
55. [ ] Add ADRs (Architecture Decision Records) for key choices (DI approach, persistence, hashing, navigation strategy).
56. [ ] Introduce a properties-based localization for titles of windows and menus; update FXML to use resourceBundle.
57. [ ] Add defensive checks in FXML loader paths and a startup self-check that verifies resources availability.
58. [ ] Consolidate constants (FXML paths, CSS paths) in one place; ensure testability with indirection if needed.
59. [ ] Ensure thread-safety of services and repositories; document threading expectations (UI vs background).
60. [ ] Add background task patterns (Service/Task) for any future IO bound operations (e.g., loading history) with progress indicators.
61. [ ] Add a smoke test profile that launches the app headless (Monocle/TestFX) for basic regressions.
62. [ ] Create error reporting: capture uncaught exceptions and show a user-friendly dialog with copyable details.
63. [ ] Implement a Settings view to change language/theme; persist preferences using Java Preferences or a JSON file.
64. [ ] Refactor package structure to follow feature or layer-based grouping consistently.
65. [ ] Replace hard-coded Italian strings in code with constants or i18n keys; keep one language per bundle.
66. [ ] Add code comments and JavaDoc on public methods and non-trivial logic for maintainability.
67. [ ] Verify and fix any resource path case-sensitivity issues for cross-platform compatibility.
68. [ ] Remove unneeded classes or placeholders (e.g., empty HomeAdminController) or flesh them out with minimal functionality.
69. [ ] Add a Security review checklist (password policy, lockout policy, logging of auth events, no sensitive data in logs).
70. [ ] Plan data persistence evolution (file -> embedded DB) and create a migration script or import/export format.
