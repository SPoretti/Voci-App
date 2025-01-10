# NetworkManager [Class]

## Overview

`NetworkManager` is a class that provides a utility function to check if the device is currently connected to the internet. It utilizes the `ConnectivityManager` system service to determine the network status.

## Features

- **Network Connectivity Check:** Provides a function to determine if the device has an active internet connection.

## Key Components

- **`isNetworkConnected()`:** Checks if the device is connected to the internet. Returns `true` if connected, `false` otherwise.

## Known Limitations

- **Connectivity Status:** Reflects the current network status at the time of the call. Network changes might not be immediately reflected.

## Notes

- **Usage:** Can be used in various parts of the application to check for internet connectivity before performing network operations.
- **Permissions:** Requires the `ACCESS_NETWORK_STATE` permission.
- **Dependency Injection:** The class is annotated with `@Inject`, indicating that it can be used with dependency injection frameworks like Hilt or Dagger.