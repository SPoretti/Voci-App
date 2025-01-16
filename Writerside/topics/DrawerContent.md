# DrawerContent Composable Documentation

## Overview
`DrawerContent` is a Jetpack Compose function that defines the UI for a drawer menu. It provides navigation options for the application and includes branding elements like a logo.

---

## Function Definition
```kotlin
@Composable
fun DrawerContent ( // Navigation controller for navigating between screens
    navController: NavHostController    
    )
```

### Parameters:
- `navController`: An instance of `NavHostController` used to manage and navigate between screens in the application.

---

## UI Structure

### Layout
1. **Row**: The root container fills the width of the screen.
    - **Left Section**: Occupies 3/4 of the width.
        - **Box**: Contains a column with logo and navigation links.
        - **Column**:
            - **Logo**: Displays the application logo using an `Image` composable.
            - **Horizontal Divider**: A visual separator below the logo.
            - **Navigation Links**:
                - "Home": Navigates to the `home` screen.
                - "Mappa Senzatetto": Navigates to the `HomelessesMap/` screen.

2. **Right Section**: Occupies 1/4 of the width but is left empty.

---

## Key Components

### Logo
- **Image**:
    - Resource: `R.drawable.voci_logo`
    - Size: `128.dp`
    - Description: "Logo VoCi"

### Navigation Links
- **Home**:
    - Icon: `Icons.Default.Home`
    - Action: Navigates to the `home` screen.
- **Mappa Senzatetto**:
    - Icon: `Icons.Default.Map`
    - Action: Navigates to the `HomelessesMap/` screen.

---

## Code Example
```kotlin
@Composable
fun DrawerContent(
    navController: NavHostController
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.voci_logo
                        ),
                        contentDescription = "Logo VoCi",
                        modifier = Modifier.size(128.dp)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                NavigationLink(
                    text = "Home",
                    icon = Icons.Default.Home
                ) {
                    navController.navigate("home")
                }
                NavigationLink(
                    text = "Mappa Senzatetto",
                    icon = Icons.Default.Map
                ) {
                    navController.navigate("HomelessesMap/")
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {}
    }
}
```
