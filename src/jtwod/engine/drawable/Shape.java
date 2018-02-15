package jtwod.engine.drawable;

import javafx.scene.Parent;
import jtwod.engine.Drawable;
import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.*;

/**
 * A base object in the engine.
 *
 * @param <ParentEngine> The ParentEngine type for this Shape.
 */
public abstract class Shape<ParentEngine extends Engine> extends Drawable<ParentEngine>
{
    /**
     * The position of the Shape.
     */
    private Vector position;

    /**
     * The size of the Shape.
     */
    private Dimensions size;

    /**
     * The positional constraint for the Shape.
     */
    private Vector positionConstraint;
    
    /**
     * Enumeration definition for constraint event types.
     */
    public enum ConstrainedEventType 
    {
        LeftXAxis,
        RightXAxis,
        TopYAxis,
        BottomYAxis
    }

    /**
     * Construct the Shape.
     */
    public Shape(int layer, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.position = Vector.Zero();
        this.size = Dimensions.Zero();
        this.positionConstraint = Vector.Max(engine);
    }
    
    /**
     * Called when this Shape was constrained on a specific axis.
     *
     * @param event The constraint border that this Shape was constrained on.
     */
    protected void onConstrained(ConstrainedEventType event)
    {
        // Not implemented by default.
    }

    /**
     * Update the Shape.
     */
    @Override
    protected void update()
    {
        // Not implemented by default.
    }

    /**
     * Perform an update on the Shape.
     */
    protected void performUpdate()
    {
        this.updateConstraints();
        this.update();
    }

    /**
     * Check if this Shape is colliding with another.
     *
     * @param shape The other Shape that we want to see if this Shape is colliding with.
     * @return True if the Shapes are colliding.
     */
    public final boolean isCollidingWith(Shape<ParentEngine> shape)
    {
        Rectangle r = new Rectangle();
        r.setBounds(this.position.getX(), this.position.getY(), this.getSize().getWidth(), this.getSize().getHeight());

        Rectangle r2 = new Rectangle();
        r2.setBounds(shape.getPosition().getX(), shape.getPosition().getY(), shape.getSize().getWidth(), shape.getSize().getHeight());

        return (r.intersects(r2));
    }

    /**
     * Move the Shape starting from it's current position.
     *
     * @param distance The distance to move the Shape.
     */
    public final void move(Vector distance)
    {
        this.position = this.position.plus(distance);
    }

    /**
     * Update the position of the Shape based on it's positional constraints.
     */
    public final void updateConstraints()
    {
        if (this.position != null && this.positionConstraint != null && ! this.positionConstraint.isZero()) {
            this.constrainCustom(this.positionConstraint);
        }
    }

    /**
     * Retrieve the position of this Shape.
     *
     * @return The position of this Shape
     */
    public final Vector getPosition()
    {
        return position;
    }

    /**
     * Update the position of this Shape.
     *
     * @param position the new position.
     */
    public final void setPosition(Vector position)
    {
        this.position = position;
    }

    /**
     * Get the size of this Shape.
     *
     * @return
     */
    public final Dimensions getSize()
    {
        return this.size;
    }

    /**
     * Update the size of this Shape.
     *
     * @param size the new size.
     */
    public final void setSize(Dimensions size)
    {
        this.size = size;
    }

    /**
     * Update the position constraint for this Shape.
     *
     * @param positionConstraint The new positional constraint.
     */
    public final void setPositionConstraint(Vector positionConstraint)
    {
        this.positionConstraint = positionConstraint;
    }

    /**
     * Retrieves a Shape for the specified engine at the largest size possible.
     *
     * @param engine The engine to pull the Shape from.
     * @return The largest possible Shape for the associated Engine.
     */
    public final static <ParentEngine extends Engine> Shape<ParentEngine> MaxSizeBaseObject
    (
        Class<ParentEngine> type,
        Engine engine,
        int buffer
    ) {
        return new Shape<ParentEngine>(-1, type.cast(engine), null){
            {
                this.setSize(new Dimensions(engine.getWindowSize().getWidth() + buffer, engine.getWindowSize().getHeight() + buffer));
                this.setPosition(Vector.Zero().plusX(-buffer).plusY(-buffer));
            }
        };
    }
    
    /**
     * A custom implementation of the Vector constraint mechanism.
     * This will call back to the {@link Shape#onConstrained(ConstrainedEventType)} function.
     * 
     * We use this instead of {@link Vector#constrain(Vector)}
     *
     * @param constraint The constraint to use.
     */
    private void constrainCustom(Vector constraint)
    {
        if (this.position.getX() < 0 - constraint.getXBuffer()) {
            this.position.setX(0);
            this.onConstrained(ConstrainedEventType.LeftXAxis);
        }

        if (this.position.getY() < 0 - constraint.getYBuffer()) {
            this.position.setY(0);
            this.onConstrained(ConstrainedEventType.TopYAxis);
        }

        if (this.position.getX() > constraint.getX() - constraint.getXBuffer()) {
            this.position.setX(constraint.getX());
            this.onConstrained(ConstrainedEventType.RightXAxis);
        }

        if (this.position.getY() > constraint.getY() - constraint.getYBuffer()) {
            this.position.setY(constraint.getY());
            this.onConstrained(ConstrainedEventType.BottomYAxis);
        }
    }
}
