package jtwod.examples.testgame;

import jtwod.engine.Drawable;
import jtwod.engine.Engine;
import jtwod.engine.EntityController;
import jtwod.engine.Scene;

import jtwod.engine.drawable.Entity;
import jtwod.engine.drawable.Image;
import jtwod.engine.drawable.Text;

import jtwod.engine.graphics.Texture;

import jtwod.engine.metrics.AspectRatio;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.KeyEvent;

public final class TestGameEngine extends Engine {

    /**
     * Define the player entity.
     */
    private Entity<TestGameEngine> myPlayer;

    /**
     * Initialize the new GameEngine with a title, an icon, and textures.
     * Since we aren't using any textures, i left that null. We just use
     * Textures.whiteTexture(size); for our player sprite.
     */
    public TestGameEngine() {
        // Set the title to "My Game"
        // Set the bounds to 800 width with a 12x9 aspect ratio.
        super(
            "My Game",
            new Dimensions (
                new AspectRatio(
                     new Dimensions(12, 9),
                     AspectRatio.AspectRatioControlAxis.WidthControlled
                )
            ).setWidth(800)
        );
    }
    
    /*
     * Load the textures into the system.
     *
     * @see jtwod.engine.Engine#loadTextures()
     */
    @Override
    public final void loadTextures()
    {
        /*
         * Load the Player texture into the game. 
         * We just use the built in "Unknown Texture".
         */
        this.getTextureGroup().addTexture("Player", Texture.unknownTexture(new Dimensions(32, 32)));
    }

    /*
     * This function is called after the engine has been primed.
     * At this point, the main window should be open and active.
     * 
     * @see jtwod.engine.Engine#onEngineStart()
     */
    @Override
    public final void onEngineStart() {
        /*
         * Initialize the new Screen that we will be using to render things out.
         */
        Scene<TestGameEngine> mainScene = new Scene<TestGameEngine>("Main Screen", this) {

            /**
             * The Serial version UID.
             */
            private static final long serialVersionUID = -1982332528698274277L;

            /*
             * Override this function to handle the Priming of the Screen.
             * 
             * @see jtwod.engine.Screen#prime()
             */
            @Override
            protected final void prepare()
            {
                /*
                 * Assign a new EntityController to the screen.
                 * When the entity controller is initialized, we spawn our player onto it.
                 */
                this.setController(new EntityController<TestGameEngine>(this) {
                    {
                        /*
                         * Initialize the player entity.
                         *
                         * This is a very simple entity, just a rendered sprite of a white block
                         * for the entity sprite, and then we constrain the entities position
                         * to the bounds of the screen so that they cannot leave it.
                         */
                        myPlayer = new Entity<TestGameEngine>(Vector.Zero(), this.getParentScreen()) {
                            {
                                // Set the sprite for the entity to a white 10x10 image.
                                this.setRenderedTexture(
                                    this.getParentEngine().getTextureGroup().getTexture("Player")
                                );

                                // Constrain the entity to the screen bounds.
                                this.setPositionConstraint(
                                    Vector.Max(
                                        this.getParentEngine()
                                    ).plusX(
                                        -this.getSize().getWidth()
                                    ).plusY(
                                        -this.getSize().getHeight()
                                    )
                                );
                            }
                        };

                        /*
                         * Spawn the player.
                         */
                        this.spawnEntity(myPlayer);
                    }
                });
            }

            /**
             * Render out a frame to the Screen.
             * Here we just draw out a blank black background.
             *
             * Our entity will be automatically rendered by the EntityController
             * attached to this screen.
             *
             * @param graphics The graphics object to use for rendering.
             */
            @Override
            protected final void renderFrame(Graphics graphics) {
                // Create the Text.
                Text<TestGameEngine> text = new Text<TestGameEngine>(
                    "Use the arrow keys to move around!",
                    new Font("Ariel", Font.BOLD, 24),
                    Color.white,
                    Drawable.Center.Horizontally,
                    Vector.Zero().plusY(130),
                    this.getParentEngine()
                );

                // Render the text out.
                text.render(graphics, this);
            }

            /**
             * Handle user input. Here we just allow you to move the player around.
             * So, we update the players velocity based on the keys you press.
             *
             * @param keyEvent The key event.
             */
            @Override
            protected final void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setY(3));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setY(-3));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setX(-3));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setX(3));
                }
            }

            /**
             * Here we return the player velocity to 0 since you are no longer hitting the key.
             *
             * @param keyEvent The key event.
             */
            @Override
            protected final void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setY(0));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setY(0));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setX(0));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    myPlayer.setVelocity(myPlayer.getVelocity().setX(0));
                }
            }
        };
        
        /**
         * Let's add our background to the global Drawables
         * instead of rendering it directly from the screen.
         * 
         * This will add a black background to all screens.
         */
        this.addGlobalDrawable(new Drawable<TestGameEngine>(this) {
            /*
             * Make sure that this Drawable is not topmost.
             */
            {
                this.setTopMost(false);
            }
            
            /*
             * Render the graphics for the Drawable out.
             *
             * @see jtwod.engine.Drawable#render(java.awt.Graphics, jtwod.engine.Screen)
             */
            @Override
            protected final void render(Graphics graphics, Scene<TestGameEngine> screen) {
                // Create the background.
                Image<TestGameEngine> image = new Image<TestGameEngine>(
                    Texture.colorTexture(Color.black,
                        new Dimensions(
                            (int)this.getParentEngine().getWindowSize().getWidth(),
                            (int)this.getParentEngine().getWindowSize().getHeight()
                        )
                    ),
                    Vector.Zero(),
                    this.getParentEngine()
                );
                
                // Render it out
                image.render(graphics, screen);
            }

            @Override
            protected final void update() {
                // Not implemented.
            }
        });

        /*
         * Set the main screen for the engine to get things rolling.
         */
        this.setScene(mainScene);
        this.setFullScreen(true);
    }
}
