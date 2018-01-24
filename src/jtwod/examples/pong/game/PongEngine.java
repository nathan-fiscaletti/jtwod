package jtwod.examples.pong.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import jtwod.engine.Drawable;
import jtwod.engine.Engine;
import jtwod.engine.EntityController;
import jtwod.engine.Scene;
import jtwod.engine.drawable.Image;
import jtwod.engine.drawable.Text;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.AspectRatio;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

public class PongEngine extends Engine {

    
    // Configuration
    public final static int startingBallSpeed = 15;    
    public final static int paddleToWallPadding = 25;
    public final static int ballToPaddlePadding = 10;
    public final static int paddleSpeed = 10;
    public final static Dimensions paddleDimensions = new Dimensions(20, 75);
    public final static Dimensions ballDimensions = new Dimensions(20, 20);
    
    // Entities
    public static Paddle paddle1;
    public static Paddle paddle2;
    public static Ball ball;
    
    // Custom Drawables
    public static Text<PongEngine> logoText;
    public static Text<PongEngine> instructionsText;
    public static Text<PongEngine> scoreText;
    public static Text<PongEngine> roundText;
    public static Text<PongEngine> worthText;
    
    public static int round = 1;
    
    public PongEngine()
    {
        super(
            "Pong",
            new Dimensions (
                new AspectRatio(
                    new Dimensions(12, 9),
                    AspectRatio.AspectRatioControlAxis.WidthControlled
                )
            ).setWidth(800)
        );
    }
    
    @Override
    public final void loadTextures()
    {
        this.getTextureGroup().addTexture("Ball", Texture.whiteTexture(ballDimensions));
        this.getTextureGroup().addTexture("Paddle", Texture.whiteTexture(paddleDimensions));
    }
    
    @Override
    public void onEngineStart() {
        Scene<PongEngine> mainScreen = new Scene<PongEngine>("Pong", this) {
            /**
             * Serial Version UID.
             */
            private static final long serialVersionUID = -3405319262667259702L;
            
            @Override
            public final void initialize()
            {
                this.setController(new EntityController<PongEngine>(this) {
                    {
                        paddle1 = new Paddle(new Vector(paddleToWallPadding, 0), this.getParentScreen(), 1);
                        paddle2 = new Paddle(
                                Vector.Max(this.getParentEngine())
                                    .plusX(-(paddleDimensions.getWidth() + paddleToWallPadding))
                                    .plusY(-paddleDimensions.getHeight()), 
                                this.getParentScreen(),
                                2
                        );
                        
                        ball = new Ball(Vector.Zero(), this.getParentScreen());
                        ball.startOnPaddle(paddle1);
                        
                        this.spawnEntity(ball);
                        this.spawnEntity(paddle1);
                        this.spawnEntity(paddle2);
                        
                        // Add the logo to the drawables.
                        logoText = new Text<PongEngine>(
                            "Dropshot Pong",
                            new Font("Systen", Font.BOLD, 48),
                            Color.yellow,
                            Drawable.Center.Horizontally,
                            Vector.Zero().plusY(80),
                            this.getParentEngine()
                        );
                        
                        instructionsText = new Text<PongEngine>(
                            "Press 'Space' to begin!",
                            new Font("Systen", Font.BOLD, 18),
                            Color.gray,
                            Drawable.Center.Horizontally,
                            Vector.Zero().plusY(112),
                            this.getParentEngine()
                        );
                        
                        scoreText = new Text<PongEngine>(
                            "0 - 0",
                            new Font("Systen", Font.BOLD, 24),
                            Color.white,
                            Drawable.Center.Horizontally,
                            Vector.Zero().plusY(150),
                            this.getParentEngine()
                        );
                        
                        roundText = new Text<PongEngine>(
                            "Round " + PongEngine.round,
                            new Font("Systen", Font.BOLD, 16),
                            Color.white,
                            Drawable.Center.Horizontally,
                            Vector.Zero().plusY(180),
                            this.getParentEngine()
                        );
                        
                        worthText = new Text<PongEngine>(
                            "Round Worth 0",
                            new Font("Systen", Font.BOLD, 14),
                            Color.white,
                            Drawable.Center.Horizontally,
                            Vector.Zero().plusY(210),
                            this.getParentEngine()
                        );
                        
                        this.getParentEngine().addGlobalDrawable(logoText);
                        this.getParentEngine().addGlobalDrawable(instructionsText);
                        this.getParentEngine().addGlobalDrawable(scoreText);
                        this.getParentEngine().addGlobalDrawable(roundText);
                        this.getParentEngine().addGlobalDrawable(worthText);
                    }
                });
            }
            
            @Override
            public void update()
            {
                if (! ball.isStarted()) {
                    logoText.setShouldRenderWhenGlobal(true);
                    instructionsText.setShouldRenderWhenGlobal(true);
                    worthText.setShouldRenderWhenGlobal(false);
                    if ( ball.currentPaddle == 1) {
                        ball.setToSpawnLocation(paddle1);
                    } else {
                        ball.setToSpawnLocation(paddle2);
                    }
                } else {
                    logoText.setShouldRenderWhenGlobal(false);
                    instructionsText.setShouldRenderWhenGlobal(false);
                    worthText.setShouldRenderWhenGlobal(true);
                }
                
                scoreText.setText(paddle1.score + " - " + paddle2.score);
                roundText.setText("Round " + round);
                worthText.setText("Round Worth " + ball.worth);
            }
            
            /**
             * Handle user input. Here we just allow you to move the player around.
             * So, we update the players velocity based on the keys you press.
             *
             * @param keyEvent The key event.
             */
            @Override
            protected final void onKeyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    paddle2.setVelocity(Vector.Zero().setY(paddleSpeed));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    paddle2.setVelocity(Vector.Zero().setY(-paddleSpeed));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
                    paddle1.setVelocity(Vector.Zero().setY(-paddleSpeed));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
                    paddle1.setVelocity(Vector.Zero().setY(paddleSpeed));
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (! ball.isStarted()) {
                        ball.setStarted(true);
                    }
                }
            }

            /**
             * Here we return the player velocity to 0 since you are no longer hitting the key.
             *
             * @param keyEvent The key event.
             */
            @Override
            protected final void onKeyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    paddle2.setVelocity(Vector.Zero());
                    if (ball.currentPaddle == 1) {
                        if (! ball.isStarted()) {
                            ball.setVelocity(Vector.Zero());
                        }
                    }
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    paddle2.setVelocity(Vector.Zero());
                    if (ball.currentPaddle == 1) {
                        if (! ball.isStarted()) {
                            ball.setVelocity(Vector.Zero());
                        }
                    }
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
                    paddle1.setVelocity(Vector.Zero());
                    if (ball.currentPaddle == 2) {
                        if (! ball.isStarted()) {
                            ball.setVelocity(Vector.Zero());
                        }
                    }
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
                    paddle1.setVelocity(Vector.Zero());
                    if (ball.currentPaddle == 2) {
                        if (! ball.isStarted()) {
                            ball.setVelocity(Vector.Zero());
                        }
                    }
                }
            }
        };
        
        /**
         * Let's add our background to the global Drawables
         * instead of rendering it directly from the screen.
         * 
         * This will add a black background to all screens.
         */
        this.addGlobalDrawable(new Drawable<PongEngine>(this) {
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
            protected final void render(Graphics graphics, Scene<PongEngine> screen) {
                // Create the background.
                Image<PongEngine> image = new Image<PongEngine>(
                    Texture.blackTexture(
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
        
        this.setScreen(mainScreen);
    }
    
}
