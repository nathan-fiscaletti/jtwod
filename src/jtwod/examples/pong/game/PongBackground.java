package jtwod.examples.pong.game;

import java.awt.Color;
import java.awt.Graphics;

import jtwod.engine.Drawable;
import jtwod.engine.Scene;
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
		super(engine);
        this.setTopMost(false);
    }
    
    /*
     * Render the graphics for the Drawable out.
     *
     * @see jtwod.engine.Drawable#render(java.awt.Graphics, jtwod.engine.Screen)
     */
    @Override
    protected final void render(Graphics graphics, Scene<PongEngine> screen) {
        // Create the background.
        Image<PongEngine> image = new Image<PongEngine>(
            Texture.colorTexture(Color.black,
                new Dimensions(
                    (int)this.getParentEngine().getWindowSize().getWidth(),
                    (int)this.getParentEngine().getWindowSize().getHeight()
                )
            ),
            Vector.Zero(),
            this.getParentEngine()
        );
        
        // Render it out
        image.render(graphics, screen);
    }

    @Override
    protected final void update() {
        // Not implemented.
    }
}
