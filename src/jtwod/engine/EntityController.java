package jtwod.engine;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import jtwod.engine.drawable.Entity;
import jtwod.engine.drawable.Shape;

public abstract class EntityController<ParentEngine extends Engine> extends Drawable<ParentEngine> {

    /**
     * A random object for use within the controller.
     */
    private Random random = new Random();

    /**
     * All entities currently being rendered alongside this controller.
     */
    private LinkedList<Entity<ParentEngine>> entities = new LinkedList<>();

    /**
     * The screen that this controller is attached to.
     */
    private Scene<ParentEngine> parentScreen;

    /**
     * Create a new EntityController and attach it to the supplied Screen.
     *
     * @param screen The Screen associated with this EntityController.
     */
    public EntityController(Scene<ParentEngine> screen)
    {
        super(-1, screen.getParentEngine());
        this.parentScreen = screen;
    }

    /**
     * Handles a control tick.
     */
    protected void runControlTick() {
        // Not implemented by default.
    }

    /**
     * Handles a control tick on a per Entity basis.
     * @param entity The Entity to run a Tick for.
     */
    protected void iterateEntityPerTick(Entity<ParentEngine> entity)
    {
        // Not implemented by default.
    }

    /**
     * Render out all Entities under the scope of this EntityController.
     *
     * @param graphics The Graphics to use for rendering.
     * @param screen The Screen to use as an Observer.
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> screen)
    {
        for (int entityId = 0; entityId < getAllEntities().size(); entityId++) {
            getAllEntities().get(entityId).render(graphics, screen);
        }
    }

    /**
     * Perform an update.
     */
    @Override
    public final void update()
    {
        this.runControlTick();

        for (int i = 0; i < this.getAllEntities().size(); i++) {
            Entity<ParentEngine> entity = this.getAllEntities().get(i);

            // Perform control tick per entity.
            iterateEntityPerTick(entity);

            // Check bounds
            if (
            	! entity.getPosition().isInsideBoundsOf(
            		Shape.MaxSizeBaseObject(
            				entity.getParentEngine().getClass(),
							this.getParentEngine(),
							128
					)
				)
			) {
                entity.onExitBounds();
            }

            // Check Entity Collision
            if (! entity.isDead()) {
                for (int collidingEntityId = 0; collidingEntityId < this.getAllEntities().size(); collidingEntityId++) {
                    Entity<ParentEngine> collidingEntity = this.getAllEntities().get(collidingEntityId);
                    if (collidingEntity != entity && !collidingEntity.isDead()) {
                        if (entity.isCollidingWith(collidingEntity)) {
                            entity.onCollide(collidingEntity);
                        }
                    }
                }
            }

            // Perform Heart Beat
            entity.performUpdate();
        }
    }

    /**
     * The screen that this Controller is attached to.
     *
     * @return The Screen associated with this EntityController.
     */
    public final Scene<ParentEngine> getParentScreen()
    {
        return this.parentScreen;
    }

    /**
     * Get the random utility object.
     *
     * @return The Random associated with this EntityController.
     */
    public final Random getRandom()
    {
        return this.random;
    }

    /**
     * Retrieve all Entities attached to this EntityController.
     *
     * @return All entities being managed by the EntityController.
     */
    public final LinkedList<Entity<ParentEngine>> getAllEntities()
    {
        return this.entities;
    }

    /**
     * Spawn an Entity on this Controller.
     *
     * @param entity The Entity to add.
     */
    public final void spawnEntity(Entity<ParentEngine> entity)
    {
    	this.parentScreen.addKeyListener(entity);
        this.entities.add(entity);
    }

    /**
     * Despawn an Entity from this Controller.
     *
     * @param entity The Entity to remove.
     */
    public final void deSpawnEntity(Entity<ParentEngine> entity)
    {
    		this.parentScreen.removeKeyListener(entity);
        this.entities.remove(entity);
    }

    /**
     * Despawn all entities from this Controller.
     */
    public final void deSpawnAllEntities()
    {
    		while(entities.size() > 0) {
        		Entity<ParentEngine> entity = entities.pollFirst();
            this.parentScreen.removeKeyListener(entity);
        }
    }

}
