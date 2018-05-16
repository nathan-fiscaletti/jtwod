package jtwod.engine;

import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.event.KeyEvent;

import jtwod.engine.drawable.Entity;
import jtwod.engine.drawable.Shape;

/**
 * For controlling all <code>{@link jtwod.engine.drawable.Entity Entitiy}</code>
 * objects associated with a <code>{@link jtwod.engine.Scene Scene}</code>.
 *  
 * <p>
 * Each <b>tick</b> that occurs on an
 * <code>{@link jtwod.engine.EntityController EntityController}</code>
 * is handled in this order:
 * </p>
 * <ol>
 * <li>
 * <code>
 * {@link jtwod.engine.EntityController#runControlUpdate()}
 * </code>
 * </li>
 * <li>
 * <code>
 * {@link jtwod.engine.EntityController#iterateEntityPerControlUpdate(Entity)}
 * </code>
 * </li>
 * <li>
 * Internal check for
 * <code>{@link jtwod.engine.drawable.Entity Entity}</code>
 * Collision.
 * </li>
 * <li>
 * <code>{@link jtwod.engine.drawable.Entity#update()}</code>
 * </li>
 * </ol>
 * 
 * @param <ParentEngine> 
 * The type for the parent <code>{@link jtwod.engine.Engine Engine}</code> 
 * associated with this 
 * <code>{@link jtwod.engine.EntityController EntityController}</code>.
 * 
 * @see 
 * EntityController#iterateEntityPerControlUpdate(jtwod.engine.drawable.Entity) 
 * @see EntityController#runControlUpdate() 
 * @see EntityController#spawnEntity(jtwod.engine.drawable.Entity) 
 * @see EntityController#removeEntity(jtwod.engine.drawable.Entity) 
 * @see EntityController#removeAllEntities()
 * @see Drawable#update() 
 */
public abstract class EntityController<
    ParentEngine extends Engine
