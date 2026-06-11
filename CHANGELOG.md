# Changelog

All notable changes to this project will be documented here.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

## [0.1.0] - 2026-06-11

### Added
- `JavaSoundBackend` — implements `IAudioBackend` using Java Sound API with OGG Vorbis support
- `AMEAudio` — convenience wrapper that wires `AMEEngine`, `ConfigLoader`,
  `SoundpackLoader` and `JavaSoundBackend` together with a built-in tick loop
- OGG playback via `tritonus-share`, `vorbisspi` and `mp3spi`