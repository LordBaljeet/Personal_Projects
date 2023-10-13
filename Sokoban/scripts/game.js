"use strict";

// Variables globale.
let movesCount = 0;
let mapIndex = 0;

/**
 * @typedef {{x: number, y: number}} Position un object position.
 */

/**
 * tableau des moves faits par le joueur.
 *
 * @type {{playerPosition: Position, boxPosition: Position}[]}
 */
let states = [];

/*--------------------------Creation du HTML-------------------------*/

/**
 * Methode qui adapte le html pour afficher le niveau désiré sur la page.
 * @param {number} level le niveau a afficher.
 */
function buildLevel(level) {
    mapIndex = level;
    const mapLevel = levels[level].map;
    for (let line = 0; line < mapLevel.length; line++) {
        $("#world").append("<div class = 'line'>");
        // tableau contenant toutes les lignes du niveau.
        const levelLine = $(".line");
        for (const character of mapLevel[line]) {
            switch (character) {
            case "🧍":
                $(levelLine[line]).append("<div class = 'player square'>");
                break;
            case "x":
                $(levelLine[line]).append("<div class = 'destination square'>");
                break;
            case "#":
                $(levelLine[line]).append("<div class = 'box square'>");
                break;
            case " ":
                $(levelLine[line]).append("<div class = 'floor square'>");
                break;
            case "@":
                $(levelLine[line]).append("<div class = 'box destination square onTarget'>");
                break;
            default:
                $(levelLine[line]).append("<div class = 'wall square'>");
                break;
            }
        }
    }
    //ajout du style + le numero du niveau sur la page:
    $("#world").css("border", "16px double white");
    const levelSpan = $(".levelNav span")[1];
    $(levelSpan).text(mapIndex);
    setBest();
}

/**
 * initialise le niveau.
 */
function initLevel() {
    movesCount = 0;
    states = [];
    $("#world").empty();
    $(".screen").css("display", "none");
    $(".gameInfo span:first").text(movesCount);
    buildLevel(mapIndex);
    move();
}

/**
 * reset le level.
 */
function resetLevel() {
    $(".resetLevel").on("click", function () {
        $(window).off("keydown");
        initLevel();
    });
}

/**
 * affiche la modale d'aide sur la page.
 */
function setHelpScreen() {
    const helpModal = $(".modalContainer")[0];
    $("#helpBtn").on("click", function () {
        $(helpModal).css("display", "block");
        //desactive le mouvement du joueur.
        $(window).off("keydown");
    });
    const closeBtn = $(".close")[0];
    $(closeBtn).on("click", function () {
        $(helpModal).css("display", "none");
        //reactive le mouvement du joueur.
        move();
    });
}

/**
 * active le systeme de navigation entre les niveaux.
 */
function levelNavToggle() {
    $(".levelNavToggle").on("click", function() {
        $(".levelNavToggle").toggleClass("playerMode devMode");
        if ($(".levelNavToggle").hasClass("playerMode")) {
            $(".levelNav :button").css("visibility", "hidden");
            $(".levelNavToggle").text("Enter Dev Mode");
        } else {
            $(".levelNav :button").css("visibility", "visible");
            $(".levelNavToggle").text("Enter Player Mode");
        }
    });
}
function levelNav() {
    $(".nextLevel").on("click", function () {
        if (mapIndex < 6) {
            mapIndex++;
            $(window).off("keydown");
            initLevel();
        }
    });
    $(".prevLevel").on("click", function () {
        if (mapIndex > 0) {
            mapIndex--;
            $(window).off("keydown");
            initLevel();
        }
    });
}

/**
 * ajoute a la page un ecran affichant la fin du niveau.
 */
function setGameOverScreen() {
    $(".gameOver").css("display", "block");
    $(".nextLevIndex").text(mapIndex);
}

/**
 * ajoute a la page un ecran felicitant le joueur pour avoir fini le jeu.
 */
