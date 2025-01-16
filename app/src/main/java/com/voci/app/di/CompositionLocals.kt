package com.voci.app.di

import androidx.compose.runtime.compositionLocalOf


val LocalServiceLocator = compositionLocalOf<ServiceLocator> {
    error("ServiceLocator not provided")
}