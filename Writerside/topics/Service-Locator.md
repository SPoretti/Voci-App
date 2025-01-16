# ServiceLocator [Service]

## Overview
The `ServiceLocator.kt` file is a custom implementation of a Service Locator pattern designed to manage dependency injection for the VociApp project. Since the use of Hilt or other DI frameworks was restricted by the project guidelines, this class provides a centralized way to initialize and retrieve dependencies such as repositories, view models, and other services within the app.

The `ServiceLocator` ensures that all components requiring dependencies have access to pre-configured and properly initialized instances, avoiding repetitive initialization logic across the app.

---

## Purpose of Service Locator
### Dependency Injection Without Hilt
Dependency injection is a design pattern used to reduce coupling between classes and promote reusability and testability. Typically, frameworks like Hilt or Dagger handle this process automatically, but here, the Service Locator pattern was chosen to provide a similar functionality without relying on external libraries.

### How It Works
The Service Locator acts as a centralized registry where:
- Dependencies are created and managed.
- Components can fetch these dependencies on demand.

By doing this, it separates the creation of objects from their usage, simplifying the structure of the app.

---

## Structure of the File

### `ServiceLocator` Class
The `ServiceLocator` is designed as a singleton, ensuring a single instance manages all dependencies:

- **Initialization:**
    - `initialize(context: Context, firestore: FirebaseFirestore)` initializes the singleton instance and essential dependencies.
    - Throws an exception if accessed before initialization.

- **Repositories and View Models:**
    - Dependencies such as `HomelessRepository` or `VolunteerViewModel` are instantiated in the `ServiceLocator` using lazy initialization and provided on demand through getter methods.

- **Network Management:**
    - Manages the `NetworkManager` to provide information about the network state and status.

### Key Methods
- **Getters for Dependencies:** Methods like `obtainHomelessRepository()` or `obtainVolunteerViewModel()` return the corresponding initialized instances.
- **Data Fetching:** The `fetchAllData()` method simplifies fetching of all necessary data by invoking the `fetch` methods of the respective view models.

---

## Integration in the Application

### `CompositionLocalProvider`
To integrate the `ServiceLocator` into Jetpack Compose, it is provided via a `CompositionLocalProvider`:
```kotlin
CompositionLocalProvider(LocalServiceLocator provides serviceLocator) {
    // Your Composable content
}
```
This allows the `ServiceLocator` to be accessed throughout the composable hierarchy using the `LocalServiceLocator`.

### Composition Local Definition
The `LocalServiceLocator` is defined as:
```kotlin
val LocalServiceLocator = compositionLocalOf<ServiceLocator> {
    error("ServiceLocator not provided")
}
```
This ensures that any attempt to access `LocalServiceLocator` without a proper provider will result in an error, promoting safe usage.

---

## Benefits of the Service Locator Pattern

### Advantages
- **Centralized Management:** All dependencies are defined and managed in a single place, reducing redundancy.
- **Flexibility:** Changes to dependency initialization (e.g., switching implementations) can be done in one place.
- **Simplifies Testing:** Mock dependencies can be injected into the `ServiceLocator` for testing purposes.

### Limitations
- **Tight Coupling:** Components directly depend on the `ServiceLocator`, which can make refactoring or scaling harder compared to frameworks like Hilt.
- **Manual Initialization:** Developers must ensure that the `ServiceLocator` is properly initialized, as failing to do so will result in runtime exceptions.

---

## Usage Example
Here is a high-level example of how dependencies are provided and accessed:

### Initialization in Application Class
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initialize(this, FirebaseFirestore.getInstance())
    }
}
```

### Using Dependencies in Composables
```kotlin
@Composable
fun HomelessScreen() {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()

    // Use the view model in the composable
    // ...
}
```

---

## Conclusion
The `ServiceLocator.kt` file provides a robust and flexible solution for managing dependency injection in the VociApp project. While it requires more manual setup and management compared to using Hilt, it aligns with the project constraints and effectively simplifies dependency management across the app.

This approach demonstrates a good understanding of software design patterns and offers a practical alternative when external frameworks cannot be used.