function setApplauseScreen() {
    $(".applause").css("display", "block");
}

/**
 * affiche la section d'information ainsi qu'active les boutons du jeu.
 */
function setGameInfos() {
    $(".gameInfo").css("visibility", "visible");
    $(".soon").css("visibility", "hidden");
    resetLevel();
    setHelpScreen();
    levelNav();
    rewindMove();
}

/**
 * initialise le niveau 0 et affiche/active la section d'information.
 */
function setGame() {
    initLevel();
    setGameInfos();
    levelNavToggle();
}

function setBest() {
    const best = levels[mapIndex].best;
    if (best !== undefined) {
        $(".bestText").css("display", "block");
        $(".best").text(best);
    }
}
/*--------------------------Creation des fonctions du jeu-------------------------*/

/**
 * retourne la position actuelle du joueur sur le plateau.
 * @returns la position du joueur.
 */
function getPlayerPosition() {
    let x = 0;
    let y = 0;
    for (const square of $(".square")) {
        if ($(square).hasClass("player")) {
            x = $(square).index();
            y = $($(square).parent()).index();
        }
    }
    return {x, y};
}

/**
 * retourne la case de la page web qui se trouve a la position indique.
 * @param {Position} position la position de la case.
 * @returns l'element de la case.
 */
function getSquareAt(position) {
    return $($(".line")[position.y]).children()[position.x];
}

/**
 * deplace le joueur sur le plateau ainsi qu'une eventuelle box.
 */
function move() {
    $(window).on("keydown", function (event) {
        const playerPos = getPlayerPosition();
        let pos1 = playerPos;
        let pos2 = playerPos;
        let boxPos = {x: 0, y: 0};
        switch (event.key) {
        case "ArrowUp":
            event.preventDefault();
            pos1 = {x: playerPos.x, y: playerPos.y - 1};
            pos2 = {x: playerPos.x, y: playerPos.y - 2};
            boxPos = {x: playerPos.x, y: playerPos.y + 1};
            moveDirection(playerPos, pos1, pos2);
            $(".player").removeClass("left right up down");
            $(".player").addClass("up");
            break;
        case "ArrowDown":
            event.preventDefault();
            pos1 = {x: playerPos.x, y: playerPos.y + 1};
            pos2 = {x: playerPos.x, y: playerPos.y + 2};
            boxPos = {x: playerPos.x, y: playerPos.y - 1};
            moveDirection(playerPos, pos1, pos2);
            $(".player").removeClass("left right up down");
            $(".player").addClass("down");
            break;
        case "ArrowLeft":
            event.preventDefault();
            pos1 = {x: playerPos.x - 1, y: playerPos.y};
            pos2 = {x: playerPos.x - 2, y: playerPos.y};
            boxPos = {x: playerPos.x + 1, y: playerPos.y};
            moveDirection(playerPos, pos1, pos2);
            $(".player").removeClass("left right up down");
            $(".player").addClass("left");
            break;
        case "ArrowRight":
            event.preventDefault();
            pos1 = {x: playerPos.x + 1, y: playerPos.y};
            pos2 = {x: playerPos.x + 2, y: playerPos.y};
            boxPos = {x: playerPos.x - 1, y: playerPos.y};
            moveDirection(playerPos, pos1, pos2);
            $(".player").removeClass("left right up down");
            $(".player").addClass("right");
            break;
        }
        pullBox(boxPos, playerPos);
        if (allOnTarget()) {
            finishLevel();
        }
    });
}

/**
 * Permet de deplacer le joueur (et une box) vers une direction donne.
 * @param {Position} playerPos la position du joueur.
 * @param {Position} pos1 la position une case autour du joueur
 * @param {Position} pos2 la position 2 cases autour du joueur.
 */
