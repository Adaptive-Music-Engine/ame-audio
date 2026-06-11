# ame-audio

Audio backend implementation for [ame-core](https://github.com/Adaptive-Music-Engine/ame-core).
Provides `.ogg` playback via the Java Sound API — no additional setup required.

## What this is

`ame-audio` implements `IAudioBackend` from `ame-core` using Java's built-in audio system
extended with OGG Vorbis support. It also provides `AMEAudio` — a simple wrapper that
wires everything together including the tick loop.

## Usage

```java
AMEAudio audio = new AMEAudio("config/contexts.json", "sounds");

audio.setContext("combat");
audio.setBiome("tropical");
audio.setSoundpack("fantasy");
audio.triggerStinger("stinger_victory");
audio.fadeOut(4);

audio.shutdown();
```

## Add as a dependency

```groovy
repositories {
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.github.com/Adaptive-Music-Engine/ame-audio")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation 'com.ame:ame-audio:0.1.0'
}
```

## Requirements

- Java 21+
- [ame-core](https://github.com/Adaptive-Music-Engine/ame-core) 0.1.0+