> extends Drawable<ParentEngine> 
{
    /**
     * All <code>{@link jtwod.engine.drawable.Entity Entity}</code>s currently 
     * being managed by this
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     */
    private final LinkedList<Entity<ParentEngine>> entities;

    /**
     * Create a new
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * and attach it to the supplied
     * <code>{@link jtwod.engine.Scene Scene}</code>.
     *
     * @param scene The Screen associated with this EntityController.
     */
    public EntityController(Scene<ParentEngine> scene)
    {
        super(-1, scene.getParentEngine(), scene);
        this.entities = new LinkedList<>();
    }

    /**
     * Override to control what happens to this
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * on each tick.
     * 
     * <p>
     * Each <b>tick</b> that occurs on an
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * is handled in this order:
     * </p>
     * <ol>
     * <li>
     * <code>
     * {@link jtwod.engine.EntityController#runControlUpdate()}
     * </code>
     * </li>
     * <li>
     * <code>
     * {@link jtwod.engine.EntityController#iterateEntityPerControlUpdate(Entity)}
     * </code>
     * </li>
     * <li>
     * Internal check for
     * <code>{@link jtwod.engine.drawable.Entity Entity}</code>
     * Collision.
     * </li>
     * <li>
     * <code>{@link jtwod.engine.drawable.Entity#update()}</code>
     * </li>
     * </ol>
     */
    protected void runControlUpdate()
    {
        // Not implemented by default.
    }

    /**
     * Override to control what happens during each tick on a per
     * <code>{@link jtwod.engine.drawable.Entity Entity}</code> basis.
     * 
     * <p>
     * Each <b>tick</b> that occurs on an
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * is handled in this order:
     * </p>
     * <ol>
     * <li>
     * <code>
     * {@link jtwod.engine.EntityController#runControlUpdate()}
     * </code>
     * </li>
     * <li>
     * <code>
     * {@link jtwod.engine.EntityController#iterateEntityPerControlUpdate(Entity)}
     * </code>
     * </li>
     * <li>
     * Internal check for
     * <code>{@link jtwod.engine.drawable.Entity Entity}</code>
     * Collision.
     * </li>
     * <li>
     * <code>{@link jtwod.engine.drawable.Entity#update()}</code>
     * </li>
     * </ol>
     * 
     * @param entity 
     * The <code>{@link jtwod.engine.drawable.Entity Entity}</code>
     * perform a tick on.
     */
    protected void iterateEntityPerControlUpdate(Entity<ParentEngine> entity)
    {
        // Not implemented by default.
    }

    /**
     * Render out all <code>{@link jtwod.engine.drawable.Entity Entity}</code>s
     * under the scope of this
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     *
     * @param graphics 
     * The <code>{@link java.awt.Graphics Graphics}</code> object to use for 
     * rendering.
     * 
     * @param screen
     * The <code>{@link jtwod.engine.Scene Scene}</code> that this
     * <code>{@link jtwod.engine.EntityController EntityController}</code>
     * will be rendering out to.
     * 
     * @see jtwod.engine.Drawable#render(java.awt.Graphics, jtwod.engine.Scene) 
     */
    @Override
    protected final void render(Graphics graphics, Scene<ParentEngine> screen)
    {
        getAllEntities().stream().filter(
                (entity) -> (entity.isVisible())
        ).forEach(entity -> entity.render(graphics, screen));
    }

    /**
     * Perform a tick on this
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     * 
     * @see jtwod.engine.Drawable#update()
     */
    @Override
    protected final void update()
    {
        this.runControlUpdate();

        this.getAllEntities().stream().forEach((entity) -> {
            // Perform control tick per entity.
            iterateEntityPerControlUpdate(entity);

            // Check Entity Collision
            if (! entity.isDead()) {
                this.getAllEntities().stream().filter(
                        collidingEntity -> ! collidingEntity.isDead()
                     && collidingEntity != entity
                ).forEach((collidingEntity) -> {
                    if (entity.isCollidingWith(collidingEntity)) {
                        entity.onCollide(collidingEntity);
                    }
                });
            }

            // Perform Heart Beat
            entity.notifyUpdate();
        });
    }

    /**
     * Retrieve a list of all
     * <code>{@link jtwod.engine.drawable.Entity Entity}</code>s 
     * currently being managed by this
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     *
     * @return The <code>{@link jtwod.engine.drawable.Entity Entity}</code>s.
     */
    public final LinkedList<Entity<ParentEngine>> getAllEntities()
    {
        return this.entities;
    }

    /**
     * Spawn an <code>{@link jtwod.engine.drawable.Entity Entity}</code>
     * inside this 
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     *
     * @param entity 
     * The <code>{@link jtwod.engine.drawable.Entity Entity}</code> to spawn.
     */
    public final void spawnEntity(Entity<ParentEngine> entity)
    {
        this.getParentScene().addKeyListener(entity);
        this.getParentScene().addMouseListener(entity.getMouseAdapter());
        this.entities.add(entity);
    }

    /**
     * Removes an <code>{@link jtwod.engine.drawable.Entity Entity}</code> from
     * this <code>{@link jtwod.engine.EntityController EntityController}</code>.
     *
     * @param entity
     * The <code>{@link jtwod.engine.drawable.Entity Entity}</code> to remove.
     */
    public final void removeEntity(Entity<ParentEngine> entity)
    {
        this.getParentScene().removeKeyListener(entity);
        this.getParentScene().removeMouseListener(entity.getMouseAdapter());
        this.entities.remove(entity);
    }

    /**
     * Removes all <code>{@link jtwod.engine.drawable.Entity Entity}</code>s
     * from this 
     * <code>{@link jtwod.engine.EntityController EntityController}</code>.
     */
    public final void removeAllEntities()
    {
        while(entities.size() > 0) {
            Entity<ParentEngine> entity = entities.pollFirst();
            this.getParentScene().removeKeyListener(entity);
            this.getParentScene().removeMouseListener(entity.getMouseAdapter());
        }
    }

    /**
     * Override for Drawable#keyPressed.
     *
     * @param e The KeyEvent.
     * @throws UnsupportedOperationException
     */
    @Override
    public final void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Override for Drawable#keyReleased.
     *
     * @param e The KeyEvent.
     * @throws UnsupportedOperationException
     */
    @Override
    public final void keyReleased(KeyEvent e)
    {
        throw new UnsupportedOperationException();
    }
}
