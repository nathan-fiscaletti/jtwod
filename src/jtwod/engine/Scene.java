package jtwod.engine;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

/**
 * Represents a <code>{@link jtwod.engine.Scene Scene}</code> that can be
 * rendered out through an <code>{@link jtwod.engine.Engine Engine}</code>.
 *
 * @param <ParentEngine> 
 * The type for the parent <code>{@link jtwod.engine.Engine Engine}</code> 
 * associated with this 
 * <code>{@link jtwod.engine.Scene Scene}</code>.
 * 
 * @see jtwod.engine.Scene#allocate() 
 * @see jtwod.engine.Scene#deallocate() 
 * @see jtwod.engine.Scene#update() 
 * @see jtwod.engine.Scene#keyPressed(java.awt.event.KeyEvent) 
 * @see jtwod.engine.Scene#keyReleased(java.awt.event.KeyEvent) 
 */
public abstract class Scene<
    ParentEngine extends Engine
> extends Canvas implements Runnable
{
    /**
     * The Serial Version UID for this Serializable Object.
     */
    private static final long serialVersionUID = 5380604501382560639L;
    
    /**
     * The <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code> to
     * render out to this <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private final DrawableGroup<ParentEngine> drawableGroup;

    /**
     * The name of this <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private final String name;

    /**
     * The <code>{@link jtwod.engine.EntityController EntityController}</code>
     * for managing the 
     * <code>{@link jtwod.engine.drawable.Entity Entity}</code>s
     * Rendered through this <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private EntityController<ParentEngine> controller;

    /**
     * The Ticks Per Second for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private int tps;

    /**
     * The Frames Per Second for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private int fps;

    /**
     * Used to control the state of the primary
     * <code>{@link java.lang.Thread Thread}</code> attached to this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private boolean running = false;

    /**
     * The primary <code>{@link java.lang.Thread Thread}</code> for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    private Thread thread;

    /**
     * The parent <code>{@link jtwod.engine.Engine Engine}</code> that this
     * <code>{@link jtwod.engine.Scene Scene}</code> is attached to.
     */
    private final ParentEngine parentEngine;

    /**
     * Initialize the <code>{@link jtwod.engine.Scene Scene}</code> with a
     * parent <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @param name
     * The name of the <code>{@link jtwod.engine.Scene Scene}</code>.
     * @param engine 
     * The parent <code>{@link jtwod.engine.Engine Engine}</code> this
     * <code>{@link jtwod.engine.Scene Scene}</code> is attached to.
     */
    public Scene(String name, ParentEngine engine)
    {
        this.name = name;
        this.parentEngine = engine;
        this.controller = new EntityController<ParentEngine>(this) {};
        this.drawableGroup = new DrawableGroup(this.getParentEngine());
    }

    /**
     * Initialize the <code>{@link jtwod.engine.Scene Scene}</code> with a
     * parent <code>{@link jtwod.engine.Engine Engine}</code> and an
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     *
     * @param name
     * The name of the <code>{@link jtwod.engine.Scene Scene}</code>.
     * @param engine 
     * The parent <code>{@link jtwod.engine.Engine Engine}</code> this
     * <code>{@link jtwod.engine.Scene Scene}</code> is attached to.
     * @param controller 
     * The <code>{@link jtwod.engine.EntityController EntityController}</code>
     * to attach with this <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    public Scene(
        String name,
        ParentEngine engine,
        EntityController<ParentEngine> controller
    ) {
        this.name = name;
        this.parentEngine = engine;
        this.controller = controller;
        this.drawableGroup = new DrawableGroup<>(this.getParentEngine());
    }

    /**
     * Override this function to control how the
     * <code>{@link jtwod.engine.Scene Scene}</code> is prepared.
     */
    protected void allocate() {
        // Not implemented by default.
    }
    
    /**
     * Override this function to control what happens after this
     * <code>{@link jtwod.engine.Scene Scene}</code> has been stopped.
     */
    protected void deallocate()
    {
        // Not implemented by default.
    }

    /**
     * Override to control what happens on each tick of this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    protected void update()
    {
        // Not implemented by default.
    }

    /**
     * Called when a key is pressed down.
     *
     * @param keyEvent The <code>{@link java.awt.event.KeyEvent KeyEvent}</code>
     * object associated with the key press.
     */
    protected void keyPressed(KeyEvent keyEvent)
    {
        // Not implemented by default.
    }

    /**
     * Called when a key is released.
     *
     * @param keyEvent The <code>{@link java.awt.event.KeyEvent KeyEvent}</code>
     * object associated with the key release.
     */
    protected void keyReleased(KeyEvent keyEvent)
    {
        // Not implemented by default.
    }

    /**
     * Primary <code>{@link java.lang.Thread Thread}</code> body for controlling
     * the <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    @Override
    public final void run() {
        init();
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                runUpdate();
                updates++;
                delta--;
            }
            renderFrame();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                tps = updates;
                fps = frames;
                updates = 0;
                frames = 0;
            }
        }
        this.deallocate();
    }

    /**
     * Initialize the <code>{@link jtwod.engine.Scene Scene}</code>s primary
     * <code>{@link java.lang.Thread Thread}</code>.
     */
    public final synchronized void start()
    {
        if (this.running) {
            return;
        }

        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Stop the <code>{@link jtwod.engine.Scene Scene}</code>s primary
     * <code>{@link java.lang.Thread Thread}</code>.
     */    
    public final synchronized void stop()
    {
        this.running = false;
    }

    /**
     * Retrieve the name for the <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @return The name for this <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    public final String getSceneName()
    {
        return this.name;
    }

    /**
     * Retrieve the
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * attached to this <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @return
     * The <code>{@link jtwod.engine.EntityController EntityController}</code>
     * attached to this <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    public final EntityController<ParentEngine> getEntityController()
    {
        return this.controller;
    }

    /**
     * Update the
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * attached to this <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @param controller
     * The new
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     */
    public final void setEntityController(
        EntityController<ParentEngine> controller
    ) {
        this.controller = controller;
    }

    /**
     * Retrieve the Ticks Per Second for the
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @return The current Ticks Per Second for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    public final int getTps()
    {
        return this.tps;
    }

    /**
     * Retrieve the Frames Per Second for the
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @return The current Frames Per Second for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     */
    public final int getFps()
    {
        return this.fps;
    }

    /**
     * Retrieve the parent <code>{@link jtwod.engine.Engine Engine}</code> that
     * this <code>{@link jtwod.engine.Scene Scene}</code> is attached to.
     *
     * @return The parent <code>{@link jtwod.engine.Engine Engine}</code>.
     */
    public final ParentEngine getParentEngine()
    {
        return this.parentEngine;
    }
    
    /**
     * Retrieve the
     * <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>
     * for this <code>{@link jtwod.engine.Scene Scene}</code>.
     * 
     * @return
     * The <code>{@link jtwod.engine.DrawableGroup DrawableGroup}</code>.
     */
    public final DrawableGroup<ParentEngine> getDrawableGroup()
    {
        return this.drawableGroup;
    }
    
    /**
     * Internal initialization function.
     */
    private void init()
    {
        this.requestFocus();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                triggerKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e){
                triggerKeyReleased(e);
            }
        });
        this.allocate();
    }
    
    /**
     * Invokes keyPressed with a
     * <code>{@link java.awt.event.KeyEvent KeyEvent}</code>.
     *
     * @param keyEvent 
     * The <code>{@link java.awt.event.KeyEvent KeyEvent}</code>.
     */
    private void triggerKeyPressed(KeyEvent keyEvent)
    {
        this.keyPressed(keyEvent);
    }
    
    /**
     * Invokes keyReleased with a
     * <code>{@link java.awt.event.KeyEvent KeyEvent}</code>.
     *
     * @param keyEvent 
     * The <code>{@link java.awt.event.KeyEvent KeyEvent}</code>.
     */
    private void triggerKeyReleased(KeyEvent keyEvent)
    {
        this.keyReleased(keyEvent);
    }
    
    /**
     * Internal renderFrame function.
     */
    private void renderFrame()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }

        Graphics graphics = bs.getDrawGraphics();
        
        this.drawableGroup.render(graphics, this);
        
        // Entities will always be rendered on top.
        if (this.controller != null) {
            this.controller.render(graphics, this);
        }

        graphics.dispose();
        try {
            bs.show();
        } catch (Exception e){}
    }

    /**
     * Internal update function.
     */
    private void runUpdate()
    {
        this.drawableGroup.update();
        
        if (this.controller != null) {
            this.controller.update();
        }

        this.update();
    }
}
