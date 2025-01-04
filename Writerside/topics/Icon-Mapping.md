# Icon [Mapping]

## IconCategory to Icons Mapping

### Purpose
This mapping defines the relationship between `IconCategory` enums and their corresponding drawable resource IDs. It is used in components like `IconSelector` to display appropriate icons based on the selected category.

### Mapping Table

| Icon Category | Icon Resource ID        |
|---------------|-------------------------|
| `SHOES`       | `R.drawable.shoes_icon` |
| `PANTS`       | `R.drawable.pants_icon` |
| `SHIRT`       | `R.drawable.shirt_icon` |
| `OTHER`       | `R.drawable.more_icon`  |

### Notes
- Each `IconCategory` enum is mapped to a drawable resource ID, which represents the corresponding icon.
- The mapping is utilized to maintain a clean separation of concerns, ensuring that the logic for determining which icon to display remains centralized and easily maintainable.
- Extend this map to include additional categories or icons as needed.
- This mapping is integral to UI components that dynamically render icons based on user selection or data-driven logic.
