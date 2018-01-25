package jtwod.engine;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class for implementing both render and update methods.
 *
 * @author Nathan
 */
public abstract class Drawable<ParentEngine extends Engine> extends KeyAdapter {

    /**
     * Enumeration used to define Center constraints in Drawable objects.
     */
    public enum Center {
        Vertically,
        Horizontally,
        Parent,
        None
    }

    /**
     * If set to false, this will not render when it is in the global render scope.
     */
    private boolean shouldRenderWhenGlobal = true;

    /**
     * If set to true, this render will render on top of everything else.
     */
    private boolean topMost = false;

    /**
     * The parent engine for this Drawable.
     */
    private ParentEngine parentEngine;

    /**
     * Create the renderer with a parent engine.
     *
     * @param engine
     */
    public Drawable(ParentEngine engine)
    {
        this.parentEngine = engine;
    }

    /**
     * Render graphics out to a screen.
     *
     * @param graphics
     * @param screen
     */
    protected abstract void render(Graphics graphics, Scene<ParentEngine> screen);

    /**
     * Update this renderer.
     */
    protected abstract void update();
    
    /**
     * Event triggered when a key is pressed.
     *
     * @param e The event associated with the key press.
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
    		// Not implemented by default.
    }
    
    /**
     * Event triggered when a key is pressed.
     *
     * @param e The event associated with the key release.
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
    		// Not implemented by default.
    }

    /**
     * Update whether or not this Drawable should render in the global scope.
     *
     * @param shouldRenderWhenGlobal
     */
    public final void setShouldRenderWhenGlobal(boolean shouldRenderWhenGlobal)
    {
        this.shouldRenderWhenGlobal = shouldRenderWhenGlobal;
    }

    /**
     * Checks if this renderer should render in the global scope.
     *
     * @return
     */
    public final boolean shouldRenderWhenGlobal()
    {
        return this.shouldRenderWhenGlobal;
    }

    /**
     * Update whether or not this Drawable should render on top of everything else.
     *
     * @param topMost
     */
    public final void setTopMost(boolean topMost)
    {
        this.topMost = topMost;
    }

    /**
     * Checks if this Drawable should render on top of everything else.
     *
     * @return
     */
    public final boolean isTopMost()
    {
        return this.topMost;
    }

    /**
     * Retrieve the parent engine.
     *
     * @return
     */
    public final ParentEngine getParentEngine()
    {
        return this.parentEngine;
    }
}
