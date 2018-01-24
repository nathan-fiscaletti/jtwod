package jtwod.examples.pong.game;


import jtwod.engine.Engine;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.AspectRatio;
import jtwod.engine.metrics.Dimensions;

public class PongEngine extends Engine {

    
    // Configuration
    public final static int startingBallSpeed = 15;    
    public final static int paddleToWallPadding = 25;
    public final static int ballToPaddlePadding = 10;
    public final static int paddleSpeed = 10;
    public final static Dimensions paddleDimensions = new Dimensions(20, 75);
    public final static Dimensions ballDimensions = new Dimensions(20, 20);
    
    // Game Variables
    public static int round = 1;
    
    public PongEngine()
    {
        super (
            "Pong",
            new Dimensions (
                new AspectRatio(
                    new Dimensions(12, 9),
                    AspectRatio.AspectRatioControlAxis.WidthControlled
                )
            ).setWidth(800)
        );
    }
    
    @Override
    public final void loadTextures()
    {
        this.getTextureGroup().addTexture("Ball", Texture.whiteTexture(ballDimensions));
        this.getTextureGroup().addTexture("Paddle", Texture.whiteTexture(paddleDimensions));
    }
    
    @Override
    public void onEngineStart() {
        /**
         * Add the background to the global drawables.
         */
        this.addGlobalDrawable(new PongBackground(this));
        
        /**
         * Set the main screen.
         */
        this.setScreen(new PongScene(this));
    }
    
}
