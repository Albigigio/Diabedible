# Diabedible

## Demo / Test Access

While the app is in testing, you can log in using any of the following demo users. All share the same demo password.

- Username: IDMario — Role: DIABETIC — Password: 123456
- Username: DRGiulia — Role: DOCTOR — Password: 123456
- Username: Admin1 — Role: ADMIN — Password: 123456

Notes:
- These users are defined in `src/main/resources/com/example/diabedible/users.txt` with a legacy SHA-256 hash. On first successful login, the app upgrades the password storage to a stronger PBKDF2 format automatically.
- The Login screen shows a hint with the demo credentials.
