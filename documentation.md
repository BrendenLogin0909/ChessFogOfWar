# Fog of War Chess - Android App

## Project Documentation

This document provides an overview of the Fog of War Chess Android application, its features, and implementation details.

## Game Concept

Fog of War Chess is a variant of traditional chess with the following key differences:

1. **Limited Visibility**: Players can only see opponent pieces that are in the potential movement paths of their own pieces.
2. **Blackout Screen**: Between turns, a blackout screen appears to allow passing the device to the other player without revealing piece positions.
3. **Strategic Exploration**: Players must strategically position their pieces to maximize visibility of the board.

## Features

- Standard chess movement rules
- Fog of War visibility system
- Animated blackout screen between turns
- Game state management (check, checkmate, stalemate detection)
- New game and exit options
- About screen with game rules

## Technical Implementation

### Core Components

1. **MainActivity**: Entry point with menu options
2. **GameActivity**: Manages the game flow and turn transitions
3. **ChessBoardView**: Custom view for rendering the chess board and handling interactions
4. **ChessRules**: Contains chess movement validation and visibility logic
5. **GameStateManager**: Handles game state including checkmate and stalemate detection

### Fog of War Implementation

The visibility system works by:
1. Calculating all possible movement paths for each of the current player's pieces
2. Making opponent pieces visible only if they are in these paths
3. Updating visibility after each move

### Blackout Screen

The blackout screen:
1. Appears when the "Next Turn" button is pressed
2. Displays instructions to pass the device to the other player
3. Fades in and out with smooth animations
4. Prevents the next player from seeing the previous player's view

## User Guide

1. Start the app and tap "Start Game"
2. White player goes first
3. Tap a piece to select it, then tap a destination to move
4. After your move, tap "Next Turn"
5. Pass the device to your opponent when prompted
6. Tap the screen to continue to the next player's turn
7. Continue until checkmate or stalemate occurs

## Future Enhancements

Potential future improvements:
- Online multiplayer
- AI opponent
- Customizable visibility rules
- Game history and replay
- Different chess variants with Fog of War mechanics
