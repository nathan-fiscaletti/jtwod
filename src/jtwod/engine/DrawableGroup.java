package jtwod.engine;

import javafx.scene.Parent;
import jtwod.engine.drawable.Shape;

import java.awt.Graphics;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Class used to store a group of <code>Drawable</code>s.
 *
 * @param <ParentEngine> 
 * The type for the parent <code>{@link jtwod.engine.Engine Engine}</code> 
 * associated with this 
 * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>.
 * 
 * @see DrawableGroup#addDrawable(jtwod.engine.Drawable) 
 * @see DrawableGroup#removeDrawable(jtwod.engine.Drawable) 
 * @see Drawable#getParentDrawableGroup() 
 * @see Drawable#getSubDrawableGroup() 
 */
public final class DrawableGroup<
    ParentEngine extends Engine
> extends Drawable<ParentEngine>
{
    /**
     * The list of <code>{@link jtwod.engine.Drawable Drawable}</code>s
     * associated with their own layers.
     */
    private final LinkedList<Drawable<ParentEngine>> drawables;
    
    /**
     * Construct the
     * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>
     * with it's parent <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @param engine The parent <code>{@link jtwod.engine.Engine Engine}</code>
     *               with which this DrawableGroup will be associated.
     */
    public DrawableGroup(ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(-1, engine, scene, false);
        this.drawables  = new LinkedList<>();
    }
    
    /**
     * Add a <code>{@link jtwod.engine.Drawable Drawable}</code> to this
     * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>.
     *
     * @param drawable The <code>{@link jtwod.engine.Drawable Drawable}</code>
     *                 to add.
     */
    public final void addDrawable(Drawable<ParentEngine> drawable)
    {
        if (drawable.getParentDrawableGroup() != null)
        {
            drawable.getParentDrawableGroup().removeDrawable(drawable);
        }
        
        drawable.setParentDrawableGroup(this);
        this.drawables.add(drawable);
        
        drawables.sort(Comparator.comparingInt(Drawable::getLayer));

        if (drawable instanceof Shape) {
            this.getParentScene().addMouseListener(((Shape)drawable).getMouseAdapter());
        }
    }

    /**
     * Remove a <code>{@link jtwod.engine.Drawable Drawable}</code> from this
     * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>.
     *
     * @param drawable The <code>{@link jtwod.engine.Drawable Drawable}</code>
     *                 to remove.
     */
    public final void removeDrawable(Drawable<ParentEngine> drawable)
    {
        if (this.drawables.contains(drawable)) {
            if (drawable instanceof Shape) {
                this.getParentScene().removeMouseListener(((Shape)drawable).getMouseAdapter());
            }
            this.drawables.remove(drawable);
            drawable.setParentDrawableGroup(null);
        }
    }
    
    /**
     * Invoke <code>{@link jtwod.engine.Drawable#update() Drawable.update}</code>
     * on each <code>{@link jtwod.engine.Drawable Drawable}</code> in this
     * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>.
     * 
     * @see jtwod.engine.Drawable#update()
     */
    @Override
    protected final void update()
    {
        this.drawables.forEach(Drawable::notifyUpdate);
    }
    
     /**
     * Invoke <code>{@link jtwod.engine.Drawable#render Drawable.render}</code>
     * on each <code>{@link jtwod.engine.Drawable Drawable}</code> in this
     * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>.
     * 
     * @param graphics The AWT <code>Graphics</code> object to use.
     * @param scene The <code>{@link jtwod.engine.Scene Scene}</code> to which
     *              the <code>{@link java.awt.Graphics Graphics}</code> are
     *              being rendered.
     * 
     * @see jtwod.engine.Drawable#render(java.awt.Graphics, jtwod.engine.Scene)
     */
    @Override
    protected final void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        if (this.isVisible()) {
            drawables.stream().filter(
                    (drawable) -> (drawable.isVisible())
            ).forEachOrdered((drawable) -> drawable.render(graphics, scene));
        }
    }
}
