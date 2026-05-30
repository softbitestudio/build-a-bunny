# 🐰 Build a Bunny

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-4285F4?logo=jetpackcompose&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-26%20(Android%208.0)-orange)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)
![Status](https://img.shields.io/badge/status-In%20Development-FF69B4)

A character creator app for Android where you design your own anthropomorphic bunny — mix and match fur, ears, eyes, accessories, and more to build a unique character, then save your collection to a personal gallery.

---

## Screenshots

> _Coming soon — assets in progress_

---

## Features

- **9 customization categories** — body shape, fur color, ear style, eye style & color, nose, mouth, accessory, and background
- **Real-time preview** — every change is instantly reflected in a live canvas rendering of your bunny
- **Gallery** — save as many characters as you like and jump back in to edit any of them
- **Long-press to delete** from the gallery with a confirmation prompt
- **Name your bunny** — each character gets its own name

---

## Customization Options

| Category | Options |
|---|---|
| **Body Shape** | Round · Slim · Chubby |
| **Fur Color** | White · Cream · Brown · Grey · Black · Spotted |
| **Ear Style** | Standing · Floppy · Lop |
| **Eye Style** | Round · Sleepy · Wide · Sparkle |
| **Eye Color** | Brown · Blue · Green · Pink · Black |
| **Nose** | Pink · Brown · Black |
| **Mouth** | Smile · Neutral · Silly · Surprised |
| **Accessory** | None · Bow Tie · Top Hat · Flower · Glasses · Scarf |
| **Background** | White · Pink · Meadow · Sky · Stars · Rainbow |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Character rendering | Compose `Canvas` (procedural drawing) |
| State management | `ViewModel` + `StateFlow` |
| Navigation | Navigation Compose |
| Serialization | kotlinx.serialization |
| Build system | Gradle 8.7 with version catalog |
| Min SDK | 26 (Android 8.0) |

---

## Project Structure

```
app/src/main/kotlin/com/softbite/buildabunny/
│
├── data/
│   ├── model/
│   │   ├── CharacterConfig.kt       # Immutable character state + withOption()
│   │   ├── CustomizationCategory.kt # Enum of all 9 customizable aspects
│   │   └── CustomizationOption.kt   # Option definitions + central registry
│   └── repository/
│       └── CharacterRepository.kt   # In-memory StateFlow-backed store
│
├── navigation/
│   ├── Screen.kt                    # Sealed route definitions
│   └── AppNavGraph.kt               # NavHost wiring
│
└── ui/
    ├── creator/
    │   ├── CreatorUiState.kt
    │   ├── CreatorViewModel.kt
    │   └── CreatorScreen.kt         # Preview + name field + tabs + option grid
    ├── gallery/
    │   ├── GalleryViewModel.kt
    │   └── GalleryScreen.kt         # Saved character grid, long-press delete
    ├── components/
    │   ├── BunnyCanvas.kt           # Full layered Canvas renderer
    │   ├── CategoryTabRow.kt        # Scrollable category tabs
    │   └── OptionGrid.kt            # Adaptive grid of selectable options
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

---

## Architecture

The app follows **MVVM** with a unidirectional data flow:

```
User interaction
      │
      ▼
  ViewModel  ──  selectOption(category, id)
      │               │
      │        CharacterConfig.withOption()   ← pure, immutable update
      │
      ▼
  StateFlow<CreatorUiState>
      │
      ▼
  CreatorScreen  →  BunnyCanvas(config)
```

`CharacterConfig` is a plain `@Serializable` data class. Every customization produces a new copy via `withOption(category, id)` — no mutation, easy to diff and test.

Adding a new customization attribute requires:
1. A new `CustomizationCategory` enum entry
2. A new `List<CustomizationOption>` in `CustomizationOptions`
3. A `when` branch in `CharacterConfig.withOption()` and `selectedOptionFor()`
4. Drawing logic in `BunnyCanvas`

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK 34

### Clone & run

```bash
git clone https://github.com/softbitestudio/build-a-bunny.git
cd build-a-bunny
./gradlew assembleDebug
```

Or open the project in Android Studio and run on a device or emulator (API 26+).

---

## Roadmap

- [ ] Image-based layered rendering (replacing procedural Canvas)
- [ ] Persistent storage with Room
- [ ] Export / share your bunny as an image
- [ ] More accessories and background scenes
- [ ] Color picker for custom fur colors

---

## License

[Apache 2.0](LICENSE)
