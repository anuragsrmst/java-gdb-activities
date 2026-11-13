# Activity 23: JDBC Account Repository (CRUD Operations)

## Objective
Persist domain `Account` entities to disk using parameterized SQL queries with Java Database Connectivity (`JDBC`). Implement `JdbcAccountRepository` conforming to `AccountRepository` to perform complete single-entity CRUD operations, preventing SQL injection vulnerabilities and mapping relational result sets to rich domain entities.

---

## 🏗️ Architecture Diagram

```text
┌────────────────────────────────────────────────────────┐
│                   AccountService                       │
└───────────────────────────┬────────────────────────────┘
                            │ invokes CRUD
                            ▼
┌────────────────────────────────────────────────────────┐
│                   AccountRepository                    │
│                     («interface»)                      │
│     save, findById, findAll, update, delete, exists    │
└───────────────────────────┬────────────────────────────┘
                            │ implements
                            ▼
┌────────────────────────────────────────────────────────┐
│                 JdbcAccountRepository                  │
│       (PreparedStatement + ResultSet Data Mapping)     │
└───────────────────────────┬────────────────────────────┘
                            │ executes SQL
                            ▼
┌────────────────────────────────────────────────────────┐
│                  SQLite: `accounts`                    │
└────────────────────────────────────────────────────────┘
```

---

## Prerequisites
- Activity 22 completed: SQLite connection and schema initialization with JDBC.

---

## Target Files to Complete
- `src/com/gdb/repository/JdbcAccountRepository.java` — Concrete relational repository for accounts.
- `src/com/gdb/tests/TestJdbcAccountRepository.java` — CRUD verification test suite.

---

## Step-by-Step Instructions

Search the code for `📝 STEP` — each step below matches one marked placeholder:

### STEPS 1–7 — `JdbcAccountRepository.java`
1. **STEP 1 (`save`)**: Parameterized `INSERT INTO accounts (account_number, account_holder_name, age, balance, account_type, status, pin, opening_date, overdraft_used, interest_earned) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`.
2. **STEP 2 (`findById`)**: Parameterized `SELECT * FROM accounts WHERE account_number = ?`. Map `ResultSet` to `Account` via `AccountFactory`.
3. **STEP 3 (`findAll`)**: Parameterized `SELECT * FROM accounts`. Map all rows to `List<IAccount>`.
4. **STEP 4 (`update`)**: Parameterized `UPDATE accounts SET balance = ?, status = ?, overdraft_used = ?, interest_earned = ? WHERE account_number = ?`.
5. **STEP 5 (`delete`)**: Parameterized `DELETE FROM accounts WHERE account_number = ?`.
6. **STEP 6 (`exists`)**: Parameterized `SELECT 1 FROM accounts WHERE account_number = ?`. Return `rs.next()`.
7. **STEP 7 (`nextAccountNumber`)**: `SELECT COALESCE(MAX(account_number), 1000) + 1 FROM accounts`.

---

## How to Compile & Run

### Windows (PowerShell)
```powershell
New-Item bin -ItemType Directory -Force | Out-Null
javac -encoding UTF-8 -cp "lib/sqlite-jdbc.jar;lib/slf4j-api.jar;lib/slf4j-simple.jar" -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java -ea -cp "bin;lib/sqlite-jdbc.jar;lib/slf4j-api.jar;lib/slf4j-simple.jar" com.gdb.tests.TestJdbcAccountRepository
```

### Linux & macOS (Bash)
```bash
mkdir -p bin
javac -encoding UTF-8 -cp "lib/sqlite-jdbc.jar:lib/slf4j-api.jar:lib/slf4j-simple.jar" -d bin $(find src -name "*.java")
java -ea -cp "bin:lib/sqlite-jdbc.jar:lib/slf4j-api.jar:lib/slf4j-simple.jar" com.gdb.tests.TestJdbcAccountRepository
```

---

## Expected Output
```text
============================================================
  ACTIVITY 23 — JDBC ACCOUNT REPOSITORY CRUD TESTS
============================================================
[STEP 1-2] Verifying save() and findById()...
  Found: Account #1001 | Rajesh Sharma (30 yrs, Tenure: 0 yrs) | Savings | Rs. 50000.0 | Active

[STEP 3-4] Verifying update() and findAll()...
  Updated balance: Rs. 65000.0
  Total accounts in DB: 2

[STEP 5-7] Verifying delete() and exists()...
  Account 1002 successfully deleted.

[STEP 8] All JDBC Account Repository CRUD tests verified successfully!
```

---

## Verification Checklist
- [ ] Parameterized queries (`?` placeholders) used across all SQL operations to prevent SQL injection.
- [ ] Resource cleanup handled deterministically using try-with-resources.
- [ ] `JdbcAccountRepository` implements strictly single-entity CRUD operations.
