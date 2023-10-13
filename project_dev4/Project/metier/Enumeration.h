#ifndef ENUMERATION_H
#define ENUMERATION_H

/*!
 * \brief The Type enum indicates a block type(Subject, Behavior, Connector, Block)
 */
enum class Type{BEHAVIOR,SUBJECT,CONNECTOR,BLOCK};

/*!
 * \brief The CheckDirection enum represents the direction in which to check for.
 * Used to find rule-chains in the game board.
 */
enum class CheckDirection{VERTICAL,HORIZONTAL};

/*!
 * \brief The GameState enum represents the possible states the game can be into.
 */
enum class GameState{PLAYING,WON,LOST,PAUSED};

#endif // ENUMERATION_H
