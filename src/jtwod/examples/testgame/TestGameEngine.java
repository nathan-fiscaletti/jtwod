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
             * @see jtwod.engine.Screen#allocate()
             */
            @Override
            protected final void allocate()
            {
                /*
                 * Assign a new EntityController to the screen.
                 * When the entity controller is initialized, we spawn our player onto it.
                 */
                this.setEntityController(new EntityController<TestGameEngine>(this) {
                    {
                        /*
                         * Initialize the player entity.
                         *
                         * This is a very simple entity, just a rendered sprite of a white block
                         * for the entity sprite, and then we constrain the entities position
                         * to the bounds of the screen so that they cannot leave it.
                         */
                        myPlayer = new Entity<TestGameEngine>(Vector.Zero(), this.getParentEngine().getTextureGroup().getTexture("Player"), this.getParentScene()) {
                            {
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
                
                
                // Add the background to the Scene.
                Image<TestGameEngine> background = new Image<>(0,
                        Texture.colorTexture(
                                Color.black, 
                                this.getParentEngine().getWindowSize()
                        ), 
                        Vector.Zero(), 
                        this.getParentEngine()
                );
                
                this.getDrawableGroup().addDrawable(background);

                // Add text to the scene.
                Text<TestGameEngine> text = new Text<>(
                    1,
                    "Use the arrow keys to move around!",
                    new Font("Ariel", Font.BOLD, 24),
                    Color.white,
                    Drawable.Center.Horizontally,
                    Vector.Zero().plusY(130),
                    this.getParentEngine()
                );
                
                this.getDrawableGroup().addDrawable(text);
            }

            /**
             * Handle user input. Here we just allow you to move the player around.
             * So, we update the players velocity based on the keys you press.
             *
             * @param keyEvent The key event.
             */
            @Override
            protected final void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        myPlayer.setVelocity(myPlayer.getVelocity().setY(3));
                        break;
                    case KeyEvent.VK_UP:
                        myPlayer.setVelocity(myPlayer.getVelocity().setY(-3));
                        break;
                    case KeyEvent.VK_LEFT:
                        myPlayer.setVelocity(myPlayer.getVelocity().setX(-3));
                        break;
                    case KeyEvent.VK_RIGHT:
                        myPlayer.setVelocity(myPlayer.getVelocity().setX(3));
                        break;
                    default:
                        break;
                }
            }

            /**
             * Here we return the player velocity to 0 since you are no longer hitting the key.
             *
             * @param keyEvent The key event.
             */
            @Override
            protected final void keyReleased(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        myPlayer.setVelocity(myPlayer.getVelocity().setY(0));
                        break;
                    case KeyEvent.VK_UP:
                        myPlayer.setVelocity(myPlayer.getVelocity().setY(0));
                        break;
                    case KeyEvent.VK_LEFT:
                        myPlayer.setVelocity(myPlayer.getVelocity().setX(0));
                        break;
                    case KeyEvent.VK_RIGHT:
                        myPlayer.setVelocity(myPlayer.getVelocity().setX(0));
                        break;
                    default:
                        break;
                }
            }
        };
        
        /*
         * Set the main screen for the engine to get things rolling.
         */
        this.setScene(mainScene);
    }
}
