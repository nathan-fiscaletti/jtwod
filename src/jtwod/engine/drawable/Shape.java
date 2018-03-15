package jtwod.engine.drawable;

import jtwod.engine.Drawable;
import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
     * The maximum positional constraint for the Shape.
     */
    private Vector constraintMax;

    /**
     * The minimum positional constraint for the Shape.
     */
    private Vector constraintMin;

    /**
     * Adapter for listening for Mouse Clicks.
     */
    private MouseAdapter mouseAdapter;

    /**
     * Internal variable used to store the last state of the cursor.
     */
    private boolean isCursorOver = false;
    
    /**
     * Enumeration definition for constraint event types.
     */
    public enum AxisEventType
    {
        LeftX,
        RightX,
        TopY,
        BottomY
    }

    /**
     * Construct the Shape.
     */
    public Shape(int layer, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.position = Vector.Zero();
        this.size = Dimensions.Zero();

        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getParentScene().isCursorVisible()) {
                    Vector mouseLocation = new Vector(e.getX(), e.getY());
                    if (containsVector(mouseLocation)) {
                        Shape.this.mouseClicked(e.getButton(), mouseLocation);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (getParentScene().isCursorVisible()) {
                    Vector mouseLocation = new Vector(e.getX(), e.getY());
                    if (containsVector(mouseLocation)) {
                        Shape.this.mousePressed(e.getButton(), mouseLocation);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getParentScene().isCursorVisible()) {
                    Vector mouseLocation = new Vector(e.getX(), e.getY());
                    if (containsVector(mouseLocation)) {
                        Shape.this.mouseReleased(e.getButton(), mouseLocation);
                    }
                }
            }
        };
    }

    /**
     * Retrieve the MouseAdapter associated with this Shape.
     *
     * @return The MouseAdapter.
     */
    public MouseAdapter getMouseAdapter()
    {
        return this.mouseAdapter;
    }

    /**
     * Called when this Shape was constrained on a specific axis.
     *
     * @param event The constraint border that this Shape was constrained on.
     */
    protected void onConstrained(AxisEventType event)
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
    @Override
    protected void notifyUpdate()
    {
        super.notifyUpdate();
        this.updateCursorOver();
        this.updateConstraints();
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
        if (
            this.position != null &&
            this.constraintMax != null &&
            this.constraintMin != null
        ) {
            this.constrain(this.constraintMin, this.constraintMax);
        }
    }

    /**
     * Update the current tracking of the cursor over this Shape.
     */
    private void updateCursorOver()
    {
        if (this.containsVector(this.getParentEngine().getCursorLocation())) {
            if (! this.isCursorOver) {
                this.isCursorOver = true;
                this.mouseEntered();
            }
        } else {
            if (this.isCursorOver) {
                this.mouseLeft();
            }
            this.isCursorOver = false;
        }
    }

    /**
     * Check if a Vector is within this Shape.
     * @param vector The Vector to use.
     */
    public final boolean containsVector(Vector vector)
    {
        return (
            vector.getX() >= this.position.getX() &&
            vector.getX() <= this.position.getX() + this.size.getWidth() &&
            vector.getY() >= this.position.getY() &&
            vector.getY() <= this.position.getY() + this.size.getHeight()
        );
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
     * @param min The new positional constraint minimum.
     * @param max The new positional constraint maximum.
     */
    public final void setPositionConstraint(Vector min, Vector max)
    {
        this.constraintMin = min;
        this.constraintMax = max;
    }

    /**
     * Called when the mouse clicks this Shape.
     *
     * @param button The mouse button that was pressed. (1, 2, 3)
     * @param location The location in the Scene that the mouse was.
     */
    protected void mouseClicked(int button, Vector location)
    {
        // Not implemented by default.
    }

    /**
     * Called when the mouse presses down on this Shape.
     *
     * @param button The mouse button that was pressed. (1, 2, 3)
     * @param location The location in the Scene that the mouse was.
     */
    protected void mousePressed(int button, Vector location)
    {
        // Not implemented by default.
    }

    /**
     * Called when the mouse releases on this Shape.
     *
     * @param button The mouse button that was pressed. (1, 2, 3)
     * @param location The location in the Scene that the mouse was.
     */
    protected void mouseReleased(int button, Vector location)
    {
        // Not implemented by default.
    }

    /**
     * Called when the mouse enters this Shape.
     */
    protected void mouseEntered()
    {
        // Not implemented by default.
    }

    /**
     * Called when the mouse leaves this Shape.
     */
    protected void mouseLeft()
    {
        // Not implemented by default.
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
     * This will call back to the {@link Shape#onConstrained(AxisEventType)} function.
     * 
     * We use this instead of {@link Vector#constrain(Vector, Vector)}
     *
     * @param min The minimum constraint to use.
     * @param max The maximum constraint to use.
     */
    private void constrain(Vector min, Vector max)
    {
        if (this.position.getX() < min.getX() - min.getXBuffer()) {
            this.position.setX(min.getX() - min.getXBuffer());
            this.onConstrained(AxisEventType.LeftX);
        }

        if (this.position.getY() < min.getY() - min.getYBuffer()) {
            this.onConstrained(AxisEventType.TopY);
            this.position.setY(min.getY() - min.getYBuffer());
        }

        if (this.position.getX() > max.getX() - max.getXBuffer()) {
            this.position.setX(max.getX() - max.getXBuffer());
            this.onConstrained(AxisEventType.RightX);
        }

        if (this.position.getY() > max.getY() - max.getYBuffer()) {
            this.position.setY(max.getY() - max.getYBuffer());
            this.onConstrained(AxisEventType.BottomY);
        }
    }
}
