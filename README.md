# edisen
![Java Build](https://github.com/bobbylight/Edisen/actions/workflows/gradle.yml/badge.svg)
![Java Build](https://github.com/bobbylight/Edisen/actions/workflows/codeql-analysis.yml/badge.svg)

The beginnings of a simple NES IDE.

## Compiling
`Edisen` requires Java 14 or later to compile and run.

```bash
./gradlew clean build --warning-mode all
```

## Building the Native Applications
`Edisen` packages are available for both Windows and Mac.

Windows:
```bash
./gradlew clean generateWindowsStarterExe

# Running the built application:
./build/install/edisen/edisen.exe
```

Mac:
```bash
./gradlew clean generateMacApp

# Running the built application:
open -a ./build/install/edisen/Edisen.app
```

## What's Done
This is still early days, and currently the scaffolding is being worked on.
It's not really usable just yet to build games, but is good to whet
appetites.

* The shell of a syntax highlighting code editor.
* A basic project configuration

## Progress
Check the [issues page](https://github.com/bobbylight/Edisen/issues)
for progress on features, bugs, etc.
