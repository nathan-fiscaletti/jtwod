package jtwod.engine.drawable;

import java.awt.*;
import java.util.Random;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

/**
 * Parent class for all Entities.
 * 
 * @param <ParentEngine> The ParentEngine type for this Entity.
 */
public abstract class Entity<ParentEngine extends Engine> extends Shape<ParentEngine>
{
    /**
     * The velocity of the Entity.
     */
    private Vector velocity;

    /**
     * The Game that the Entity is attached to.
     */
    private Scene<ParentEngine> parentScene;

    /**
     * The rendered Texture for the Entity.
     */
    private Texture renderedTexture;

    /**
     * Kill the Entity after this many seconds.
     */
    private int shouldKillAfter = 0;

    /**
     * If set to true, the death animation will be played when this Entity is killed.
     */
    private boolean shouldPlayDeathAnimation = false;

    /**
     * Used to manage the death animation for this Entity.
     */
    private int deathTick = 0;

    /**
     * The number of ticks that the Entity has been alive.
     */
    private int lifeLived = 0;

    /**
     * If set to true, the Entity will die.
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
     * Create a new instance of an Entity.
     *
     * @param position The initial position.
     * @param screen The Scene to attach the entity to.
     */
    public Entity(Vector position, Scene<ParentEngine> scene)
    {
        super(scene.getParentEngine());

        this.setPosition(position);
        this.velocity = Vector.Zero();
        this.setPositionConstraint(Vector.Zero());
        this.parentScene = scene;
        this.random = new Random();
    }

    /**
     * Called when the Entity is killed.
     */
    public void onDeath()
    {
        // Not implemented by default.
    }

    /**
     * Called when the Entity wants to update it's sprite.
     */
    public void updateSprite()
    {
        // Not implemented by default.
    }

    /**
     * Called when an entity collides with another Entity.
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
     * @param graphics The graphics object to use for rendering.
     * @param scene The Scene that we are rendering out to.
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        graphics.drawImage(this.getRenderedTexture().asBufferedImage(), this.getPosition().getX(), this.getPosition().getY(), scene);
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
     * Retrieve the Texture that is currently being rendered out for this Entity.
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
     * Tell the Entity that it should die and set it's Velocity to 0.
     */
    public final void kill()
    {
        this.isDead = true;
        this.velocity = Vector.Zero();
    }

    /**
     * Tell this Entity to die after X ticks from being spawned.
     *
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
     * Set the Velocity for the Entity.
     *
     * @param velocity
     */
    public void setVelocity(Vector velocity)
    {
        this.velocity = velocity;
    }

    /**
     * Retrieve the current velocity for the Entity.
     *
     * @return
     */
    public Vector getVelocity()
    {
        return this.velocity;
    }

    /**
     * Get the parent Scene.
     *
     * @return
     */
    public Scene<ParentEngine> getParentScene()
    {
        return this.parentScene;
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
                    this.parentScene.getController().deSpawnEntity(this);
                }
            } else {
                this.onDeath();
                this.parentScene.getController().deSpawnEntity(this);
            }
        }
    }

}
