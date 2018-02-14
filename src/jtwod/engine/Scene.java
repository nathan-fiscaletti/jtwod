package jtwod.engine;

import jtwod.engine.drawable.Graph;
import jtwod.engine.drawable.Image;
import jtwod.engine.drawable.Text;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.*;
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
     * The current limit for this <code>{@link jtwod.engine.Scene Scene}</code>s
     * ticks per second.
     */
    private double tpsLimit = 60;

    /**
     * The current limit for this <code>{@link jtwod.engine.Scene Scene}</code>s
     * frames per second.
     */
    private double fpsLimit = 120;

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
     * The parent <code>{@link jtwod.engine.Engine Engine}</code> that this
     * <code>{@link jtwod.engine.Scene Scene}</code> is attached to.
     */
    private final ParentEngine parentEngine;

    /**
     * If set to false, the <code>{@link jtwod.engine.Scene Scene}</code>
     * will not be rendered.
     */
    private boolean isRendering = true;

    /**
     * The legend item for FPS color.
     */
    private Image<ParentEngine> fpsColorBlock;

    /**
     * The legend item for TPS color.
     */
    private Image<ParentEngine> tpsColorBlock;

    /**
     * The <code>{@link jtwod.engine.drawable.Text Text}</code> Renderer for the FPS.
     */
    private Text<ParentEngine> fpsRenderer;

    /**
     * The <code>{@link jtwod.engine.drawable.Text Text}</code> Renderer for the TPS.
     */
    private Text<ParentEngine> tpsRenderer;

    /**
     * The black background drawn behind everything.
     */
    private Image<ParentEngine> background;

    /**
     * A graph for representing FPS and TPS.
     */
    private Graph<ParentEngine> graphRenderer;

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
        this.controller = null;
        this.drawableGroup = new DrawableGroup<>(this.getParentEngine());
        initializeInternalDrawables(engine);
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
        initializeInternalDrawables(engine);
    }

    /**
     * Initialize the internal Drawables for this scene.
     *
     * @param engine The engine.
     */
    private void initializeInternalDrawables(ParentEngine engine)
    {
        this.tpsRenderer = new Text<ParentEngine>(
            Integer.MAX_VALUE, // Highest possible layer
            "TPS: " + this.getTps(),
            new Font("Monospaced", Font.BOLD, 9),
            Color.white,
            Vector.Zero().plusY(11).plusX(20),
            engine
        );
        this.tpsRenderer.setVisible(false);

        this.fpsRenderer = new Text<ParentEngine>(
            Integer.MAX_VALUE, // Highest possible layer
            "FPS: " + this.getFps(),
            new Font("Monospaced", Font.BOLD, 9),
            Color.white,
            Vector.Zero().plusY(24).plusX(20),
            engine
        );
        this.fpsRenderer.setVisible(false);

        this.background = new Image<ParentEngine>(
            0, // First layer
            Texture.colorTexture(Color.black,
                    new Dimensions(
                            this.getParentEngine().getWindowSize().getWidth(),
                            this.getParentEngine().getWindowSize().getHeight()
                    )
            ),
            Vector.Zero(),
            this.getParentEngine()
        );

        this.fpsColorBlock = new Image<ParentEngine>(
            Integer.MAX_VALUE,
            Texture.colorTexture(
                    Color.blue,
                    new Dimensions(10, 10)
            ),
            new Vector(5, 15),
            engine
        );
        this.fpsColorBlock.setVisible(false);

        this.tpsColorBlock = new Image<ParentEngine>(
            Integer.MAX_VALUE,
            Texture.colorTexture(
                    Color.green,
                    new Dimensions(10, 10)
            ),
            new Vector(5, 1),
            engine
        );
        this.tpsColorBlock.setVisible(false);

        this.graphRenderer = new Graph<ParentEngine>(
            Integer.MAX_VALUE,
            engine,
            Vector.Zero().plusY(44),
            new Dimensions(200, 50),
            2, 1
        ) {
            @Override
            public double getNextValueForDataSet(int dataSet) {
                switch (dataSet) {
                    case 0 : {
                        return getTps();
                    }

                    case 1 : {
                        return getFps();
                    }
                }

                return 0;
            }

            @Override
            public double getMaxValueForDataSet(int dataSet) {
                switch (dataSet) {
                    case 0 : {
                        return (int)tpsLimit;
                    }

                    case 1 : {
                        return (int)fpsLimit;
                    }
                }

                return 0;
            }

            @Override
            public Color getColorForDataSet(int dataSet) {
                switch (dataSet) {
                    case 0 : {
                        return Color.green;
                    }

                    case 1 : {
                        return Color.blue;
                    }
                }

                return Color.white;
            }
        };
        this.graphRenderer.setVisible(false);

        this.drawableGroup.addDrawable(this.fpsColorBlock);
        this.drawableGroup.addDrawable(this.tpsColorBlock);
        this.drawableGroup.addDrawable(this.graphRenderer);
        this.drawableGroup.addDrawable(this.background);
        this.drawableGroup.addDrawable(this.tpsRenderer);
        this.drawableGroup.addDrawable(this.fpsRenderer);
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
        double ticksDelta = 0;
        double framesDelta = 0;

        // Used to store how many Ticks
        // have occurred this second.
        int updates = 0;

        // Used to store how many Frames
        // have been rendered this second.
        int frames = 0;

        // Used to keep track of when a second has passed.
        long timer = System.currentTimeMillis();

        while(running) {
            // Enforce the tpsLimit currently defined in this class.
            long now = System.nanoTime();

            ticksDelta += (now - lastTime) / (1000000000 / this.tpsLimit);
            if (ticksDelta >= 1) {
                runUpdate();
                updates++;
                ticksDelta--;
            }

            framesDelta += (now - lastTime) / (1000000000 / this.fpsLimit);
            if (framesDelta >= 1) {
                renderFrame();
                frames++;
                framesDelta--;
            }

            lastTime = now;

            // Each 1/10th of a second, update the FPS and TPS.
            if(System.currentTimeMillis() - timer > 100){
                timer += 100;
                this.tps = updates*10;
                this.fps = frames*10;
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
        new Thread(this).start();
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
     * Retrieve the
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * attached to this <code>{@link jtwod.engine.Scene Scene}</code> as
     * the specified type.
     *
     * @param asType
     * The <code>{@link jtwod.engine.EntityController EntityController}</code>
     * type to cast the result to.
     * @return
     * The <code>{@link jtwod.engine.EntityController EntityController}</code>
     * attached to this <code>{@link jtwod.engine.Scene Scene}</code> as the
     * specified type.
     */
    public final <
        Controller extends EntityController<ParentEngine>
    > Controller getEntityController(Class<Controller> asType) {
        return asType.cast(this.getEntityController());
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
     * Update the rendering for this <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @param shouldRender The value.
     */
    public final void setShouldRender(boolean shouldRender)
    {
        this.isRendering = shouldRender;
    }

    /**
     * Returns the current render status for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @return Whether or not we should be rendering.
     */
    public final boolean shouldRender()
    {
        return this.isRendering;
    }

    /**
     * Update the TPS limit for this <code>{@link jtwod.engine.Scene Scene}</code>.
     * Note: FPS Limit should be a multiple of the TPS limit or else movement updates
     *       will be less smooth.
     *
     * @param tpsLimit The new limit.
     */
    public final void setTpsLimit(double tpsLimit)
    {
        this.tpsLimit = tpsLimit;
    }

    /**
     * Update the FPS limit for this <code>{@link jtwod.engine.Scene Scene}</code>.
     * Note: FPS Limit should be a multiple of the TPS limit or else movement updates
     *       will be less smooth.
     *
     * @param fpsLimit The new limit.
     */
    public final void setFpsLimit(double fpsLimit)
    {
        this.fpsLimit = fpsLimit;
    }

    /**
     * Updates the display of the debug information in this
     *
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     * @param render Whether or not we should render the debug information.
     */
    public final void setShouldRenderDebug(boolean render)
    {
        this.tpsRenderer.setVisible(render);
        this.fpsRenderer.setVisible(render);
        this.graphRenderer.setVisible(render);
        this.fpsColorBlock.setVisible(render);
        this.tpsColorBlock.setVisible(render);
    }

    /**
     * Retrieve the currently active debug visibility for this
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @return The Visibility.
     */
    public final boolean shouldRenderDebug()
    {
        return this.fpsRenderer.isVisible() && this.tpsRenderer.isVisible();
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

        if (this.isRendering) {
            this.drawableGroup.render(graphics, this);

            // Entities will always be rendered on top.
            if (this.controller != null) {
                this.controller.render(graphics, this);
            }
        } else {
            this.background.render(graphics, this);
            this.fpsRenderer.render(graphics, this);
            this.tpsRenderer.render(graphics, this);
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

        this.tpsRenderer.setText("TPS: " + this.getTps());
        this.fpsRenderer.setText("FPS: " + this.getFps());

        if (this.controller != null) {
            this.controller.update();
        }

        this.update();
    }
}
