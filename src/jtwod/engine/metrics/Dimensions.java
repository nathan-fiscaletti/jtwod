package jtwod.engine.metrics;

/**
 * Used to represent Dimensions.
 */
public final class Dimensions 
{
    /**
     * The width.
     */
    private int width = 0;

    /**
     * The height.
     */
    private int height = 0;

    /**
     * The aspect ratio for this dimension.
     */
    private AspectRatio aspectRatio;

    /**
     * Create a new dimension.
     *
     * @param aspectRatio the aspect ratio.
     */
    public Dimensions(AspectRatio aspectRatio)
    {
        this.aspectRatio = aspectRatio;
        this.width = aspectRatio.getRatio().getWidth();
        this.height = aspectRatio.getRatio().getHeight();
    }

    /**
     * Create a new dimension.
     * @param width the width.
     * @param height the height.
     */
    public Dimensions(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * Retrieve the width.
     *
     * @return width
     */
    public final int getWidth() 
    {
        return width;
    }

    /**
     * Sets the width.
     * This will recalculate using the
     * aspect ratio if one is set,
     * meaning that the height might
     * also be updated when you
     * modify the width.
     *
     * @param width the new width.
     */
    public final Dimensions setWidth(int width) 
    {
        this.width = width;
        this.recalculate();
        
        return this;
    }

    /**
     * Retrieve the height.
     *
     * @return height
     */
    public final int getHeight() 
    {
        return height;
    }

    /**
     * Sets the height.
     * This will recalculate using the
     * aspect ratio if one is set,
     * meaning that the height might
     * not end up being what you set
     * it to in the end.
     *
     * @param height the new width.
     */
    public final Dimensions setHeight(int height) 
    {
        this.height = height;
        this.recalculate();
        
        return this;
    }

    /**
     * Convert this Dimensions object into a Vector object
     * mapping the Width property to the X coordinate and
     * the Height property to the Y coordinate.
     *
     * @return The new Vector.
     */
    public final Vector asVector()
    {
        return new Vector(this.width, this.height);
    }

    /**
     * Add to the width of these Dimensions.
     *
     * @param width The width to add.
     * @return These Dimensions.
     */
    public final Dimensions plusWidth(int width)
    {
        return new Dimensions(this.width + width, this.height);
    }

    /**
     * Add to the height of these Dimensions.
     *
     * @param height The height to add.
     * @return These Dimensions.
     */
    public final Dimensions plusHeight(int height)
    {
        return new Dimensions(this.width, this.height + height);
    }

    /**
     * Add to the width height of these Dimensions.
     *
     * @param width The width to add.
     * @param height The height to add.
     * @return These Dimensions.
     */
    public final Dimensions plus(int width, int height)
    {
        return new Dimensions(this.width + width, this.height + height);
    }

    /**
     * Retrieve the aspect ratio.
     *
     * @return aspectRatio
     */
    public final AspectRatio getAspectRatio() 
    {
        return aspectRatio;
    }

    /**
     * Update the aspect ratio.
     * This will also recalculate the dimensions
     * using the new aspect ratio.
     *
     * @param aspectRatio
     */
    public final Dimensions setAspectRatio(AspectRatio aspectRatio) 
    {
        this.aspectRatio = aspectRatio;
        this.recalculate();
        
        return this;
    }

    /**
     * Convert this object to an AWT Dimension.
     * @return
     */
    public final java.awt.Dimension asAwtDimension()
    {
        return new java.awt.Dimension(this.width, this.height);
    }

    /**
     * Recalculate the dimensions using the current aspect ratio.
     */
    private void recalculate()
    {
        if (this.aspectRatio != null) {
            switch (this.aspectRatio.getControlAxis()) {
                case HeightControlled : 
                    this.width = 
                        this.height / 
                            this.aspectRatio.getRatio().getHeight()
                          * this.aspectRatio.getRatio().getWidth();
                    break;
                case WidthControlled :
                    this.height = 
                        this.width / 
                              this.aspectRatio.getRatio().getWidth() 
                            * this.aspectRatio.getRatio().getHeight();
                    break;
            }
            
        }
    }

    /**
     * Create a perfectly square Dimensions object using the specified size.
     *
     * @param size The width and height of the Dimensions.
     * @return The Dimensions.
     */
    public static final Dimensions Square(int size)
    {
        return new Dimensions(size, size);
    }

    /**
     * Retrieve the Zero dimension.
     *
     * @return
     */
    public static final Dimensions Zero()
    {
        return new Dimensions(0, 0);
    }

    /**
     * Create Dimensions from AWT Dimensions.
     *
     * @param dimensions
     * @return
     */
    public static final Dimensions FromAwtDimensions(java.awt.Dimension dimensions)
    {
        return new Dimensions((int)dimensions.getWidth(), (int)dimensions.getHeight());
    }
}
