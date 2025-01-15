# Dependency Injection (DI) [Package]

The `di` package in the VociApp project is dedicated to managing dependency injection (DI). It provides a centralized and consistent mechanism for delivering dependencies to various parts of the application. For more information about the `ServiceLocator`, refer to [ServiceLocator Documentation](Service-Locator.md).

## Purpose of Dependency Injection
Dependency Injection is a software design pattern aimed at:
- Reducing tight coupling between classes.
- Improving code modularity and testability.
- Simplifying the management and configuration of dependencies.

Instead of components creating their own dependencies, these are provided externally, allowing for greater flexibility and adherence to the Single Responsibility Principle.

## Role of the `di` Package
The `di` package:
- Serves as the home for all DI-related implementations.
- Provides an alternative to external DI frameworks, adhering to project constraints.
- Ensures that dependencies are initialized and accessed in a consistent and reliable manner.

## Overview of Approach
In this project, the DI functionality is implemented using a Service Locator pattern. This allows for a centralized registry to manage the initialization and retrieval of dependencies without relying on external libraries such as Hilt or Dagger. This approach enables the application to:
- Maintain a single source of truth for dependencies.
- Decouple the creation and usage of objects.
- Simplify testing through the use of mock implementations.

## Conclusion
The `di` package is a critical part of the VociApp project, offering a structured approach to dependency management. Its implementation ensures modularity, testability, and consistency across the application, aligning with the project's technical constraints and goals.

