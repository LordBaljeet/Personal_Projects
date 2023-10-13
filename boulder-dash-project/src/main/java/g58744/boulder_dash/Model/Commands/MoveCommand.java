package g58744.boulder_dash.Model.Commands;

import g58744.boulder_dash.Model.Direction;
import g58744.boulder_dash.Model.Entities.*;
import g58744.boulder_dash.Model.Game;
import g58744.boulder_dash.Model.MoveData;
import g58744.boulder_dash.Model.Position;

import java.util.Stack;

public class MoveCommand implements Command{

    private final Stack<MoveData> moves;
    private final Game game;
    private final MovableEntity player;
    private final Position destination;

    public MoveCommand(Game game, MovableEntity player, Position destination) {
        this.game = game;
        this.player = player;
        this.destination = destination;
        moves = new Stack<>();
    }

    @Override
    public void execute() {
        if(player.getPos().x() != destination.x()) {
            Direction dir = player.getPos().x() > destination.x() ? Direction.W : Direction.E;
            MoveData pushData = game.managePushingBehaviour(destination, dir);
            if(pushData != null) moves.push(pushData);
        }
        Entity landingComp = game.getBoard().getEntity(destination);
        MoveData data = new MoveData(player.getPos(), destination, player, landingComp);
        player.move(destination, game.getBoard());
        if(landingComp.getType() == EntityType.DIAMOND) {
            game.incrementCollectedDiamonds();
            game.unTrackEntity((DeadlyEntity) landingComp);
        }
        moves.push(data);
        for (MoveData fallingCompData: game.manageFallingComponents()) {
            if(fallingCompData != null) moves.push(fallingCompData);
        }
    }

    @Override
    public void unexecute() {
        //To show only the very first state of the board, before any falling
        boolean playerFound = false;
        while(!moves.isEmpty() && !playerFound) {
            MoveData data = moves.pop();
            data.predator().move(data.start(), game.getBoard());
            game.getBoard().replaceEntity(data.end(), data.prey());
            if(data.predator() == game.getPlayer()){
                playerFound = true;
                if(!moves.isEmpty() && moves.peek().start() == data.end()) {
                    MoveData rockData = moves.pop();
                    rockData.predator().move(rockData.start(), game.getBoard());
                    game.getBoard().replaceEntity(rockData.end(), rockData.prey());
                }
            }
            if(data.prey().getType() == EntityType.DIAMOND) {
                game.decrementCollectedDiamonds();
                game.trackEntity((DeadlyEntity) data.prey());
            }
        }
    }
}