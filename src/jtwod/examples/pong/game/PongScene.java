package jtwod.examples.pong.game;

import java.awt.Color;
import java.awt.Font;

import jtwod.engine.Drawable;
import jtwod.engine.Scene;
import jtwod.engine.drawable.Text;
import jtwod.engine.metrics.Vector;

public class PongScene extends Scene<PongEngine> {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 4347149361762469174L;
    
    // Custom Drawables
    public Text<PongEngine> logoText;
    public Text<PongEngine> instructionsText;
    public Text<PongEngine> scoreText;
    public Text<PongEngine> roundText;
    public Text<PongEngine> worthText;
    
    public static PongBackground background;
    
    public PongScene(PongEngine engine) {
        super("Pong", engine);
    }

    @Override
    public final void allocate()
    {
        this.setEntityController(new PongEntityController(this));
        
        // Add the logo to the drawables.
        logoText = new Text<>(
            1,
            "Dropshot Pong",
            new Font("Systen", Font.BOLD, 48),
            Color.yellow,
            Drawable.Center.Horizontally,
            Vector.Zero().plusY(80),
            this.getParentEngine()
        );
        
        instructionsText = new Text<>(
            1,
            "Press 'Space' to begin!",
            new Font("Systen", Font.BOLD, 18),
            Color.gray,
            Drawable.Center.Horizontally,
            Vector.Zero().plusY(112),
            this.getParentEngine()
        );
        
        scoreText = new Text<>(
            1,
            "0 - 0",
            new Font("Systen", Font.BOLD, 24),
            Color.white,
            Drawable.Center.Horizontally,
            Vector.Zero().plusY(150),
            this.getParentEngine()
        );
        
        roundText = new Text<>(
            1,
            "Round " + PongEngine.round,
            new Font("Systen", Font.BOLD, 16),
            Color.white,
            Drawable.Center.Horizontally,
            Vector.Zero().plusY(180),
            this.getParentEngine()
        );
        
        worthText = new Text<>(
            1,
            "Round Worth 0",
            new Font("Systen", Font.BOLD, 14),
            Color.white,
            Drawable.Center.Horizontally,
            Vector.Zero().plusY(210),
            this.getParentEngine()
        );
        
        background = new PongBackground(this.getParentEngine());
        
        this.getDrawableGroup().addDrawable(logoText);
        this.getDrawableGroup().addDrawable(instructionsText);
        this.getDrawableGroup().addDrawable(scoreText);
        this.getDrawableGroup().addDrawable(background);
        this.getDrawableGroup().addDrawable(roundText);
        this.getDrawableGroup().addDrawable(worthText);
    }
    
    @Override
    public void update()
    {
        if (! PongEntityController.ball.isStarted()) {
            logoText.setVisible(true);
            instructionsText.setVisible(true);
            worthText.setVisible(false);
        } else {
            logoText.setVisible(false);
            instructionsText.setVisible(false);
            worthText.setVisible(true);
        }
        
        scoreText.setText(PongEntityController.paddle1.score + " - " + PongEntityController.paddle2.score);
        roundText.setText("Round " + PongEngine.round);
        worthText.setText("Round Worth " + PongEntityController.ball.worth);
    }
}
