package jtwod.examples.pong.game;

import jtwod.engine.EntityController;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Vector;

public class PongEntityController extends EntityController<PongEngine> {

    // Entities
    public static Paddle paddle1;
    public static Paddle paddle2;
    public static Ball ball;
    
    public PongEntityController(Scene<PongEngine> screen) {
        super(screen);
        
        PongEntityController.paddle1 = new Paddle(new Vector(PongEngine.paddleToWallPadding, 0), this.getParentScreen(), 1);
        PongEntityController.paddle2 = new Paddle(
                Vector.Max(this.getParentEngine())
                    .plusX(-(PongEngine.paddleDimensions.getWidth() + PongEngine.paddleToWallPadding))
                    .plusY(-PongEngine.paddleDimensions.getHeight()), 
                this.getParentScreen(),
                2
        );
        
        ball = new Ball(Vector.Zero(), this.getParentScreen());
        ball.startOnPaddle(paddle1);
        
        this.spawnEntity(ball);
        this.spawnEntity(paddle1);
        this.spawnEntity(paddle2);
    }
    
    @Override
    public final void runControlTick()
    {
        if (! ball.isStarted()) {
            if ( ball.currentPaddle == 1) {
                ball.setToSpawnLocation(paddle1);
            } else {
                ball.setToSpawnLocation(paddle2);
            }
        }
    }
}
