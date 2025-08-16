# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Minecraft mod called "Map Curios Slot" that adds a curios slot specifically for maps. The mod is built using the Architectury framework to support multiple mod loaders, currently targeting Forge for Minecraft 1.20.1.

## Build System & Commands

This project uses Gradle with the Architectury plugin for multi-platform mod development.

### Essential Commands
- `./gradlew build` - Build all modules (common + forge)
- `./gradlew :forge:build` - Build only the Forge version
- `./gradlew :common:build` - Build only the common module
- `./gradlew :forge:runClient` - Run Minecraft client with the mod for testing
- `./gradlew clean` - Clean build artifacts
- `./gradlew publishToMavenLocal` - Publish to local Maven repository

### Development Setup
- Java 17 required (configured via toolchain)
- Uses official Mojang mappings
- Mod version: 1.0 (defined in gradle.properties)
- Target Minecraft: 1.20.1
- Forge version: 47.3.0
- Architectury version: 9.2.14

## Architecture

### Multi-Platform Structure
The project follows Architectury's multi-platform pattern:

- **`common/`** - Platform-agnostic code shared between mod loaders
  - Contains core mod logic, configuration, and client-side functionality
  - Main classes: `MapCuriosSlotMod`, `MapCuriosSlotClient`
  
- **`forge/`** - Forge-specific implementation
  - Platform-specific mod initialization and event handling
  - Contains `MapCuriosSlotModForge` which bootstraps the common code
  - Includes `mods.toml` for Forge mod metadata

### Key Components
- **Curios Integration**: Integration with Curios API to provide map slots
- **Map Slot Definition**: Custom curios slot type specifically for maps
- **Slot Rendering**: Custom rendering for the map curios slot

### Dependencies
- Architectury API for cross-platform compatibility
- Curios API integration (required dependency)
- Minecraft Forge as the target loader

## Development Notes

### Adding New Features
- Add cross-platform code to `common/src/main/java/com/madgique/mapcuriosslot/`
- Platform-specific implementations go in `forge/src/main/java/com/madgique/mapcuriosslot/forge/`
- New curios slot types should be added to the curios configuration

### Testing
- Use `./gradlew :forge:runClient` to launch a development instance
- Test configuration changes by modifying the generated config file
- The `forge/run/` directory contains the development environment with logs, saves, and config
- **IMPORTANT**: Always use `./gradlew :forge:runClient` for testing, never use build commands during development

### Resource Management
- Language files: `common/src/main/resources/assets/map_curios_slot/lang/en_us.json`
- Forge metadata: `forge/src/resources/META-INF/mods.toml`
- Both contain mod display information and translations