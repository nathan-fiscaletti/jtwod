package jtwod.engine;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

/**
 * Represents a screen that can be rendered out to the window.
 *
 * @param <ParentEngine> The engine type that this screen is associated with.
 */
public abstract class Scene<ParentEngine extends Engine> extends Canvas implements Runnable
{
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1303916252996012557L;

    /**
     * The name of the Scene.
     */
    private String name;

    /**
     * The entity controller for controlling
     * any entities you might want to have
     * attached to this screen.
     */
    private EntityController<ParentEngine> controller;

    /**
     * The Ticks Per Second for this screen.
     */
    private int tps;

    /**
     * The Frames Per Second for this screen.
     */
    private int fps;

    /**
     * Primary thread control.
     */
    private boolean running = false;

    /**
     * Primary Thread.
     */
    private Thread thread;

    /**
     * The parent Engine for this screen.
     */
    private ParentEngine parentEngine;

    /**
     * Initialize the screen with a parent engine.
     *
     * @param engine
     */
    public Scene(String name, ParentEngine engine)
    {
        this.name = name;
        this.parentEngine = engine;
        this.controller = new EntityController<ParentEngine>(this) {};
    }

    /**
     * Initialize the screen with a parent engine and an EntityController.
     *
     * @param controller
     */
    public Scene(String name, ParentEngine engine, EntityController<ParentEngine> controller)
    {
        this.name = name;
        this.parentEngine = engine;
        this.controller = controller;
    }

    /**
     * Called to initialize properties.
     */
    protected void initialize() {
        // Not implemented by default.
    }

    /**
     * Called when a frame should be rendered out.
     *
     * @param graphics
     */
    protected void renderFrame(Graphics graphics)
    {
        // Not implemented by default.
    }

    /**
     * Called when an update to this screen should occure.
     */
    protected void update()
    {
        // Not implemented by default.
    }

    /**
     * Called when a key is pressed down.
     *
     * @param keyEvent
     */
    protected void onKeyPressed(KeyEvent keyEvent)
    {
        // Not implemented by default.
    }

    /**
     * Called when a key is released.
     *
     * @param keyEvent
     */
    protected void onKeyReleased(KeyEvent keyEvent)
    {
        // Not implemented by default.
    }

    /**
     * Primary run body for controlling screens.
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
        try {
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the screens primary thread.
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
     * Stop the screens primary thread.
     * @throws Exception
     */
    public final synchronized void stop()
	{
        this.running = false;
    }

    /**
     * Retrieve the name for the Screen.
     *
     * @return
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the controller for the screen.
     *
     * @return
     */
    public final EntityController<ParentEngine> getController()
    {
        return this.controller;
    }

    /**
     * Retrieve the EntityController as a specific type.
     *
     * @return
     */
    public final <ControllerType extends EntityController<? extends Engine>> ControllerType getController(Class<ControllerType> type) {
        return type.cast(this.controller);
    }

    /**
     * Assign a new EntityController to the screen.
     *
     * @param controller
     */
    public final void setController(EntityController<ParentEngine> controller)
    {
        this.controller = controller;
    }

    /**
     * Retrieve the Ticks Per Second for the screen.
     *
     * @return
     */
    public final int getTps()
    {
        return this.tps;
    }

    /**
     * Retrieve the Frames Per Second for the screen.
     *
     * @return
     */
    public final int getFps()
    {
        return this.fps;
    }

    /**
     * Retrieve the parent engine.
     *
     * @return
     */
    public ParentEngine getParentEngine()
    {
        return this.parentEngine;
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
                onKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e){
                onKeyReleased(e);
            }
        });
        this.initialize();
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

        for (Drawable<? extends Engine> drawable : parentEngine.getGlobalDrawables()) {
            if (drawable.shouldRenderWhenGlobal() && !drawable.isTopMost()) {
                drawable.render(graphics, null);
            }
        }

        this.renderFrame(graphics);

        if (this.controller != null) {
            this.controller.render(graphics, this);
        }

        for (Drawable<? extends Engine> drawable : parentEngine.getGlobalDrawables()) {
            if (drawable.shouldRenderWhenGlobal() && drawable.isTopMost()) {
                drawable.render(graphics, null);
            }
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
        for (Drawable<? extends Engine> drawable : parentEngine.getGlobalDrawables()) {
            if (drawable.shouldRenderWhenGlobal()) {
                drawable.update();
            }
        }

        if (this.controller != null) {
            this.controller.update();
        }

        this.update();
    }
}
