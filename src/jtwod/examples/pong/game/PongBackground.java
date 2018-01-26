package jtwod.examples.pong.game;

import java.awt.Color;

import jtwod.engine.Drawable;
import jtwod.engine.drawable.Image;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

public class PongBackground extends Drawable<PongEngine>
{
    /**
     * Construct the background and set it to be bottom most.
     *
     * @param engine The parent engine for this Drawable.
     */
    public PongBackground(PongEngine engine)
    {
        // Set this to layer 0.
        super(0, engine);
        
        // Add the background image
        this.getSubDrawableGroup().addDrawable(new Image<PongEngine>(
            0, // First layer
            Texture.colorTexture(Color.black,
                new Dimensions(
                    this.getParentEngine().getWindowSize().getWidth(),
                    this.getParentEngine().getWindowSize().getHeight()
                )
            ),
            Vector.Zero(),
            this.getParentEngine()
        ));
    }
    
    @Override
    protected final void update() {
        // Not implemented.
    }
}
