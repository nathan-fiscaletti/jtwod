package jtwod.examples.pong.game;

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
    
}
