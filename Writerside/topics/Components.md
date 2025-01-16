# Components [Package]

The `Components` package contains all the building blocks required for the various screens of the application. It is organized into multiple sub-packages, each focusing on a specific feature or functionality. These components are designed to promote reusability, modularity, and consistency throughout the application.

## Structure

Below is the general structure of the `Components` package. Each sub-package corresponds to a particular feature or domain, encapsulating all related components.

```
VOCIAPP\APP\SRC\MAIN\JAVA\COM\EXAMPLE\VOCIAPP\UI\COMPONENTS
├───core
│       BottomBar.kt
│       CustomChip.kt
│       CustomFAB.kt
│       DrawerContent.kt
│       HapticModifier.kt
│       NavigationLink.kt
│       Screens.kt
│       SearchBar.kt
│       StatusLED.kt
│
├───homeless
│       AddHomelessDialog.kt
│       GenderSelector.kt
│       HomelessDialogList.kt
│       HomelessList.kt
│       HomelessListItem.kt
│
├───maps
│       MapOnDevice.kt
│       MultiPointMap.kt
│
├───requests
│       AddRequestDialog.kt
│       DialogSearchBar.kt
│       IconMapping.kt
│       IconSelector.kt
│       ModifyRequestDialog.kt
│       RequestList.kt
│       RequestListItem.kt
│       SortButtons.kt
│
├───updates
│       ButtonOption.kt
│       FormText.kt
│       StatusButtonData.kt
│       UpdateButton.kt
│
└───volunteers
        AuthTextField.kt
        ProfileInfoItem.kt
```

## Sub-Packages

### Volunteers
This package contains components related to managing and displaying volunteers.

### Core
The `Core` package includes fundamental components and utilities used across the application. Examples include:
- **Bottom Bar**: Navigation component for switching between primary screens.
- **Custom FAB**: A floating action button tailored to the app's design.
- **Custom Chip**: A reusable chip component for tags and filters.
- **Search Bar**: A search input bar used across multiple screens.
- **Status LED**: A visual indicator for statuses.
- **Screens**: Base templates or utilities for screen layouts.

### Homeless
The `Homeless` package focuses on components for managing and displaying homeless individuals. It includes:
- Dialogs for adding and listing homeless individuals.
- List and item components for displaying information.

### Maps
This package provides map-related components, such as:
- **Map On Device**: Display a single point on the map.
- **Multi Point Map**: Show multiple points for mapping data.

### Requests
The `Requests` package encapsulates components for handling user requests. Examples include:
- Dialogs for adding and modifying requests.
- List and item components for displaying request data.
- Sorting buttons and icon selectors for enhanced functionality.

## Usage
These components are designed to be imported and used in the various screens of the application. They ensure that UI and functionality remain consistent and easy to maintain.
