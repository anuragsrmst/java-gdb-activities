# Activity 21: Repository Pattern – In-Memory Storage & Factory

## Objective
Decouple domain business logic and entity lifecycles from data access mechanisms using the **Repository Pattern** and **Factory Pattern**. Implement thread-safe in-memory collections for `AccountRepository` and `TransactionRepository`, and provide a configurable `RepositoryFactory` to instantiate storage backends at runtime without altering core banking services.

---

## 🏗️ Architecture Diagram

```text
┌────────────────────────────────────────────────────────┐
│                   AccountService                       │
│    (coordinates domain accounts and transaction logs)   │
└───────────────┬────────────────────────┬───────────────┘
                │                        │
                ▼                        ▼
┌────────────────────────┐      ┌────────────────────────┐
│   «interface»          │      │   «interface»          │
│   AccountRepository    │      │   TransactionRepository│
│ (Single-Entity CRUD)   │      │ (Ledger & Multi-Acc)   │
└───────────────┬────────┘      └────────┬───────────────┘
                │                        │
                ▼                        ▼
┌────────────────────────┐      ┌────────────────────────┐
│InMemoryAccountRepo     │      │InMemoryTransactionRepo │
│(Map<Integer, IAccount>)│      │(List<Transaction>)     │
└────────────────────────┘      └────────────────────────┘
```

---

## Prerequisites
- Activity 20 completed: Banking domain models, `AccountFactory`, `AccountService`, and custom banking exceptions.

---

## What Is Already Done For You
| Component | Provided Functionality |
|---|---|
| `com.gdb.domain.*` | `Account`, `SavingsAccount`, `CurrentAccount`, `Transaction`, `TransactionType` domain models. |
| `com.gdb.exceptions.*` | `AccountException`, `InsufficientFundsException`, `InvalidPinException` domain exceptions. |

---

## Target Files to Complete
- `src/com/gdb/repository/AccountRepository.java` — Single-entity CRUD repository interface.
- `src/com/gdb/repository/TransactionRepository.java` — Ledger persistence and multi-account transfer interface.
- `src/com/gdb/repository/InMemoryAccountRepository.java` — Thread-safe in-memory map backing for accounts.
- `src/com/gdb/repository/InMemoryTransactionRepository.java` — Thread-safe in-memory list backing for transactions.
- `src/com/gdb/repository/RepositoryFactory.java` — Configurable factory creating repository instances.
- `src/com/gdb/service/AccountService.java` — Refactored service delegating to injected repositories.
- `src/com/gdb/tests/TestRepositoryInMemory.java` — Verification test suite.

---

## Step-by-Step Instructions

Search the code for `📝 STEP` — each step below matches one marked placeholder:

### STEPS 1–8 — `InMemoryAccountRepository.java`
1. **STEP 1**: Initialize `storage = new ConcurrentHashMap<>()` and `nextId = new AtomicInteger(1001)`.
2. **STEP 2 (`save`)**: Store `account` into `storage` with its account number.
3. **STEP 3 (`findById`)**: Return `storage.get(accountNumber)`.
4. **STEP 4 (`findAll`)**: Return `new ArrayList<>(storage.values())`.
5. **STEP 5 (`update`)**: Update `storage.put(account.getAccountNumber(), account)`.
6. **STEP 6 (`delete`)**: Remove `storage.remove(accountNumber)`.
7. **STEP 7 (`exists`)**: Return `storage.containsKey(accountNumber)`.
8. **STEP 8 (`nextAccountNumber`)**: Return `nextId.getAndIncrement()`.

---

## How to Compile & Run

### Windows (PowerShell)
```powershell
New-Item bin -ItemType Directory -Force | Out-Null
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java -ea -cp bin com.gdb.tests.TestRepositoryInMemory
```

### Linux & macOS (Bash)
```bash
mkdir -p bin
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -ea -cp bin com.gdb.tests.TestRepositoryInMemory
```

---

## Expected Output
```text
============================================================
  ACTIVITY 21 — REPOSITORY PATTERN IN-MEMORY TESTS (JAVA)
============================================================
[STEP 1-8] Verifying InMemoryAccountRepository CRUD...
  Saved: Account #1001 | Rajesh Sharma (30 yrs, Tenure: 0 yrs) | Savings | Rs. 50000.0 | Active
  Updated balance: Rs. 65000.0
  Total accounts in repository: 2

[STEP 9-14] Verifying InMemoryTransactionRepository...
  Found transactions for #1001: 2

[STEP 15] Verifying AccountService integration with repositories...
  Deposit completed. New balance: Rs. 25000.0

[STEP 16] All In-Memory Repository tests verified successfully!
```

---

## Verification Checklist
- [ ] `AccountRepository` contains pure single-entity CRUD operations.
- [ ] `TransactionRepository` manages transaction logging and transfers.
- [ ] All tests pass with assertions enabled (`-ea`).
