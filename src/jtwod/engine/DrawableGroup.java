package jtwod.engine;

import java.awt.Graphics;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Class used to store a group of Drawables.
 * @param <ParentEngine>
 */
public final class DrawableGroup<ParentEngine extends Engine> extends Drawable<ParentEngine> {
    
    /**
     * Construct the Drawable Group with it's parent engine.
     *
     * @param engine
     */
    public DrawableGroup(ParentEngine engine) {
        super(-1, engine, false);
    }

    /**
     * The list of Drawables associated with their own layers.
     */
    private LinkedList<Drawable<ParentEngine>> drawables = new LinkedList<>();
    
    /**
     * Add a Drawable to this Scene.
     *
     * @param drawable The Drawable to add.
     */
    public final void addDrawable(Drawable<ParentEngine> drawable)
    {
        if (drawable.getParentDrawableGroup() != null)
        {
            drawable.getParentDrawableGroup().removeDrawable(drawable);
        }
        
        drawable.setParentDrawableGroup(this);
        this.drawables.add(drawable);
        
        Collections.sort(drawables, new Comparator<Drawable<ParentEngine>>() {
            @Override
            public int compare(Drawable<ParentEngine> drawable1, Drawable<ParentEngine> drawable2) {
                return drawable1.getLayer() - drawable2.getLayer();
            }
        });
    }

    /**
     * Remove a Drawable from this DrawableGroup.
     *
     * @param drawable The Drawable to remove.
     */
    public final void removeDrawable(Drawable<ParentEngine> drawable)
    {
        if (this.drawables.contains(drawable)) {
            this.drawables.remove(drawable);
            drawable.setParentDrawableGroup(null);
        }
    }
    
    @Override
    public final void update()
    {
        for(Drawable<ParentEngine> drawable : this.drawables)
        {
            drawable.update();
        }
    }
    
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        for (Drawable<ParentEngine> drawable : drawables)
        {
            if (drawable.isVisible()) {
                drawable.render(graphics, scene);
            }
        }
    }
}
