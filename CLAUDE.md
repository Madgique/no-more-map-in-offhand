# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Minecraft mod called "No More Map in Offhand" that displays maps from accessory slots without needing to hold them. The mod is built using the Architectury framework to support multiple mod loaders, currently targeting Forge for Minecraft 1.20.1.

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
  - Uses **Curios API** for accessory slots
  - Includes `mods.toml` for Forge mod metadata

- **`fabric/`** - Fabric-specific implementation
  - Platform-specific mod initialization and event handling
  - Contains `MapCuriosSlotModFabric` which bootstraps the common code  
  - Uses **Accessories API** for accessory slots
  - Includes `fabric.mod.json` for Fabric mod metadata

### Key Components
- **Curios Integration**: Integration with Curios API to provide map slots
- **Map Slot Definition**: Custom curios slot type specifically for maps
- **Slot Rendering**: Custom rendering for the map curios slot

### Dependencies
- Architectury API for cross-platform compatibility
- **Forge**: Curios API integration (required dependency)
- **Fabric**: Accessories API integration (required dependency)
- Multiple mod loaders supported (Forge and Fabric)

## Development Notes

### Adding New Features
- Add cross-platform code to `common/src/main/java/com/madgique/nomoremapinoffhand/`
- **Forge** platform-specific implementations go in `forge/src/main/java/com/madgique/nomoremapinoffhand/forge/`
- **Fabric** platform-specific implementations go in `fabric/src/main/java/com/madgique/nomoremapinoffhand/fabric/`
- **CRITICAL**: Never delete Fabric or Forge modules - both are required for multi-platform support
- **CRITICAL**: Forge uses Curios API, Fabric uses Accessories API - both are necessary and not duplicates

### Testing
- Use `./gradlew :forge:runClient` to launch a development instance
- Test configuration changes by modifying the generated config file
- The `forge/run/` directory contains the development environment with logs, saves, and config
- **IMPORTANT**: Always use `./gradlew :forge:runClient` for testing, never use build commands during development

### Resource Management
- Language files: `common/src/main/resources/assets/no_more_map_in_offhand/lang/en_us.json`
- Forge metadata: `forge/src/resources/META-INF/mods.toml`
- Both contain mod display information and translations