package jtwod.examples.pong.game;

import java.awt.event.KeyEvent;
import java.util.Random;

import jtwod.engine.Scene;
import jtwod.engine.drawable.Entity;
import jtwod.engine.metrics.Vector;

public class Ball extends Entity<PongEngine> {

    private boolean started = false;
    public int currentPaddle = 1;
    
    public int worth = 1;
    
    public Ball(Vector position, Scene<PongEngine> screen) {
        super(position, screen.getParentEngine().getTextureGroup().getTexture("Ball"), screen);
        this.setPositionConstraint(Vector.Zero(), Vector.Max(this.getSize().getWidth(), 0, getParentEngine()));
    }
    
    @Override
    public final void onCollide(Entity<PongEngine> entity)
    {
        if (this.started) {
            if (entity instanceof Paddle) {
                Vector currentVelocity = this.getVelocity();
                this.setVelocity(currentVelocity.setX(-(currentVelocity.getX())));
                this.worth += 1;

                if (((Paddle)entity).paddleID == 1) {
                    PongEntityController.aiHitBallLast = true;
                } else {
                    PongEntityController.aiHitBallLast = false;
                }
            }
        }
    }

    @Override
    public final void update()
    {
        // Not implemented.
    }
    
    @Override
    public final void keyPressed(KeyEvent keyEvent)
    {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            if (! this.isStarted()) {
                this.setStarted(true);
                if (currentPaddle == 2) {
                    PongEntityController.aiHitBallLast = false;
                } else {
                    PongEntityController.aiHitBallLast = true;
                }
            }
        }
    }
    
    @Override
    protected void onConstrained(AxisEventType event)
    {
        switch (event) {
            case TopY :
            case BottomY :
                Vector currentVelocity = this.getVelocity();
                this.setVelocity(currentVelocity.setY(-(currentVelocity.getY())));
                break;
            case LeftX:
                    PongEntityController.paddle2.score += this.worth;
                this.worth = 1;
                PongEngine.round += 1;
                this.startOnPaddle(PongEntityController.paddle1);
                break;
            case RightX:
                PongEntityController.paddle1.score += this.worth;
                this.worth = 1;
                PongEngine.round += 1;
                this.startOnPaddle(PongEntityController.paddle2);
                break;
        }
    }
    
    public void startOnPaddle(Paddle paddle)
    {
        this.currentPaddle = paddle.paddleID;
        //this.getParentScene().getEntityController(PongEntityController.class).aiHitBallLast = true;
        this.setVelocity(Vector.Zero());
        this.setStarted(false);
    }
    
    public void setToSpawnLocation(Paddle paddle)
    {
        if ( this.currentPaddle == 1) {
            this.setPosition(new Vector(paddle.getPosition().getX() + paddle.getSize().getWidth() + PongEngine.ballToPaddlePadding, paddle.getPosition().getY() + (paddle.getSize().getHeight() / 2) - this.getSize().getHeight() / 2));
        } else {
            this.setPosition(new Vector(paddle.getPosition().getX() - ( PongEngine.ballToPaddlePadding + this.getSize().getWidth()), paddle.getPosition().getY() + (paddle.getSize().getHeight() / 2) - (this.getSize().getHeight() / 2)));
        }
    }
    
    private void setStarted(boolean value)
    {
        this.started = value;
        if (started) {
            if (this.currentPaddle == 1) {
                this.setVelocity(new Vector(10, new Random().nextBoolean() ? 10 : -10));
            } else {
                this.setVelocity(new Vector(-10, new Random().nextBoolean() ? 10 : -10));
            }
        }
    }
    
    public boolean isStarted()
    {
        return this.started;
    }
}
