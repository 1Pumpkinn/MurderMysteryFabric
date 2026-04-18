# Implementation Plan: Game Death Mechanics

## Overview

This implementation plan breaks down the four death mechanics (spectator mode, game timer with boss bar, inventory management, and chat restrictions) into discrete coding tasks. Each task builds incrementally on previous work, with early validation through code integration.

## Tasks

- [x] 1. Create GameTimer class with boss bar management
  - Create `src/main/java/net/saturn/murdermysteryfabric/game/GameTimer.java`
  - Implement constructor that accepts MinecraftServer and duration in seconds
  - Create boss bar using `server.getBossBarManager()` with identifier "murdermysteryfabric:game_timer"
  - Add all online players to boss bar in constructor
  - Implement `formatTime(int seconds)` helper method to format as M:SS
  - Implement `updateBossBar()` helper to update text and percent
  - Implement `tick()` method that decrements counter, updates boss bar, and returns true when time expires
  - Implement color logic: yellow for ≥30s, red for <30s
  - Implement `addPlayer(ServerPlayerEntity)` method to add players to boss bar
  - Implement `remove()` method to clean up boss bar
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 2.10, 2.11_

- [ ]* 1.1 Write unit tests for GameTimer
  - Test time formatting (65 → "1:05", 300 → "5:00")
  - Test boss bar percent calculation
  - Test color transitions at 30-second threshold
  - Test tick countdown and expiration detection
  - _Requirements: 2.4, 2.5, 2.6, 2.7, 2.8_

- [x] 2. Integrate GameTimer into GameManager
  - Add `private GameTimer gameTimer` field to GameManager
  - Modify `startGame()` to create GameTimer with 300-second duration
  - Register ServerTickEvents.END_SERVER_TICK listener in `startGame()`
  - Implement tick handler that calls `gameTimer.tick()` every 20 ticks
  - Handle timer expiration by calling `endGame()` with "Time's up! The Murderer wins!"
  - Modify `endGame()` to call `gameTimer.remove()` before other cleanup logic
  - _Requirements: 2.12, 2.13, 2.14, 2.15_

- [ ]* 2.1 Write integration tests for timer lifecycle
  - Test GameTimer creation during game start
  - Test timer tick updates boss bar correctly
  - Test timer expiration triggers endGame
  - Test boss bar cleanup on game end
  - _Requirements: 2.12, 2.13, 2.14, 2.15_

- [x] 3. Checkpoint - Ensure timer works end-to-end
  - Ensure all tests pass, ask the user if questions arise.

- [x] 4. Implement spectator mode on death
  - Modify `PlayerDeathMixin.onDeath()` to check if game is running
  - Add inventory clear: `player.getInventory().clear()`
  - Add game mode change: `player.changeGameMode(GameMode.SPECTATOR)`
  - Ensure these operations happen before calling `GameManager.onPlayerDeath()`
  - Modify `GameManager.endGame()` to restore all players to GameMode.SURVIVAL
  - Iterate through all online players and call `changeGameMode(GameMode.SURVIVAL)`
  - _Requirements: 1.1, 1.2, 1.3, 1.4_

- [ ]* 4.1 Write integration tests for spectator mode transitions
  - Test player enters spectator mode on death during game
  - Test all players restored to survival on game end
  - Test player death outside game doesn't change mode
  - _Requirements: 1.1, 1.2, 1.4_

- [x] 5. Create DropInventoryMixin to prevent role item drops
  - Create `src/main/java/net/saturn/murdermysteryfabric/mixin/DropInventoryMixin.java`
  - Add @Mixin annotation targeting ServerPlayerEntity
  - Inject into `dropInventory` method at HEAD with cancellable=true
  - Check if game is running using `GameManager.getInstance().isGameRunning()`
  - Check if player inventory contains ModItems.KNIFE or ModItems.GUN
  - Call `ci.cancel()` if game is running and player has role items
  - Add DropInventoryMixin to `src/main/resources/murdermysteryfabric.mixins.json`
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8_

- [ ]* 5.1 Write unit tests for role item detection
  - Test hasRoleItem logic with KNIFE returns true
  - Test hasRoleItem logic with GUN returns true
  - Test hasRoleItem logic with regular items returns false
  - Test hasRoleItem logic with empty inventory returns false
  - _Requirements: 3.6, 3.7_

- [ ]* 5.2 Write integration tests for drop prevention
  - Test dropInventory is cancelled when player has KNIFE during game
  - Test dropInventory is cancelled when player has GUN during game
  - Test dropInventory proceeds normally outside of game
  - Test no role items appear as world entities after death
  - _Requirements: 3.4, 3.8_

- [x] 6. Checkpoint - Ensure inventory management works correctly
  - Ensure all tests pass, ask the user if questions arise.

- [x] 7. Implement chat restriction for dead players
  - Create `src/main/java/net/saturn/murdermysteryfabric/event/ModEvents.java`
  - Implement `register()` method
  - Register ServerMessageEvents.ALLOW_CHAT_MESSAGE listener
  - Check if game is running using `GameManager.getInstance().isGameRunning()`
  - Check if sender is in GameMode.SPECTATOR
  - If dead player (spectator during game), send actionbar message "Dead players cannot chat." in red
  - Return false to block message if dead player, true otherwise
  - Modify `Murdermysteryfabric.onInitialize()` to call `ModEvents.register()`
  - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7_

- [ ]* 7.1 Write integration tests for chat restrictions
  - Test message blocked for spectator during game
  - Test message allowed for survival player during game
  - Test message allowed for spectator outside game
  - Test actionbar feedback sent to blocked players
  - _Requirements: 4.2, 4.3, 4.4, 4.5, 4.6_

- [x] 8. Add error handling and edge cases
  - Add null checks in GameTimer constructor for server parameter
  - Add null check in GameTimer.tick() to prevent NPE after removal
  - Add try-catch in PlayerDeathMixin for changeGameMode failures
  - Add null checks in DropInventoryMixin for item type comparisons
  - Add try-catch in chat event handler to prevent chat system breakage
  - Add synchronization or state checks to prevent race conditions in timer expiration
  - _Requirements: All (error handling for robustness)_

- [ ]* 8.1 Write tests for error scenarios
  - Test GameTimer with null server throws exception
  - Test timer tick after removal returns false safely
  - Test spectator mode change failure is logged but game continues
  - Test chat handler exception allows message (fail-safe)
  - _Requirements: All (error handling validation)_

- [x] 9. Final integration and wiring
  - Verify all components work together in complete game flow
  - Test game start → player death → spectator mode → chat blocked → timer expires → game end
  - Verify boss bar appears for all players including mid-game joiners
  - Verify all players restored to survival with cleared inventories on game end
  - Verify no role items drop during any death scenario
  - _Requirements: All_

- [ ]* 9.1 Write end-to-end integration tests
  - Test complete game flow from start to timer expiration
  - Test complete game flow from start to murderer elimination
  - Test complete game flow from start to all innocents eliminated
  - Test player join mid-game receives boss bar
  - _Requirements: All_

- [x] 10. Final checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation at logical breakpoints
- The implementation follows a bottom-up approach: core classes first, then integration, then event handling
- Error handling is consolidated in task 8 to avoid repetition across earlier tasks
- All code uses Java and integrates with existing Minecraft Fabric APIs
