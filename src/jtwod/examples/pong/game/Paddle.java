package jtwod.examples.pong.game;

import java.awt.event.KeyEvent;

import jtwod.engine.EntityController;
import jtwod.engine.Scene;
import jtwod.engine.drawable.Entity;
import jtwod.engine.metrics.Vector;

public class Paddle extends Entity<PongEngine> {
    public int paddleID = 0;
    public int score = 0;


    public Paddle(Vector position, Scene<PongEngine> scene, int paddleID) {
        super(position, scene.getParentEngine().getTextureGroup().getTexture("Paddle"), scene);
        this.paddleID = paddleID;
        this.setPositionConstraint(Vector.Max(this.getParentEngine()).plusY(-this.getSize().getHeight()));
    }

    int curTick = 0;
    @Override
    public final void update()
    {
        if (this.paddleID == 1) {
            if (! PongEntityController.aiHitBallLast) {
                if (PongEntityController.ball.getPosition().getY() < this.getPosition().getY() + (this.getSize().getHeight() / 2)) {
                    this.setVelocity(Vector.Zero().setY(-(this.random.nextInt((PongEngine.paddleSpeed - PongEngine.aiPaddleSpeed) + 1) + PongEngine.aiPaddleSpeed)));
                } else if (PongEntityController.ball.getPosition().getY() > this.getPosition().getY() + (this.getSize().getHeight() / 2)) {
                    this.setVelocity(Vector.Zero().setY(this.random.nextInt((PongEngine.paddleSpeed - PongEngine.aiPaddleSpeed) + 1) + PongEngine.aiPaddleSpeed));
                }
            } else {
                if (curTick % (this.getParentScene().getTps() / 2) == 0) {
                    int movement = this.random.nextInt((PongEngine.paddleSpeed - PongEngine.aiPaddleSpeed) + 1) + PongEngine.aiPaddleSpeed;
                    this.setVelocity(Vector.Zero().setY(this.random.nextBoolean() ? -movement  : movement));
                }
            }
        }

        curTick++;
    }
    
    @Override
    public final void keyPressed(KeyEvent keyEvent)
    {
            if (this.paddleID == 2) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                this.setVelocity(Vector.Zero().setY(PongEngine.paddleSpeed));
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                this.setVelocity(Vector.Zero().setY(-PongEngine.paddleSpeed));
            }
            }
            
            if (this.paddleID == 1) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
                this.setVelocity(Vector.Zero().setY(-PongEngine.paddleSpeed));
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
                this.setVelocity(Vector.Zero().setY(PongEngine.paddleSpeed));
            }
            }
    }
    
    @Override
    public final void keyReleased(KeyEvent keyEvent) {
        
            if (this.paddleID == 2) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                this.setVelocity(Vector.Zero());
                if (PongEntityController.ball.currentPaddle == 1) {
                    if (! PongEntityController.ball.isStarted()) {
                            PongEntityController.ball.setVelocity(Vector.Zero());
                    }
                }
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                this.setVelocity(Vector.Zero());
                if (PongEntityController.ball.currentPaddle == 1) {
                    if (! PongEntityController.ball.isStarted()) {
                            PongEntityController.ball.setVelocity(Vector.Zero());
                    }
                }
            }
            }
        
            else if (this.paddleID == 1) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
                this.setVelocity(Vector.Zero());
                if (PongEntityController.ball.currentPaddle == 2) {
                    if (! PongEntityController.ball.isStarted()) {
                            PongEntityController.ball.setVelocity(Vector.Zero());
                    }
                }
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
                this.setVelocity(Vector.Zero());
                if (PongEntityController.ball.currentPaddle == 2) {
                    if (! PongEntityController.ball.isStarted()) {
                            PongEntityController.ball.setVelocity(Vector.Zero());
                    }
                }
            }
        }
    }
    
}