function moveDirection(playerPos, pos1, pos2) {
    if (!$(getSquareAt(pos1)).hasClass("wall")) {
        if ($(getSquareAt(pos1)).hasClass("box")) {
            if (!$(getSquareAt(pos2)).hasClass("box") && !$(getSquareAt(pos2)).hasClass("wall")) {
                //enregistre l'etat du jeu.
                states[movesCount] = new State(playerPos, pos2);
                moveBox(pos1, pos2);
                movePlayer(playerPos, pos1);
                if ($(getSquareAt(pos2)).hasClass("destination")) {
                    $(getSquareAt(pos2)).addClass("onTarget");
                }
                incrMoves();
            }
        } else {
            //enregistre l'etat du jeu.
            states[movesCount] = new State(playerPos);
            movePlayer(playerPos, pos1);
            incrMoves();
        }
        //correction de la case de depart du joueur.
        if (!$(getSquareAt(playerPos)).hasClass("floor") && !$(getSquareAt(playerPos)).hasClass("destination")) {
            $(getSquareAt(playerPos)).addClass("floor");
        }
    }
}

/**
 * Permet de deplacer la box a la case suivante.
 *
 * @param {Position} oldPos l'ancienne position.
 * @param {Position} newPos la nouvelle position.
 */
function moveBox(oldPos, newPos) {
    $(getSquareAt(newPos)).addClass("box");
    $(getSquareAt(oldPos)).removeClass("box onTarget");
}
/**
 * Permet de deplacer le joueur uniquement vers la case donne.
 *
 * @param {Position} oldPos l'ancienne position.
 * @param {Position} newPos la nouvelle position.
 */
function movePlayer(oldPos, newPos) {
    $(getSquareAt(newPos)).addClass("player");
    $(getSquareAt(oldPos)).removeClass("player left right up down");
}

/**
 * fonction qui annule les mouvements faits par le joueur.
 */
function rewindMove() {
    $(".rewind").on("click", function() {
        if (movesCount > 0) {
            decrMoves();
            const currentPlayerPosition = getPlayerPosition();
            movePlayer(getPlayerPosition(), states[movesCount].playerPosition);
            if (states[movesCount].boxPosition.x > 0 && states[movesCount].boxPosition.y > 0) {
                moveBox(states[movesCount].boxPosition, currentPlayerPosition);
            }
            states.pop();
        }
    });
}
/**
 * fonction qui decremente le compteur de mouvement.
 */
function decrMoves() {
    $(".gameInfo span:first").text(--movesCount);
}

/**
 * Increment le movesCount et l'affiche sur la page.
 */
function incrMoves() {
    $(".gameInfo span:first").text(++movesCount);
}

/**
 * indique si toutes les destinations sont occupees par des boxes.
 * @returns true si toutes les boxes sont sur les cases destinations, false sinon.
 */
function allOnTarget() {
    const targets = $(".destination");
    for (const target of targets) {
        if (!$(target).hasClass("box")) {
            return false;
        }
    }
    return true;
}

/**
 * permet d'afficher au joueur la fin du niveau,
 * lui demande de passer au suivant, si ce n'etais pas le dernier,
 * et initialise ce dernier.
 */
function finishLevel() {
    $(window).off("keydown");
    if (mapIndex === levels.length - 1) {
        setApplauseScreen();
    } else {
        mapIndex++;
        setGameOverScreen();
        const boxAnimation = setInterval(function () {
            $(".box").toggleClass("onTarget");
        }, 100);
        $(window).on("keyup", function (event) {
            if (event.key === " ") {
                initLevel();
                $(window).off("keyup");
                clearInterval(boxAnimation);
            }
        });
    }
}

/**
 * @param {Position} boxPos
 * @param {Position} playerPos
 */
function pullBox(boxPos, playerPos) {
    if ($(getSquareAt(boxPos)).hasClass("box")) {
        $(window).on("keydown", function(event) {
            console.log(boxPos);
            console.log(playerPos);
            if (event.key === "Control") {
                moveBox(boxPos, playerPos);
            }
        });
    }
}
