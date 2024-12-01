# Data

## Overview

The `data` folder is a core part of the `VociApp` project architecture. 
It contains classes and resources that handle data access, manipulation, and utility functions. 
Below is a breakdown of its structure and responsibilities.

## Folder Structure

```
\VociApp\app\src\main\java\com\example\vociapp\data
├── remote
│   └── FirestoreDataSource.kt
│   ├── repository
│   │   └── RequestRepository.kt
│   ├── types
│   │   ├── AuthState.kt
│   │   ├── Homeless.kt
│   │   ├── Request.kt
│   │   ├── RequestTab.kt
│   │   └── UserData.kt
│   └── util
│       ├── Resource.kt
│       └── SortOption.kt
```

---

## Contents and Responsibilities

### 1. **`remote`**
- **File**: `FirestoreDataSource.kt`
- **Purpose**: Handles interactions with the Firestore database, including data fetching and synchronization.
- **Responsibilities**:
    - Abstracting database calls.
    - Providing data access points for the repository layer.

---

### 2. **`repository`**
- **File**: `RequestRepository.kt`
- **Purpose**: Acts as a bridge between the data source (`remote`) and the application logic.
- **Responsibilities**:
    - Encapsulates logic to process and fetch data.
    - Provides a clean API for ViewModels to consume.

---

### 3. **`types`**
This subfolder contains data models and enums used across the app.

- **Files**:
    - `AuthState.kt`: Represents different authentication states.
    - `Homeless.kt`: Model for homeless profile data.
    - `Request.kt`: Model for a request entity.
    - `RequestTab.kt`: Enum for categorizing request tabs.
    - `UserData.kt`: Model for user profile data.

- **Purpose**: Define reusable and type-safe data structures.
- **Responsibilities**:
    - Maintaining consistent data formats.
    - Supporting type-safe operations across the app.

---

### 4. **`util`**
This subfolder includes utility classes and enums to support the app's functionality.

- **Files**:
    - `Resource.kt`: Wraps data states (e.g., success, error, loading) for safe asynchronous handling.
    - `SortOption.kt`: Enum defining sorting options for request lists.

- **Purpose**: Provide reusable utilities to simplify common tasks.
- **Responsibilities**:
    - Standardizing data handling practices.
    - Reducing repetitive code in other parts of the app.

---

## Summary

The `data` folder in the `VociApp` project encapsulates all logic related to data management. It is structured in a modular fashion, separating concerns into:
- **Data access** (`remote`),
- **Data processing** (`repository`),
- **Data definitions** (`types`), and
- **Utilities** (`util`).

This organization ensures scalability, maintainability, and a clear separation of responsibilities.
