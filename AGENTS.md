# AGENTS.md

## Project Overview

**Compose Lightsaber** is a toy lightsaber app built for kids using Compose Multiplatform and
Circuit architecture. The app simulates a lightsaber with visual blade animations, authentic sound
effects, motion-based swing detection, and customizable blade colors.

### Key Features

- ✨ Animated lightsaber blade with activation/deactivation sequences
- 🎵 Authentic Star Wars lightsaber sound effects (activation, idle hum, swing sounds, clashing)
- 📱 Motion detection for swing interactions using device accelerometer
- 🎨 Customizable blade colors (Green, Red, Yellow, Blue, Purple)
- 💾 Persistent user settings using DataStore
- 🔄 Cross-platform support (Android & iOS)

## Architecture

### Tech Stack

- **UI Framework**: Compose Multiplatform
- **Architecture**: Circuit 
- **Dependency Injection**: kotlin-inject 
- **State Management**: Kotlin StateFlow + DataStore for persistence
- **Audio**: Platform-specific sound players (MediaPlayer on Android, AVAudioPlayer on iOS)
- **Motion Detection**: Platform-specific accelerometer implementations
- **Build System**: Kotlin Multiplatform with Gradle 

### Project Structure

```
compose-lightsaber/
├── androidApp/                    # Android-specific application module
│   └── src/androidMain/
│       ├── kotlin/xyz/alaniz/aaron/lightsaber/
│       │   └── MainActivity.kt    # Android entry point
│       └── AndroidManifest.xml
├── iosApp/                        # iOS-specific application
│   ├── iosApp/
│   │   ├── ContentView.swift      # SwiftUI wrapper for Compose
│   │   └── iOSApp.swift          # iOS app entry point
│   └── Podfile                   # CocoaPods dependencies
├── shared/                        # Shared multiplatform module
│   ├── src/
│   │   ├── androidMain/          # Android-specific implementations
│   │   ├── iosMain/              # iOS-specific implementations
│   │   ├── commonMain/           # Shared code
│   │   └── commonTest/           # Shared tests
│   └── build.gradle.kts
├── gradle/libs.versions.toml      # Version catalog
└── settings.gradle.kts
```

## Core Components

### 1. UI Layer (`shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/`)

#### Screens

- **LightsaberScreen**: Main lightsaber interface with blade animation
- **SettingsScreen**: Color customization and app settings

#### Key UI Components

- **LightsaberUi.kt**: Main lightsaber interface with animated blade
- **SettingsUi.kt**: Settings interface with color picker
- **Theme.kt**: App theming with Star Wars-inspired colors

#### Presenters (Circuit Architecture)

- **LightsaberPresenter**: Manages lightsaber state, sound effects, and motion detection
- **SettingsPresenter**: Handles settings state and persistence

### 2. Data Layer (`shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/data/`)

#### Models

```kotlin
data class LightsaberSettings(
    val bladeColor: Color
)
```

#### Repository

- **SettingsRepository**: Manages persistent user settings using DataStore
- **RealSettingsRepository**: Implementation with reactive StateFlow

### 3. Audio System (`shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/`)

#### Platform Abstractions

- **SoundPlayer**: Common interface for audio playback
- **SoundResource**: Represents audio files with metadata

#### Platform Implementations

- **AndroidSoundPlayer**: Uses Android MediaPlayer for audio
- **IosSoundPlayer**: Uses iOS AVAudioPlayer for audio

#### Sound Effects Included

- `lightsaber_activating.m4a` - Blade ignition sound
- `lightsaber_deactivating.m4a` - Blade shutdown sound
- `lightsaber_idle.m4a` - Continuous humming while active
- `lightsaber_hum1/2/3.m4a` - Swing movement sounds
- `lightsaber_clash1/2/3.m4a` - Combat clash sounds

### 4. Motion Detection (`shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/`)

#### Common Interface

- **SwingDetector**: Detects device motion for swing interactions
- **SwingEvent**: Represents motion events

#### Platform Implementations

- **AndroidSwingDetector**: Uses Android SensorManager and accelerometer
- **IosSwingDetector**: Uses iOS CoreMotion framework

### 5. Dependency Injection (`shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/di/`)

#### kotlin-inject with Anvil Architecture

The project uses **kotlin-inject** as the core DI framework with **anvil-kotlin-inject** for
advanced component composition and code generation.

##### Common Components

- **ApplicationComponent**: Provides Circuit framework setup
- **DatastoreComponent**: Configures persistent storage with DataStore

##### Platform-Specific Components

**Android** (`shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/`):

- **AndroidApplicationComponent**: Main Android DI component using `@MergeComponent`
- **AndroidSoundComponent**: Audio system bindings for Android
- **AndroidMotionComponent**: Motion detection bindings for Android

**iOS** (`shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/`):

- **IosApplicationComponent**: Main iOS DI component using `@MergeComponent`
- **IosSoundComponent**: Audio system bindings for iOS
- **IosMotionComponent**: Motion detection bindings for iOS

##### Key Annotations Used

- `@Inject`: Marks classes for dependency injection
- `@ContributesTo(AppScope::class)`: Contributes bindings to the application scope
- `@ContributesBinding(AppScope::class)`: Binds implementations to interfaces
- `@MergeComponent`: Merges all contributed components into a single component
- `@SingleIn(AppScope::class)`: Ensures singleton instances within app scope
- `@Assisted`: For factory-style injection (used with Circuit)

## State Management

### Circuit Architecture Pattern

The app uses Circuit's unidirectional data flow:

```
Screen → Presenter → State → UI → Events → Presenter
```

