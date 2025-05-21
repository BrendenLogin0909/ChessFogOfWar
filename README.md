# Chess Fog of War

A chess variant for Android that implements fog of war mechanics, where pieces can only see squares they could potentially move to.

## Features

* Classic chess rules with special moves (castling, en passant, pawn promotion)
* Fog of war mechanics where pieces can only see squares they could potentially move to
* Board flipping between turns for better player perspective
* Next player screen between turns
* Special visibility rules for different pieces:
  * Pawns: See forward and diagonal attack squares
  * Knights: See L-shaped moves and adjacent squares
  * Kings: See all directions and knight-like moves
  * Other pieces: See along their movement paths

## Setup

1. Clone the repository
2. Open in Android Studio
3. Build and run on your device or emulator

## Development

Built with:
* Kotlin
* Jetpack Compose
* Android Architecture Components
* Material Design 3 