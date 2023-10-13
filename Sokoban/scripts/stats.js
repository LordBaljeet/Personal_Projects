"use strict";

/**
 * Classe representant l'etat du jeu a un moment donne.
 */
class State {

    /**
     * @param {Position} playerPos la position du joueur
     * @param {Position} boxPos la position de l'eventuelle box.
     */
    constructor(playerPos, boxPos = {x: 0, y: 0}) {
        /**
         * @private
         */
        this.__playerPosition = playerPos;
        /**
         * @private
         */
        this.__boxPosition = boxPos;
    }

    get playerPosition() {
        return Object.assign({}, this.__playerPosition);
    }

    get boxPosition() {
        return Object.assign({}, this.__boxPosition);
    }
}