#### Circuit Integration with kotlin-inject

Circuit code generation is configured to work with kotlin-inject:

```kotlin
ksp {
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg("kotlin-inject-anvil-contributing-annotations", "com.slack.circuit.codegen.annotations.CircuitInject")
}
```

#### Lightsaber State Flow

```kotlin
sealed interface LightsaberEvent : CircuitUiEvent {
    data object LightsaberActivating
    data object LightsaberActivated
    data object LightsaberDeactivating
    data object LightsaberDeactivated
    data object SettingsSelected
}

enum class BladeState {
    Initializing, Deactivated, Activating, Activated, Deactivating
}

data class LightsaberState(
    val bladeState: BladeState,
    val bladeColor: Color,
    val onEvent: (LightsaberEvent) -> Unit
) : CircuitUiState
```

### Data Persistence

- **DataStore Preferences**: Stores user settings (blade color)
- **Reactive Updates**: StateFlow provides real-time UI updates
- **Platform-specific paths**: Different storage locations per platform

## Platform-Specific Features

### Android (`shared/src/androidMain/`)

- **MainActivity**: Entry point using ComponentActivity
- **AndroidSoundPlayer**: MediaPlayer-based audio implementation
- **AndroidSwingDetector**: SensorManager-based motion detection
- **Splash Screen**: Core splash screen support

### iOS (`shared/src/iosMain/`)

- **ContentView.swift**: SwiftUI wrapper for Compose UI
- **IosSoundPlayer**: AVAudioPlayer-based audio implementation
- **IosSwingDetector**: CoreMotion-based accelerometer access
- **Exception Handling**: Unhandled exception logging

## Resources

### Audio Assets (`shared/src/commonMain/composeResources/files/raw/`)

- 9 M4A audio files for various lightsaber sound effects
- Organized for cross-platform resource access

### Drawable Assets (`shared/src/commonMain/composeResources/drawable/`)

- **lightsaber_handle.xml**: Vector drawable for lightsaber hilt
- Platform-specific app icons and launcher resources

### String Resources (`shared/src/commonMain/composeResources/values/`)

- Localized strings for UI text elements

## Testing

### Test Structure (`shared/src/commonTest/`)

- **LightsaberUiTest**: UI interaction tests using Compose testing
- **LightsaberRobot**: Test robot pattern for UI interactions
- **LightsaberColor**: Test fixtures for color validation
- **Burst Testing**: Property-based testing support

### Testing Tools

- Kotlin Test framework
- Compose UI Test utilities
- Circuit testing support
- Robot pattern for clean test interactions

## Build Configuration

### Gradle Setup

- **Kotlin 2.2.10** with Compose compiler plugin
- **KSP 2.2.10-2.0.2** for Circuit and anvil-kotlin-inject code generation
- **kotlin-inject 0.7.2** for dependency injection runtime
- **anvil-kotlin-inject 0.1.6** for component composition
- **CocoaPods** integration for iOS dependencies

### kotlin-inject Configuration

The build is configured to use kotlin-inject with anvil for enhanced DI capabilities:

```kotlin
dependencies {
    implementation(libs.kotlin.inject.runtime.kmp)
    implementation(libs.anvil.kotlin.inject.runtime)
    implementation(libs.anvil.kotlin.inject.runtime.optional)
    
    add("kspCommonMainMetadata", libs.anvil.kotlin.inject.compiler)
    add("kspAndroid", libs.anvil.kotlin.inject.compiler)
    add("kspIosX64", libs.anvil.kotlin.inject.compiler)
    add("kspIosArm64", libs.anvil.kotlin.inject.compiler)
    add("kspIosSimulatorArm64", libs.anvil.kotlin.inject.compiler)
}
```

### Version Catalog (`gradle/libs.versions.toml`)

Centralized dependency management for:

- Compose Multiplatform versions
- Circuit framework versions
- kotlin-inject and anvil-kotlin-inject versions
- Android/iOS specific dependencies
- Testing framework versions

## Development Workflow

### Key Commands

```bash
# Android build and run
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:installDebug

# iOS build (requires Xcode)
./gradlew :shared:podInstall
# Then open iosApp/iosApp.xcworkspace in Xcode

# Run tests
./gradlew :shared:testDebugUnitTest

# Clean build
./gradlew clean
```

### Code Generation

- **Circuit**: Auto-generates presenter and UI factories via KSP
- **kotlin-inject + Anvil**: Generates dependency injection components and bindings
- **KMP Resources**: Processes multiplatform resources

## Dependency Injection Benefits

### Why kotlin-inject with Anvil?

**kotlin-inject** provides:

- ✅ Compile-time safety with zero runtime overhead
- ✅ Excellent Kotlin Multiplatform support
- ✅ Clean, annotation-based API
- ✅ Full type safety with assisted injection support

**anvil-kotlin-inject** adds:

- ✅ Component merging and composition (`@MergeComponent`)
- ✅ Automatic binding contribution (`@ContributesTo`, `@ContributesBinding`)
- ✅ Scoped dependency management (`@SingleIn`)
- ✅ Enhanced modularity and code organization

### DI Architecture Highlights

1. **Modular Design**: Each platform has its own components that contribute to the merged
   application component
2. **Scope Management**: `AppScope` ensures proper singleton behavior across the application
3. **Platform Abstraction**: Common interfaces with platform-specific implementations
4. **Circuit Integration**: Seamless integration with Circuit's factory-based presenter system
5. **Type Safety**: Full compile-time verification of dependency graphs

---

*This documentation was generated to help AI agents understand the Compose Lightsaber project
structure, architecture, and implementation details.*