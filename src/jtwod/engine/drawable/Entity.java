package jtwod.engine.drawable;

import java.awt.*;
import java.util.Random;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

/**
 * Parent class for all Entities
 * that get rendered to the screen.
 *
 * @author Nathan Fiscaletti
 */
public abstract class Entity<ParentEngine extends Engine> extends Shape<ParentEngine>
{
    /**
     * The velocity of the entity.
     */
    private Vector velocity;

    /**
     * The Game that the entity is attached to.
     */
    private Scene<ParentEngine> parentScreen;

    /**
     * The rendered Texture for the entity.
     */
    private Texture renderedTexture;

    /**
     * Kill the entity after this many seconds.
     */
    private int shouldKillAfter = 0;

    /**
     * If set to true, the death animation will be played when this entity is killed.
     */
    private boolean shouldPlayDeathAnimation = false;

    /**
     * Used to manage the death animation.
     */
    private int deathTick = 0;

    /**
     * The number of ticks that the entity has been alive.
     */
    private int lifeLived = 0;

    /**
     * If set to true, the entity will die.
     */
    private boolean isDead = false;

    /**
     * Some entities make use of a random
     * value. We provide them the means
     * to access that through this
     * variable definition.
     */
    protected Random random;

    /**
     * Create a new instance of the Entity Class.
     *
     * @param position The initial position.
     * @param screen The screen to attach the entity to.
     */
    public Entity(Vector position, Scene<ParentEngine> screen)
    {
        super(screen.getParentEngine());

        this.setPosition(position);
        this.velocity = Vector.Zero();
        this.setPositionConstraint(Vector.Zero());
        this.parentScreen = screen;
        this.random = new Random();
    }

    /**
     * Called when the entity is killed.
     */
    public void onDeath()
    {
        // Not implemented by default.
    }

    /**
     * Called when the entity wants to update it's sprite.
     */
    public void updateSprite()
    {
        // Not implemented by default.
    }

    /**
     * Called when an entity collides with another.
     *
     * @param collidedWith
     */
    public void onCollide(Entity<ParentEngine> collidedWith)
    {
        // Not implemented by default.
    }

    /**
     * Called when an update is performed.
     */
    @Override
    protected void update()
    {
        // Not implemented by default.
    }

    /**
     * Called when an update tick occurs.
     */
    @Override
    public void performUpdate()
    {
        // Update velocity
        if (! this.velocity.isZero()) {
            this.move(velocity);
        }

        // Perform the heart beat
        this.update();

        // Update the constraints
        this.updateConstraints();

        // Update the sprite for the entity.
        this.updateSprite();

        // Check the remaining life of the entity
        // and kill it if necessary.
        this.checkLife();

        // Check if the entity should be killed regardless
        this.runKill();
    }

    /**
     * Called when this entity leaves the render bounds.
     */
    public void onExitBounds()
    {
        this.kill();
    }

    /**
     * Render the entities sprite out to the screen.
     *
     * @param g The graphics object to use for rendering.
     * @param screen The screen that we are rendering out to.
     */
    @Override
    public final void render(Graphics g, Scene<ParentEngine> screen)
    {
        g.drawImage(this.getRenderedTexture().asBufferedImage(), this.getPosition().getX(), this.getPosition().getY(), screen);
    }

    /**
     * Update the Texture and the bounds.
     *
     * @param sprite
     */
    public final void setRenderedTexture(Texture texture)
    {
        this.renderedTexture = texture;
        this.setSize(new Dimensions(texture.getWidth(), texture.getHeight()));
    }

    /**
     * Retrieve the Texture.
     *
     * @return
     */
    public final Texture getRenderedTexture()
    {
        return (this.renderedTexture != null)
            ? this.renderedTexture
            : Texture.unknownTexture(new Dimensions(32,32));
    }

    /**
     * Tell the entity that it should die.
     */
    public final void kill()
    {
        this.isDead = true;
        this.velocity = Vector.Zero();
    }

    /**
     * Tell this entity to die after X ticks from being spawned.
     * @param ticks
     */
    public final void killAfter(int ticks)
    {
        this.shouldKillAfter = ticks;
    }

    /**
     * Set the flag for playing the death animation.
     *
     * @param play
     */
    public final void setShouldPlayDeathAnimation(boolean play)
    {
        this.shouldPlayDeathAnimation = play;
    }

    /**
     * Set the Velocity.
     *
     * @param velocity
     */
    public void setVelocity(Vector velocity)
    {
        this.velocity = velocity;
    }

    /**
     * Retrieve the current velocity.
     *
     * @return
     */
    public Vector getVelocity()
    {
        return this.velocity;
    }

    /**
     * Get the parent screen.
     *
     * @return
     */
    public Scene<ParentEngine> getParentScreen()
    {
        return this.parentScreen;
    }

    /**
     * Check if the entity is dead.
     */
    public final boolean isDead()
    {
        return this.isDead;
    }

    /**
     * Check if we need to die based on our life lived.
     */
    private void checkLife()
    {
        this.lifeLived++;
        if (this.shouldKillAfter > 0) {
            if (this.lifeLived > this.shouldKillAfter) {
                this.kill();
            }
        }
    }

    /**
     * Kill the entity.
     */
    private void runKill()
    {
        if (this.isDead) {
            if (this.shouldPlayDeathAnimation) {
                if(deathTick < 5){
                    this.renderedTexture = this.getParentEngine().getTextureGroup().getTexture("des1");
                    deathTick ++;
                }else if(deathTick >= 5 && deathTick < 10){
                    this.renderedTexture = this.getParentEngine().getTextureGroup().getTexture("des2");
                    deathTick ++;
                }else if(deathTick >= 10 && deathTick < 15){
                    this.renderedTexture = this.getParentEngine().getTextureGroup().getTexture("des3");
                    deathTick ++;
                }else if(deathTick == 15){
                    this.onDeath();
                    this.parentScreen.getController().deSpawnEntity(this);
                }
            } else {
                this.onDeath();
                this.parentScreen.getController().deSpawnEntity(this);
            }
        }
    }

}
