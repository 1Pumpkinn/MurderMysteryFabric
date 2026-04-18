# Requirements Document

## Introduction

This document specifies the requirements for implementing four game death mechanics in the Murder Mystery Fabric mod. These mechanics enhance the gameplay experience by managing player state after death, enforcing time limits, preventing item drops, and restricting dead player communication.

## Glossary

- **Game_Manager**: The singleton class that manages game state, role assignments, and win conditions
- **Player**: A ServerPlayerEntity participating in the Murder Mystery game
- **Role_Item**: Either the KNIFE (ModItems.KNIFE) or GUN (ModItems.GUN) item given to players based on their role
- **Game_Timer**: A class that manages the countdown timer displayed via boss bar
- **Boss_Bar**: A Minecraft UI element displayed at the top of the screen showing game timer progress
- **Spectator_Mode**: GameMode.SPECTATOR, a game mode where players can observe but not interact
- **Game_Running**: The state when GameManager.isGameRunning() returns true
- **Dead_Player**: A player whose game mode is GameMode.SPECTATOR during an active game

## Requirements

### Requirement 1: Spectator Mode on Death

**User Story:** As a player, I want to enter spectator mode when I die during a game, so that I can observe the remaining gameplay without interfering.

#### Acceptance Criteria

1. WHEN a Player dies during a Game_Running state, THE Game_Manager SHALL change the Player's game mode to Spectator_Mode using changeGameMode(GameMode.SPECTATOR)
2. WHEN the game ends via endGame(), THE Game_Manager SHALL restore all Players back to GameMode.SURVIVAL
3. THE Game_Manager SHALL execute the spectator mode change inside the onPlayerDeath() method
4. FOR ALL Players in Spectator_Mode at game end, restoring to GameMode.SURVIVAL SHALL succeed without errors

### Requirement 2: Game Timer with Boss Bar

**User Story:** As a player, I want to see a countdown timer during the game, so that I know how much time remains before the Murderer wins.

#### Acceptance Criteria

1. THE Game_Timer SHALL accept a MinecraftServer and duration in seconds (default 300) as constructor parameters
2. WHEN Game_Timer is constructed, THE Game_Timer SHALL create a Boss_Bar using server.getBossBarManager() with identifier "murdermysteryfabric:game_timer"
3. WHEN Game_Timer is constructed, THE Game_Timer SHALL add all online Players to the Boss_Bar
4. THE Game_Timer SHALL display remaining time formatted as M:SS in the Boss_Bar label
5. WHEN tick() is called, THE Game_Timer SHALL decrement the counter by one second and update the Boss_Bar percent and label
6. WHEN remaining time is under 30 seconds, THE Game_Timer SHALL change the Boss_Bar color to red
7. WHEN remaining time is 30 seconds or more, THE Game_Timer SHALL display the Boss_Bar in yellow
8. WHEN tick() is called and time reaches zero, THE Game_Timer SHALL return true
9. WHEN tick() is called and time has not reached zero, THE Game_Timer SHALL return false
10. THE Game_Timer SHALL provide an addPlayer(ServerPlayerEntity) method to add Players to the Boss_Bar
11. THE Game_Timer SHALL provide a remove() method to clean up the Boss_Bar
12. WHEN Game_Manager.startGame() is called, THE Game_Manager SHALL start the Game_Timer
13. THE Game_Manager SHALL call Game_Timer.tick() every 20 server ticks via ServerTickEvents.END_SERVER_TICK
14. WHEN Game_Timer.tick() returns true, THE Game_Manager SHALL call endGame() with message "Time's up! The Murderer wins!"
15. WHEN Game_Manager.endGame() is called, THE Game_Manager SHALL call Game_Timer.remove() before any other endGame logic

### Requirement 3: Keep Inventory and Clear Role Items on Death

**User Story:** As a game designer, I want role items to be removed silently when players die, so that items don't drop on the ground and break game balance.

#### Acceptance Criteria

1. WHEN a Player dies during a Game_Running state, THE PlayerDeathMixin SHALL call killed.getInventory().clear() before switching to Spectator_Mode
2. THE PlayerDeathMixin SHALL inject into ServerPlayerEntity.onDeath at TAIL injection point
3. THE mod SHALL provide a second mixin on ServerPlayerEntity.dropInventory
4. WHEN ServerPlayerEntity.dropInventory is called during a Game_Running state and the Player holds a Role_Item, THE dropInventory mixin SHALL cancel the method execution using ci.cancel()
5. WHEN ServerPlayerEntity.dropInventory is called during a Game_Running state and the Player holds a Role_Item, THE dropInventory mixin SHALL inject at HEAD injection point
6. WHEN a Player holds ModItems.KNIFE, THE dropInventory mixin SHALL identify it as a Role_Item
7. WHEN a Player holds ModItems.GUN, THE dropInventory mixin SHALL identify it as a Role_Item
8. FOR ALL Player deaths during Game_Running, no Role_Items SHALL appear as dropped items in the world

### Requirement 4: Chat Restriction for Dead Players

**User Story:** As a player, I want dead players to be unable to chat, so that they cannot reveal information about the Murderer's identity or location.

#### Acceptance Criteria

1. THE mod SHALL register a listener for ServerMessageEvents.ALLOW_CHAT_MESSAGE in the initialization phase
2. WHEN a Dead_Player attempts to send a chat message, THE listener SHALL cancel the message
3. WHEN a Dead_Player attempts to send a chat message, THE listener SHALL send an action bar message to the Dead_Player with text "Dead players cannot chat." formatted in red
4. WHEN a Dead_Player attempts to send a chat message, THE listener SHALL return false to suppress the message
5. WHEN a Player in GameMode.SURVIVAL or GameMode.CREATIVE attempts to send a chat message during Game_Running, THE listener SHALL allow the message
6. WHEN a Player attempts to send a chat message and Game_Running is false, THE listener SHALL allow the message regardless of game mode
7. THE listener registration SHALL occur in ModCommands.register() or a new ModEvents.register() method called from Murdermysteryfabric.onInitialize()
