package jtwod.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import jtwod.engine.graphics.TextureGroup;
import jtwod.engine.metrics.Dimensions;

import kuusisto.tinysound.TinySound;

/**
 * Used to represent the very base of a game in jtwod.
 * 
 * @see Engine#onEngineStart() 
 * @see Engine#loadTextures() 
 * @see Engine#setScene(jtwod.engine.Scene) 
 * @see Engine#getTextureGroup() 
 */
public abstract class Engine
{
    /**
     * <code>{@link java.util.Random Random} utility object.
     */
    private final Random random;
    
    /**
     * If set to false, sounds played using
     * <code>{@link jtwod.engine.sound.Sound Sound}</code> will not be heard.
     */
    private boolean enableSounds = true;

    /**
     * The <code>{@link jtwod.engine.TextureGroup TextureGroup}</code> object
     * that this engine was initialized with.
     */
    private TextureGroup textureGroup;

    /**
     * The title for the primary Application Window.
     */
    private String windowTitle;

    /**
     * The size of the primary Application Window.
     */
    private Dimensions windowSize;

    /**
     * The URL for the primary Application Windows Icon.
     */
    private String iconUrl;

    /**
     * The main <code>{@link javax.swing.JFrame JFrame}</code> for the 
     * primary Application window.
     */
    private JFrame windowFrame;

    /**
     * The current <code>{@link jtwod.engine.Scene Scene}</code>
     * being displayed.
     */
    private Scene<? extends Engine> currentScene;

    /**
     * Initialize a new <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @param title The title for the primary Application window.
     * @param size The window size for the primary Application Window.
     * @param iconUrl The icon URL for the primary Application window.
     */
    public Engine(String title, Dimensions size, String iconUrl)
    {
        this.windowTitle = title;
        this.windowSize = size;
        this.iconUrl = iconUrl;
        this.random = new Random();
    }

    /**
     * Initialize a new <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @param title The title for the primary Application window.
     * @param size The window size for the primary Application Window.
     */
    public Engine(String title, Dimensions size)
    {
        this.windowTitle = title;
        this.windowSize = size;
        this.random = new Random();
    }

    /**
     * Initialize a new <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @param title The title for the primary Application window.
     */
    public Engine(String title)
    {
        this.windowTitle = title;
        this.random = new Random();
    }

    /**
     * Initialize a new <code>{@link jtwod.engine.Engine Engine}</code>.
     */
    public Engine()
    {
        this.random = new Random();
    }

    /**
     * Start the <code>{@link jtwod.engine.Engine Engine}</code>.
     */
    public final void start()
    {
        this.prime();
        this.onEngineStart();
    }

    /**
     * Primes the <code>{@link jtwod.engine.Engine Engine}</code> by creating 
     * the primary Application window and setting the Global defaults.
     */
    private void prime()
    {
        windowFrame = new JFrame(windowTitle);

        this.setWindowTitle(this.windowTitle);

        windowFrame.setBackground(Color.BLACK);
        windowFrame.setSize(
            this.getWindowSize().getWidth(), 
            this.getWindowSize().getHeight()
        );
        
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setResizable(false);
        windowFrame.setLocationRelativeTo(null);

        if (iconUrl != null) {
            windowFrame.setIconImage(
                new ImageIcon(getClass().getResource(iconUrl)).getImage()
            );
        }

        windowFrame.setVisible(true);

        /** Tiny Sound - https://github.com/finnkuusisto/TinySound */
        TinySound.init();

        BufferedImage cursorImg = new BufferedImage(
            16, 16, BufferedImage.TYPE_INT_ARGB
        );
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor"
        );
        windowFrame.getContentPane().setCursor(blankCursor);

        this.textureGroup = new TextureGroup();
        this.loadTextures();
    }

    /**
     * Override this function to load the 
     * <code>{@link jtwod.engine.graphics.TextureGroup TextureGroup}</code> for
     * your <code>{@link jtwod.engine.Engine Engine}</code> implementation.
     */
    public void loadTextures()
    {
        // Not implemented by default.
    }

    /**
     * Override this function to control what your
     * <code>{@link jtwod.engine.Engine Engine}</code> does once it
     * has been primed.
     */
    public abstract void onEngineStart();

    /**
     * Update the currently active
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @param scene The <code>{@link jtwod.engine.Scene Scene}</code>. 
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public final void setScene(Scene<? extends Engine> scene)
    {
        if (currentScene != null) {
            try {
                currentScene.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            windowFrame.remove(currentScene);
        }

        currentScene = scene;
        currentScene.setPreferredSize(this.getWindowSize().asAwtDimension());
        currentScene.setMaximumSize(this.getWindowSize().asAwtDimension());
        currentScene.setMinimumSize(this.getWindowSize().asAwtDimension());
        windowFrame.add(currentScene);
        windowFrame.pack();
        currentScene.start();
    }

    /**
     * Mute the sounds for this <code>{@link jtwod.engine.Engine Engine}</code>.
     */
    public final void muteSounds()
    {
        this.enableSounds = false;
    }

    /**
     * Un-mute the sounds for this
     * <code>{@link jtwod.engine.Engine Engine}</code>.
     */
    public final void unmuteSounds()
    {
        this.enableSounds = true;
    }

    /**
     * Check if the sounds are muted for this
     * <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @return True if the sounds are muted.
     */
    public final boolean isMuted()
    {
        return ! this.enableSounds;
    }

    /**
     * Update the title of the primary Application window.
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
     * @return The title of the primary Application Window.
     */
    public final String getWindowTitle()
    {
        return windowTitle;
    }

    /**
     * Retrieve the currently active window
     * <code>{@link jtwod.engine.metrics.Dimensions Dimensions}</code>.
     *
     * @return
     * The <code>{@link jtwod.engine.metrics.Dimensions Dimensions}</code> of
     * the primary Application Window.
     */
    public final Dimensions getWindowSize()
    {
        return new Dimensions(this.windowSize.getWidth(), this.windowSize.getHeight());
    }

    /**
     * Retrieve the
     * <code>{@link jtwod.engine.graphics.TextureGroup TextureGroup}</code>
     * associated with this <code>{@link jtwod.engine.Engine Engine}</code>.
     *
     * @return The TextureGroup associated with this 
     *         <code>{@link jtwod.engine.Engine Engine}</code>.
     */
    public final TextureGroup getTextureGroup()
    {
        return this.textureGroup;
    }
    
    /**
     * Retrieves the currently active 
     * <code>{@link java.util.Random Random}</code> utility object.
     *
     * @return The <code>{@link java.util.Random Random}</code>.
     */
    public final Random getRandom()
    {
        return this.random;
    }
}
