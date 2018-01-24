package jtwod.examples.pong.game;

import java.awt.event.KeyEvent;

import jtwod.engine.Scene;
import jtwod.engine.drawable.Entity;
import jtwod.engine.metrics.Vector;

public class Paddle extends Entity<PongEngine> {
    public int paddleID = 0;
    public int score = 0;
    
    public Paddle(Vector position, Scene<PongEngine> screen, int paddleID) {
        super(position, screen);
        this.paddleID = paddleID;
        this.setRenderedTexture(this.getParentEngine().getTextureGroup().getTexture("Paddle"));
        this.setPositionConstraint(Vector.Max(this.getParentEngine()).plusY(-this.getSize().getHeight()));
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
