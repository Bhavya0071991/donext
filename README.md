# DoNext - Todo & Task Manager

**DoNext** is a modern, intuitive, and feature-rich Android task management application built using **Jetpack Compose**, **Kotlin**, **Hilt**, **Room Database**, and **DataStore Preferences**.

---

## 📱 Features

- 📝 **Task Management**: Create, edit, track, and complete tasks with due dates, notes, and priority badges (High, Medium, Low).
- 📊 **Daily Progress Tracker**: Visual progress bar tracking completed vs. total tasks for today.
- 🎯 **Smart Filters**: Quick filter chips to view tasks (e.g., Today, Upcoming, Completed, All).
- 📅 **Calendar Overview**: Interactive calendar view to inspect and jump to tasks scheduled for specific dates.
- 🏷️ **Custom Lists & Categories**: Organize tasks into color-coded lists and categories.
- 🧩 **Subtasks Support**: Break down complex tasks into manageable subtasks.
- 🎨 **Dynamic Theme Support**: Modern Material 3 UI supporting Light Mode, Dark Mode, and System Default preference.
- 🚀 **Smooth Onboarding**: Clean splash and onboarding experience for new users.

---

## 🛠️ Architecture & Tech Stack

This app follows **Android Clean Architecture** guidelines and **MVVM (Model-View-ViewModel)** design pattern with Unidirectional Data Flow (UDF).

* **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 components
* **Language**: [Kotlin](https://kotlinlang.org/)
* **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) with KSP
* **Local Database**: [Room](https://developer.android.com/training/data-storage/room) (SQLite ORM) for offline persistence
* **Preferences Storage**: [Jetpack DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
* **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
* **Asynchronous Flow**: Kotlin Coroutines & `StateFlow` / `SharedFlow`

---

## 📂 Project Structure

```
com.app.todo
├── core/                  # Core utilities & base components
├── data/                  # Data layer (Entities, DAOs, Room DB, Preferences, Repositories)
│   ├── local/             # Room Database & DataStore sources
│   ├── mapper/            # Domain-Data entity mappers
│   └── repositoryimpl/    # Repository implementations
├── di/                    # Dependency Injection modules (Hilt)
├── domain/                # Domain layer (Models, Repository Interfaces, Use Cases)
├── presentation/          # Presentation layer (Compose Screens, ViewModels, UI States/Events)
│   ├── calendar/          # Calendar screen & ViewModel
│   ├── lists/             # Category lists screen & ViewModel
│   ├── navigation/        # BottomBar, NavGraph & Route definitions
│   ├── onboarding/       # Onboarding screen
│   ├── settings/          # App settings & Theme toggling
│   ├── taskdetail/        # Task detail & Subtasks view
│   └── tasks/             # Main tasks screen & New task bottom sheet
└── ui/theme/              # Material 3 colors, typography & theme configuration
```

---

## 🚀 Getting Started

### Prerequisites

* **Android Studio**: Ladybug / Hedgehog or newer (2024+)
* **JDK**: Version 11 or higher
* **Min SDK**: 33 (Android 13)
* **Target SDK**: 37

### Building & Running

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/donext.git
   ```
2. Open the project in **Android Studio**.
3. Let Gradle synchronize the dependencies.
4. Select an emulator or connected physical Android device.
5. Click **Run (`Shift + F10`)**.

---

## 📄 License

```
Copyright 2026 DoNext
```
