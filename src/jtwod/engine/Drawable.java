package jtwod.engine;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class for implementing both render and update methods.
 *
 * @param <ParentEngine> The type for the parent 
 *                       <code>{@link jtwod.engine.Engine Engine}</code> 
 *                       associated with this
 *                       <code>{@link jtwod.engine.Drawable Drawable}</code>.
 * 
 * @see Drawable#update() 
 * @see Drawable#render(java.awt.Graphics, jtwod.engine.Scene) 
 * @see Drawable#keyPressed(java.awt.event.KeyEvent) 
 * @see Drawable#keyReleased(java.awt.event.KeyEvent) 
 * @see Drawable#getParentDrawableGroup() 
 * @see Drawable#getSubDrawableGroup() 
 */
public abstract class Drawable<ParentEngine extends Engine> extends KeyAdapter
{
    /**
     * Enumeration used to define Center constraints in
     * <code>{@link jtwod.engine.Drawable Drawable}</code> objects.
     */
    public enum Center
    {
        Vertically,
        Horizontally,
        Parent,
        None
    }

    /**
     * If set to false, this <code>{@link jtwod.engine.Drawable Drawable}</code>
     * will not render out.
     */
    private boolean isVisible = true;
    
    /**
     * If set to false, you will not be able to add child 
     * <code>{@link jtwod.engine.Drawable Drawable}</code>s to this 
     * <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    private boolean allowChildren = true;

    /**
     * The layer on which to render the
     * <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    private int layer = 0;

    /**
     * The parent <code>{@link jtwod.engine.Engine Engine}</code> for this
     * <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    private final ParentEngine parentEngine;
    
    /**
     * The <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code> 
     * associated with this <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    private DrawableGroup<ParentEngine> parentDrawableGroup;
    
    /**
     * The <code>{@link jtwod.engine.Drawable Drawable}</code>s to render out
     * through this <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    private DrawableGroup<ParentEngine> subDrawableGroup;
    
    /**
     * Create the <code>{@link jtwod.engine.Drawable Drawable}</code> with a
     * parent <code>{@link jtwod.engine.Engine Engine}</code> and assign it to
     * the specified layer.
     *
     * @param layer The layer on which to render this
     *              <code>{@link jtwod.engine.Drawable Drawable}</code>.
     * @param engine The parent <code>{@link jtwod.engine.Engine Engine}</code>
     *               associated with this 
     *               <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    public Drawable(int layer, ParentEngine engine)
    {
        this.layer = layer;
        this.parentEngine = engine;
        this.subDrawableGroup = new DrawableGroup<>(engine);
    }
    
    /**
     * Create the <code>{@link jtwod.engine.Drawable Drawable}</code> with a
     * parent <code>{@link jtwod.engine.Engine Engine}</code> and assign it to
     * the specified layer.
     *
     * @param layer The layer on which to render this
     *              <code>{@link jtwod.engine.Drawable Drawable}</code>.
     * @param engine The parent <code>{@link jtwod.engine.Engine Engine}</code>
     *               associated with this 
     *               <code>{@link jtwod.engine.Drawable Drawable}</code>.
     * @param allowChildren If set to true, child 
     *                      <code>{@link jtwod.engine.Drawable Drawable}</code>s 
     *                      can be added as sub-components to this
     *                      <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    public Drawable(int layer, ParentEngine engine, boolean allowChildren)
    {
        this.layer = layer;
        this.parentEngine = engine;
        
        if (allowChildren) {
            this.subDrawableGroup = new DrawableGroup<>(engine);
        } else {
            this.allowChildren = false;
        }
    }

    /**
     * Render <code>{@link java.awt.Graphics Graphics}</code> out to a
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @param graphics The <code>{@link java.awt.Graphics Graphics}</code>
     *                 object to use for rendering.
     * @param scene The <code>{@link jtwod.engine.Scene Scene}</code> on which
     *              this <code>render</code> invocation will occur.
     */
    protected void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        this.subDrawableGroup.render(graphics, scene);
    }

    /**
     * Override to control what happens during each tick on this
     * <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    protected abstract void update();
    
    /**
     * Event triggered when a key is pressed.
     *
     * @param keyEvent The <code>{@link java.awt.event.KeyEvent KeyEvent}</code>
     * object associated with the key press.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        // Not implemented by default.
    }
    
    /**
     * Event triggered when a key is released.
     *
     * @param keyEvent The <code>{@link java.awt.event.KeyEvent KeyEvent}</code>
     * object associated with the key release.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        // Not implemented by default.
    }

    /**
     * Update whether or not this 
     * <code>{@link jtwod.engine.Drawable Drawable}</code> should render.
     *
     * @param visible The new visibility to use.
     */
    public final void setVisible(boolean visible)
    {
        this.isVisible = visible;
    }

    /**
     * Checks if this <code>{@link jtwod.engine.Drawable Drawable}</code>
     * should Render.
     *
     * @return True if this <code>{@link jtwod.engine.Drawable Drawable}</code>
     *         should be rendered out to it's parent 
     *         <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    public final boolean isVisible()
    {
        return this.isVisible;
    }

    /**
     * Retrieves the layer on which to render this
     * <code>{@link jtwod.engine.Drawable Drawable}</code>.
     *
     * @return The integral layer ID on which to Render this
     *         <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    public final int getLayer()
    {
        return this.layer;
    }

    /**
     * Retrieve the parent <code>{@link jtwod.engine.Engine Engine}</code>
     * associated with this <code>{@link jtwod.engine.Drawable Drawable}</code>.
     *
     * @return The parent <code>{@link jtwod.engine.Engine Engine}</code>
     *         associated with this
     *         <code>{@link jtwod.engine.Drawable Drawable}</code>.
     */
    public final ParentEngine getParentEngine()
    {
        return this.parentEngine;
    }
    
    /**
     * Retrieve the <code>{@link DrawableGroup DrawableGroup}</code> associated
     * with this <code>{@link jtwod.engine.Drawable Drawable}</code>.
     *
     * @return The <code>{@link DrawableGroup DrawableGroup}</code>.
     * 
     * @see Drawable#getSubDrawableGroup
     * @see Drawable#setParentDrawableGroup(jtwod.engine.DrawableGroup)
     */
    public final DrawableGroup<ParentEngine> getParentDrawableGroup()
    {
        return this.parentDrawableGroup;
    }
    
    /**
     * Retrieve the child <code>{@link DrawableGroup DrawableGroup}</code>
     * associated with this <code>{@link jtwod.engine.Drawable Drawable}</code>.
     * 
     * @return The <code>{@link DrawableGroup DrawableGroup}</code>.
     * 
     * @see Drawable#getParentDrawableGroup
     * @see Drawable#setParentDrawableGroup(jtwod.engine.DrawableGroup)
     */
    public final DrawableGroup<ParentEngine> getSubDrawableGroup()
    {
        if (! this.allowChildren) {
            System.err.print(
                "Warning: Trying to access sub DrawableGroup on a Drawable with"
              + "it's allowChildren property disabled."
            );
        }
        return this.subDrawableGroup;
    }
    
    /**
     * Updates which parent <code>{@link DrawableGroup DrawableGroup}</code>
     * is associated with this 
     * <code>{@link jtwod.engine.Drawable Drawable}</code>.
     *
     * @param group The new <code>{@link DrawableGroup DrawableGroup}</code>.
     * 
     * @see Drawable#getSubDrawableGroup
     * @see Drawable#getParentDrawableGroup
     */
    public final void setParentDrawableGroup(DrawableGroup<ParentEngine> group)
    {
        this.parentDrawableGroup = group;
    }
}
