package jtwod.engine.metrics;

import jtwod.engine.Engine;
import jtwod.engine.EntityController;
import jtwod.engine.Scene;

/**
 * Represents a grid of Vectors with a specified cell size.
 */
public final class VectorGrid {

    /**
     * The starting point for this VectorGrid.
     */
    private Vector location;

    /**
     * The Dimensions of a cell on this VectorGrid.
     */
    private Dimensions cellDimensions;

    /**
     * The Dimensions ot this VectorGrid.
     */
    private Dimensions gridDimensions;

    /**
     * Initialize a new VectorGrid using the specified Cell Dimensions and parent Scene.
     *
     * @param cellDimensions The Dimensions for a cell on this VectorGrid.
     * @param scene The Scene to use to determine the size of this VectorGrid.
     */
    public VectorGrid(
        Dimensions cellDimensions,
        Scene<? extends Engine> scene
    ) {
        this.setLocation(Vector.Zero());
        this.cellDimensions = cellDimensions;
        this.gridDimensions = scene.getParentEngine().getWindowSize();

        // Update the grid dimensions to handle maximum values.
        this.gridDimensions.setWidth(
            this.gridDimensions.getWidth()
          - this.cellDimensions.getWidth()
        );
        this.gridDimensions.setHeight(
            this.gridDimensions.getHeight()
          - this.cellDimensions.getHeight()
        );
    }

    /**
     * Initialize a new VectorGrid with the specified location, Cell Dimensions and parent Scene.
     *
     * @param location The starting point for this VectorGrid within the parent Scene.
     * @param cellDimensions The Dimensions for a cell on this VectorGrid.
     * @param scene The Scene to use to determine the size of this VectorGrid.
     */
    public VectorGrid(
        Vector location,
        Dimensions cellDimensions,
        Scene<? extends Engine> scene
    ) {
        this.setLocation(location);
        this.cellDimensions = cellDimensions;
        this.gridDimensions = scene.getParentEngine().getWindowSize();

        // Update the grid dimensions to handle maximum values.
        this.gridDimensions.setWidth(
            this.gridDimensions.getWidth()
          - this.cellDimensions.getWidth()
        );
        this.gridDimensions.setHeight(
            this.gridDimensions.getHeight()
          - this.cellDimensions.getHeight()
        );
    }

    /**
     * Initialize a new VectorGrid using the specified Cell Dimensions and Grid Dimensions.
     *
     * @param cellDimensions The Dimensions for a cell on this VectorGrid.
     * @param gridDimensions The Dimensions for this VectorGrid.
     */
    public VectorGrid(Dimensions cellDimensions, Dimensions gridDimensions)
    {
        this.setLocation(Vector.Zero());
        this.cellDimensions = cellDimensions;
        this.gridDimensions = gridDimensions;

        // Update the grid dimensions to handle maximum values.
        this.gridDimensions.setWidth(
            this.gridDimensions.getWidth()
          - this.cellDimensions.getWidth()
        );
        this.gridDimensions.setHeight(
            this.gridDimensions.getHeight()
          - this.cellDimensions.getHeight()
        );
    }

    /**
     * Initialize a new VectorGrid using the specified Cell Dimensions and Grid Dimensions.
     *
     * @param location The starting point for this VectorGrid within the parent Scene.
     * @param cellDimensions The Dimensions for a cell on this VectorGrid.
     * @param gridDimensions The Dimensions for this VectorGrid.
     */
    public VectorGrid(Vector location, Dimensions cellDimensions, Dimensions gridDimensions)
    {
        this.setLocation(location);
        this.cellDimensions = cellDimensions;
        this.gridDimensions = gridDimensions;

        // Update the grid dimensions to handle maximum values.
        this.gridDimensions.setWidth(
            this.gridDimensions.getWidth()
          - this.cellDimensions.getWidth()
        );
        this.gridDimensions.setHeight(
            this.gridDimensions.getHeight()
          - this.cellDimensions.getHeight()
        );
    }

    /**
     * Retrieve a Vector at the specified x and y coordinates of this VectorGrid.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The Vector.
     */
    public final Vector getVectorAtGridPoint(int x, int y)
    {
        return this.getLocation()
            .plusX (
                (
                    this.cellDimensions.getWidth() * x
                ) - this.cellDimensions.getWidth()
            )
            .plusY (
                (
                    this.cellDimensions.getHeight() * y
                ) - this.cellDimensions.getHeight()
            )
            .constrain (
                Vector.Zero()
                    .plusX(this.gridDimensions.getWidth())
                    .plusY(this.gridDimensions.getHeight())
            );
    }

    /**
     * Retrieve a random Vector on this VectorGrid.
     *
     * @return The Vector.
     */
    public final Vector random()
    {
        return Vector.Random(this.getMinimumVector(), this.getMaximumVector());
    }

    /**
     * Get the minimum possible Vector for this VectorGrid.
     *
     * @return The Vector.
     */
    public final Vector getMinimumVector()
    {
        return this.getLocation();
    }

    /**
     * Get the maximum possible Vector for this VectorGrid.
     *
     * @return The Vector.
     */
    public final Vector getMaximumVector()
    {
        return this.getLocation()
            .plusX(this.gridDimensions.getWidth())
            .plusY(this.gridDimensions.getHeight());
    }

    /**
     * Update the location of the VectorGrid.
     *
     * @param location The new location.
     */
    public final void setLocation(Vector location)
    {
        this.location = location;
    }

    /**
     * Get the location of this VectorGrid.
     *
     * @return The location.
     */
    public final Vector getLocation()
    {
        return this.location;
    }

    /**
     * Retrieve the Dimensions of this VectorGrid.
     *
     * @return The Dimensions.
     */
    public final Dimensions getGridDimensions()
    {
        return this.gridDimensions;
    }

    /**
     * Update the Dimensions of this VectorGrid.
     *
     * @param dimensions The Dimensions.
     */
    public final void setGridDimensions(Dimensions dimensions)
    {
        this.gridDimensions = dimensions;
    }

    /**
     * Retrieve the Dimensions of a cell within this VectorGrid.
     *
     * @return The Dimensions.
     */
    public final Dimensions getCellDimensions()
    {
        return this.cellDimensions;
    }

    /**
     * Update the Dimensions of a cell within this VectorGrid.
     *
     * @param dimensions The Dimensions.
     */
    public final void setCellDimensions(Dimensions dimensions)
    {
        this.cellDimensions = dimensions;
    }
}
