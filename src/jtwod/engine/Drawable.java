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
     * If set to false, this Drawable will not render out.
     */
    private boolean isVisible = true;
    
    /**
     * If set to false, you will not be able to add child Drawables to this Drawable.
     */
    private boolean canHaveChildren = true;

    /**
     * The layer on which to render the Drawable.
     */
    private int layer = 0;

    /**
     * The parent engine for this Drawable.
     */
    private ParentEngine parentEngine;
    
    /**
     * The DrawableGroup associated with this Drawable, if any.
     */
    private DrawableGroup<ParentEngine> parentDrawableGroup;
    
    /**
     * The Drawables to render out to this Scene.
     */
    private DrawableGroup<ParentEngine> subDrawableGroup;
    
    /**
     * Create the Drawable with a parent Engine and assign it to the specified layer.
     *
     * @param layer
     * @param engine
     */
    public Drawable(int layer, ParentEngine engine)
    {
        this.layer = layer;
        this.parentEngine = engine;
        this.subDrawableGroup = new DrawableGroup<ParentEngine>(engine);
    }
    
    /**
     * Create the Drawable with a parent Engine and assign it to the specified layer.
     *
     * @param layer
     * @param engine
     * @param canHaveChildren
     */
    public Drawable(int layer, ParentEngine engine, boolean canHaveChildren)
    {
        this.layer = layer;
        this.parentEngine = engine;
        
        if (canHaveChildren) {
            this.subDrawableGroup = new DrawableGroup<ParentEngine>(engine);
        } else {
            this.canHaveChildren = false;
        }
    }

    /**
     * Render graphics out to a screen.
     *
     * @param graphics
     * @param scene
     */
    public void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        this.subDrawableGroup.render(graphics, scene);
    }

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
     * Update whether or not this Drawable should render.
     *
     * @param shouldRenderWhenGlobal
     */
    public final void setVisible(boolean visible)
    {
        this.isVisible = visible;
    }

    /**
     * Checks if this Drawable should Render.
     *
     * @return
     */
    public final boolean isVisible()
    {
        return this.isVisible;
    }

    /**
     * Retrieves the layer on which to render this Drawable.
     *
     * @return
     */
    public final int getLayer()
    {
        return this.layer;
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
    
    /**
     * Retrieve the DrawableGroup associated with this Drawable, if any.
     *
     * @return
     */
    public final DrawableGroup<ParentEngine> getParentDrawableGroup()
    {
        return this.parentDrawableGroup;
    }
    
    public final DrawableGroup<ParentEngine> getSubDrawableGroup()
    {
        if (! this.canHaveChildren) {
            System.err.print("Warning: Trying to access sub DrawableGroup on a childless Drawable.");
        }
        return this.subDrawableGroup;
    }
    
    /**
     * Updates which parent DrawableGroup is associated with this Drawable.
     *
     * @param group
     */
    public final void setParentDrawableGroup(DrawableGroup<ParentEngine> group)
    {
        this.parentDrawableGroup = group;
    }
}
