package jtwod.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import jtwod.engine.graphics.TextureGroup;
import jtwod.engine.metrics.Dimensions;

import kuusisto.tinysound.TinySound;

/**
 * Class used to represent the very base of a game.
 */
public abstract class Engine {

    /**
     * If set to false, sounds played using Sound will not be heard.
     */
    private boolean enableSounds = true;

    /**
     * The global drawables that will be rendered on every window.
     *
     * Note: This can be changed by setting `renderer.shouldRenderWhenGlobal` to false
     *       on a per-Drawable basis. (See Drawable).
     */
    private LinkedList<Drawable<? extends Engine>> globalDrawables = new LinkedList<>();

    /**
     * The TextureGroup object that this engine was initialized with.
     */
    private TextureGroup textureGroup;

    /**
     * The title for the primary window.
     */
    private String windowTitle;

    /**
     * The size of the main application window.
     */
    private Dimensions windowSize;

    /**
     * The URL for the Application windows Icon.
     */
    private String iconUrl;

    /**
     * The main frame for the Application window.
     */
    private JFrame windowFrame;

    /**
     * The current screen being displayed.
     */
    private Scene<? extends Engine> currentScreen;

    /**
     * Initialize a new Engine.
     *
     * @param title The title for the Application window.
     * @param size The window size
     * @param iconUrl The icon file for the Application window.
     */
    public Engine(String title, Dimensions size, String iconUrl)
    {
        this.windowTitle = title;
        this.windowSize = size;
        this.iconUrl = iconUrl;
    }

    /**
     * Initialize a new Engine.
     *
     * @param title The title for the Application window.
     * @param size The window size
     */
    public Engine(String title, Dimensions size)
    {
        this.windowTitle = title;
        this.windowSize = size;
    }

    /**
     * Initialize a new Engine.
     *
     * @param title The title for the Application window.
     */
    public Engine(String title)
    {
        this.windowTitle = title;
    }

    /**
     * Initialize a new Engine.
     */
    public Engine()
    {
        // Empty initializer.
    }

    /**
     * Start the engine.
     */
    public final void start()
    {
        this.prime();
        this.onEngineStart();
    }

    /**
     * Primes the Engine by creating the main Application window
     * and setting the Global defaults.
     */
    private void prime()
    {
        windowFrame = new JFrame(windowTitle);

        this.setWindowTitle(this.windowTitle);

        windowFrame.setBackground(Color.BLACK);
        windowFrame.setSize(this.getWindowSize().getWidth(), this.getWindowSize().getHeight());
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setResizable(false);
        windowFrame.setLocationRelativeTo(null);

        if (iconUrl != null) {
            windowFrame.setIconImage(new ImageIcon(getClass().getResource(iconUrl)).getImage());
        }

        windowFrame.setVisible(true);

        /** Tiny Sound by finnkuusisto - https://github.com/finnkuusisto/TinySound */
        TinySound.init();

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        windowFrame.getContentPane().setCursor(blankCursor);

        this.textureGroup = new TextureGroup();
        this.loadTextures();
    }

    /**
     * Load the textures for this Engine.
     */
    public void loadTextures()
    {
        // Not implemented by default.
    }

    /**
     * Called after the engine has been primed.
     */
    public abstract void onEngineStart();

    /**
     * Change the currently active screen.
     *
     * @param screen
     */
    public final void setScreen(Scene<? extends Engine> screen)
    {
        if (currentScreen != null) {
            try {
                currentScreen.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            windowFrame.remove(currentScreen);
        }

        currentScreen = screen;
        currentScreen.setPreferredSize(this.getWindowSize().asAwtDimension());
        currentScreen.setMaximumSize(this.getWindowSize().asAwtDimension());
        currentScreen.setMinimumSize(this.getWindowSize().asAwtDimension());
        windowFrame.add(currentScreen);
        windowFrame.pack();
        currentScreen.start();
    }

    /**
     * Mute the sounds for this engine.
     */
    public final void muteSounds(){
        this.enableSounds = false;
    }

    /**
     * Unmute the sounds for this engine.
     */
    public final void unmuteSounds(){
        this.enableSounds = true;
    }

    /**
     * Check if the sounds are muted.
     *
     * @return
     */
    public final boolean isMuted()
    {
        return ! this.enableSounds;
    }

    /**
     * Update the title of the main Application window.
     *
     * @param title The new title.
     */
    public final void setWindowTitle(String title)
    {
        this.windowFrame.setTitle(title);
    }

    /**
     * Retrieve the currently active window title.
     *
     * @return
     */
    public final String getWindowTitle()
    {
        return windowTitle;
    }

    /**
     * Retrieve the currently active window size.
     *
     * @return The size of the Main Application Window.
     */
    public final Dimensions getWindowSize()
    {
        return this.windowSize;
    }

    /**
     * Retrieve the currently active global drawables.
     *
     * @return The Global Drawables associated with this Engine.
     */
    public final LinkedList<Drawable<? extends Engine>> getGlobalDrawables()
    {
    		return this.globalDrawables;
    }

    /**
     * Retrieve the TextureGroup.
     *
     * @return The TextureGroup associated with this Engine.
     */
    public final TextureGroup getTextureGroup()
    {
        return this.textureGroup;
    }

    /**
     * Add a global Drawable.
     *
     * @param drawable The Drawable to add.
     */
    public final void addGlobalDrawable(Drawable<? extends Engine> drawable)
    {
        this.globalDrawables.add(drawable);
    }

    /**
     * Remove a global Drawable.
     *
     * @param drawable The Drawable to remove.
     */
    public final void removeGlobalDrawable(Drawable<? extends Engine> drawable)
    {
        this.globalDrawables.remove(drawable);
    }

    /**
     * Remove a global Drawable based on it's index.
     *
     * @param index The index of the Drawable to remove.
     */
    public final void removeGlobalDrawable(int index)
    {
        this.globalDrawables.remove(index);
    }

    public final void setFullScreen(boolean fullScreen)
    {
        //DisplayMode dm = new DisplayMode(this.getWindowSize().getWidth(), this.getWindowSize().getHeight(), 32, DisplayMode.REFRESH_RATE_UNKNOWN);
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setDisplayMode(dm);
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this.windowFrame);
    }
}
