package com.example.vociapp.di

import androidx.compose.runtime.compositionLocalOf


val LocalServiceLocator = compositionLocalOf<ServiceLocator> {
    error("ServiceLocator not provided")
}