package jtwod.engine.metrics;

import jtwod.engine.Engine;
import jtwod.engine.drawable.Shape;

import java.util.Random;

public final class Vector
{
    /**
     * The X value.
     */
    private int x;

    /**
     * The Y value.
     */
    private int y;

    /**
     * The buffer for the vector X axis.
     */
    private int bufferX = 0;
    
    /**
     * The buffer for the vector Y axis.
     */
    private int bufferY = 0;

    /**
     * Create a new base vector.
     */
    public Vector()
    {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Create a new vector with the supplied X and Y values.
     *
     * @param x The X value.
     * @param y The Y value.
     */
    public Vector(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Constrain this vector to a specific width and height.
     *
     * @param constraint
     * @return Vector This Vector.
     */
    public final Vector constrain(Vector constraint)
    {
        if (this.x < 0 - constraint.bufferX) {
            this.x = 0 - constraint.bufferX;
        }

        if (this.y < 0 - constraint.bufferY) {
            this.y = 0 - constraint.bufferY;
        }

        if (this.x > constraint.x + constraint.bufferX) {
            this.x = constraint.x + constraint.bufferX;
        }

        if (this.y > constraint.y + constraint.bufferY) {
            this.y = constraint.y + constraint.bufferY;
        }

        return this;
    }

    /**
     * Set the X value.
     *
     * @param x
     * @return
     */
    public final Vector setX(int x)
    {
        this.x = x;

        return this;
    }

    /**
     * Set the Y value.
     *
     * @param y
     * @return
     */
    public final Vector setY(int y)
    {
        this.y = y;

        return this;
    }

    /**
     * Retrieve the X value.
     *
     * @return
     */
    public final int getX()
    {
        return this.x;
    }

    /**
     * Retrieve the X value.
     *
     * @return
     */
    public final int getY()
    {
        return this.y;
    }
    
    /**
     * Retrieve the X axis buffer.
     * 
     * @return
     */
    public final int getXBuffer()
    {
        return this.bufferX;
    }
    
    /**
     * Update the X axis buffer.
     * 
     * @param bufferX
     */
    public final Vector setXBuffer(int bufferX)
    {
        this.bufferX = bufferX;

        return this;
    }

    /**
     * Retrieve the Y axis buffer.
     * 
     * @return
     */
    public final int getYBuffer()
    {
        return this.bufferY;
    }
    
    /**
     * Update the Y axis buffer.
     * 
     * @param bufferY
     */
    public final Vector setYBuffer(int bufferY)
    {
        this.bufferY = bufferY;

        return this;
    }

    /**
     * Add to the Y value to create a new Vector.
     *
     * @param plus The value to add.
     *
     * @return Vector
     */
    public final Vector plusY(int plus) 
    {
        return new Vector(this.x, this.y + plus);
    }

    /**
     * Add to the X value to create a new Vector.
     *
     * @param plus The value to add.
     *
     * @return Vector
     */
    public final Vector plusX(int plus) 
    {
        return new Vector(this.x + plus, this.y);
    }

    /**
     * Add to the X and Y values to create a new Vector.
     *
     * @param x The value to add to X.
     * @param y The value to add to Y.
     *
     * @return Vector
     */
    public final Vector plus(int x, int y) 
    {
        return new Vector(this.x + x, this.y + y);
    }

    /**
     * Add on Vector to another Vector to create a new Vector.
     *
     * @param plus The Vector to add to this Vector.
     *
     * @return Vector
     */
    public final Vector plus(Vector plus)
    {
        return new Vector(this.x + plus.x, this.y + plus.y);
    }

    /**
     * Multiply the X and Y values of this vector by the values passed to create a new Vector.
     *
     * @param x The value to multiply X by.
     * @param y The value to multiply Y by.
     *
     * @return Vector
     */
    public final Vector multipliedBy(int x, int y)
    {
        return new Vector(this.x * x, this.y * y);
    }

    /**
     * Multiply this Vector by another Vector to create a new Vector.
     *
     * @param multipliedBy The Vector to multiply this Vector by.
     *
     * @return Vector
     */
    public final Vector multipliedBy(Vector multipliedBy)
    {
        return new Vector(this.x * multipliedBy.x, this.y * multipliedBy.y);
    }

    /**
     * Divide the X and Y values of this vector by the values passed to create a new Vector.
     *
     * @param x The value to divide X by.
     * @param y The value to divide Y by.
     *
     * @return Vector
     */
    public final Vector dividedBy(int x, int y)
    {
        return new Vector(this.x * x, this.y * y);
    }

    /**
     * Divide this Vector by another Vector to create a new Vector.
     *
     * @param dividedby The Vector to divide this Vector by.
     *
     * @return Vector
     */
    public final Vector dividedBy(Vector dividedby)
    {
        return new Vector(this.x * dividedby.x, this.y * dividedby.y);
    }

    /**
     * Check if this vector is zero.
     *
     * @return boolean
     */
    public final boolean isZero()
    {
        return this.x == 0 && this.y == 0;
    }

    /**
     * Check if this Vector is inside the bounds of a Shape.
     *
     * @param shape
     *
     * @return boolean
     */
    public final boolean isInsideBoundsOf(Shape<? extends Engine> shape)
    {
        return ! (
                this.x < shape.getPosition().x ||
                this.y < shape.getPosition().y ||
                this.x > shape.getPosition().x + shape.getSize().getWidth() ||
                this.y > shape.getPosition().y + shape.getSize().getHeight()
        );
    }

    /**
     * Create a Zero Vector.
     *
     * @return Vector
     */
    public final static Vector Zero() 
    {
        return new Vector(0, 0);
    }

    /**
     * The maximum vector allowed with an additional buffer.
     * @return
     */
    public final static Vector Max(int buffer, Engine engine) 
    {
        Vector result = new Vector(
                engine.getWindowSize().getWidth() + buffer, 
            engine.getWindowSize().getHeight() + buffer
        );
        
        result.bufferX = buffer;
        result.bufferY = buffer;

        return result;
    }
    
    /**
     * The maximum vector allowed with an additional buffer on a per-axis basis.
     * @return
     */
    public final static Vector Max(int bufferX, int bufferY, Engine engine)
    {
        Vector result = new Vector(
                engine.getWindowSize().getWidth() + bufferX, 
                engine.getWindowSize().getHeight() + bufferY
            );
        
        result.bufferX = bufferX;
        result.bufferY = bufferY;

        return result;
    }

    /**
     * The maximum vector allowed.
     * @return
     */
    public final static Vector Max(Engine engine) 
    {
        return Vector.Max(0, engine);
    }

    /**
     * Retrieve a random vector.
     * Note: Minimum will automatically go to 0,0.
     *
     * @param max The maximum that the Vector can be.
     * @return The random Vector.
     */
    public final static Vector Random(Vector max)
    {
        Random r = new Random();
        return new Vector(r.nextInt(max.x + 1), r.nextInt(max.y + 1));
    }

    /**
     * Retrieve a random vector.
     *
     * @param min The minimum that the Vector can be.
     * @param max The maximum that the Vector can be.
     * @return The random Vector.
     */
    public final static Vector Random(Vector min, Vector max)
    {
        Random r = new Random();
        return new Vector(r.nextInt(max.x-min.x)+min.x, r.nextInt(max.y-min.y)+min.y);
    }
}
