# Voci app Documentation

![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)

## Introduction

### Overview
This project was created as an assignment for the University of Milano Bicocca course "Dispositivi Mobili".

### Idea
The idea is to create an app that would help catalog and manage requests and updates about homeless people in Milan. It was born because of a member of the team who is also a member of the VoCi ONLUS, an organization that aims to aid the homeless people of their city.
Website: [VoCi ONLUS](https://www.volontaricittadini.it)


### Team
We are students of the University of [Milano Bicocca](https://www.unimib.it/), and we are interested in making an app that not only will get us a good mark but that can be used and can be helpful to a good cause.
- Samuele "DreoX" - 904280
- Matthias "Inutiliax" - 894374
- Oltion "Olcio" - 902221
- Enrico "Enz" - 899376
- Gabriele "Fish" - 899638

## Technologies

### IDEs
We used Android Studio to write and test the code and Writerside to write the documentation. Both come from a well-established company in the coding world called "JetBrains". Android Studio is built in conjunction with Google.

### Libraries
Our app is primarily written in Kotlin, a modern, concise, and type-safe programming language designed for Android development. It leverages various libraries to enhance functionality:

- **Jetpack**: A suite of libraries from Google that simplify common Android development tasks. This includes libraries for navigation, lifecycle management, UI components, and more.

- **Material Design 3**: The latest iteration of Google's Material Design system for Android. It provides a consistent and beautiful design language with pre-built components like buttons, text fields, and cards.

- **Gradle**: A build automation tool that manages dependencies, builds your app, and packages it for distribution.

- **Firebase**: Google's mobile app development platform. We utilize Firebase for various purposes:
  - **Firestore**: A NoSQL cloud database that provides flexible and scalable data storage for app data.
  - **Authentication**: Simplifies user authentication and management, allowing users to sign in with various methods like email/password or social logins.

- **Git & GitHub**: Tools for version control and collaborative development. We chose GitHub due to prior familiarity and ease of integration with our workflow. (As a side note, while GitLab was an alternative, we leaned towards GitHub for its extensive ecosystem.)

- **Mapbox**: A versatile mapping platform. We use Mapbox SDK for maps, the search API for location suggestions, and geocoding for converting between coordinates and addresses.

## Graphs

### Layer Diagram

The layer diagram illustrates the architectural structure of the application, detailing the relationships and interactions between its various layers.

![Layer Diagram](LayerDiagram.png)

### Flow Diagram

The flow diagram depicts the navigation flow within the application, outlining all possible user pathways between different screens.

![Nav Graph](NavGraph.png)

