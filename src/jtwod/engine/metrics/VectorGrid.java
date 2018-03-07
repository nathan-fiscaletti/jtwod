package jtwod.engine.metrics;

import jtwod.engine.Drawable;
import jtwod.engine.Engine;

import jtwod.engine.Scene;
import jtwod.engine.drawable.Image;
import jtwod.engine.drawable.Text;
import jtwod.engine.graphics.Texture;
import jtwod.engine.graphics.TextureGroup;

import java.awt.*;

/**
 * Represents a grid of Vectors with a specified cell size.
 */
public final class VectorGrid<ParentEngine extends Engine> extends Drawable<ParentEngine> {

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
        ParentEngine engine,
        Scene<ParentEngine> scene
    ) {
        super(Integer.MAX_VALUE, engine, scene);
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

        initializeGridLines();
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
        ParentEngine engine,
        Scene<ParentEngine> scene
    ) {
        super(Integer.MAX_VALUE, engine, scene);
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

        initializeGridLines();
    }

    /**
     * Initialize a new VectorGrid using the specified Cell Dimensions and Grid Dimensions.
     *
     * @param cellDimensions The Dimensions for a cell on this VectorGrid.
     * @param gridDimensions The Dimensions for this VectorGrid.
     */
    public VectorGrid(
        Dimensions cellDimensions,
        Dimensions gridDimensions,
        ParentEngine engine,
        Scene<ParentEngine> scene
    )  {
        super(Integer.MAX_VALUE, engine, scene);

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

        initializeGridLines();
    }

    /**
     * Initialize a new VectorGrid using the specified Cell Dimensions and Grid Dimensions.
     *
     * @param location The starting point for this VectorGrid within the parent Scene.
     * @param cellDimensions The Dimensions for a cell on this VectorGrid.
     * @param gridDimensions The Dimensions for this VectorGrid.
     */
    public VectorGrid(
            Vector location,
            Dimensions cellDimensions,
            Dimensions gridDimensions,
            ParentEngine engine,
            Scene<ParentEngine> scene
    ) {
        super(Integer.MAX_VALUE, engine, scene);

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

        initializeGridLines();
    }

    /**
     * Retrieve a Vector at the specified x and y coordinates of this VectorGrid
     * and constrains it to the bounds of this VectorGrid.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The Vector.
     */
    public final Vector getVectorAtGridPoint(int x, int y)
    {
        return this.getUnconstrainedVectorAtGridPoint(x, y)
            .constrain (
                Vector.Zero(),
                Vector.Zero()
                    .plusX(this.gridDimensions.getWidth())
                    .plusY(this.gridDimensions.getHeight())
            );
    }

    /**
     * Retrieve an unconstrained Vector at the specified x and y coordinates of this VectorGrid.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The Vector.
     */
    public final Vector getUnconstrainedVectorAtGridPoint(int x, int y)
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
            );
    }

    /**
     * Retrieve a Vector at the center of the cell of this VectorGrid specified by x and y coordinates.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The Vector.
     */
    public final Vector getVectorAtCenterOfGridPoint(int x, int y)
    {
        return this.getUnconstrainedVectorAtCenterOfGridPoint(x, y)
            .constrain(
                Vector.Zero(),
                Vector.Zero()
                    .plusX(this.gridDimensions.getWidth())
                    .plusY(this.gridDimensions.getHeight())
            );
    }

    /**
     * Retrieve an unconstrained Vector at the center of the cell of this VectorGrid specified by x and y coordinates.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The Vector.
     */
    public final Vector getUnconstrainedVectorAtCenterOfGridPoint(int x, int y)
    {
        return this.getUnconstrainedVectorAtGridPoint(x, y).plus(
            -(this.cellDimensions.getWidth() / 2),
            -(this.cellDimensions.getHeight() / 2)
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

    /**
     * Initialize the drawables for this VectorGrids Grid Lines.
     */
    private void initializeGridLines()
    {
        // Get the line counts
        int horizontalLines = this.gridDimensions.getWidth() / this.cellDimensions.getWidth();
        int verticalLines = this.gridDimensions.getHeight() / this.cellDimensions.getHeight();

        // Initialize the textures
        TextureGroup lineTextures = new TextureGroup();
        lineTextures.addTexture(
            "VerticalLine",
            Texture.dashedTexture(
                new Dimensions(
                    1,
                    this.gridDimensions.getHeight() + this.cellDimensions.getHeight()
                ),
                false,
                Color.white,
                Color.black
            )
        );

        lineTextures.addTexture(
            "HorizontalLine",
            Texture.dashedTexture(
                new Dimensions(
                    this.gridDimensions.getWidth() + this.cellDimensions.getWidth(),
                    1
                ),
                true,
                Color.white,
                Color.black
            )
        );

        // Create Vertical lines
        for (int i = 0;i<=verticalLines+1; i++) {
            this.getSubDrawableGroup().addDrawable(
                new Image<>(
                    Integer.MAX_VALUE,
                    lineTextures.getTexture(
                        "VerticalLine"
                    ),
                    this.getUnconstrainedVectorAtGridPoint(
                        i+1,
                        1
                    ),
                    this.getParentEngine(),
                    this.getParentScene()
                )
            );
        }

        for (int i = 0;i<=horizontalLines+1; i++) {
            this.getSubDrawableGroup().addDrawable(
                new Image<>(
                    Integer.MAX_VALUE,
                    lineTextures.getTexture(
                        "HorizontalLine"
                    ),
                    this.getUnconstrainedVectorAtGridPoint(
                        1,
                        i+1
                    ),
                    this.getParentEngine(),
                    this.getParentScene()
                )
            );
        }

        int cellCountX = this.gridDimensions.getWidth() / this.cellDimensions.getWidth();
        int cellCountY = this.gridDimensions.getHeight() / this.cellDimensions.getHeight();

        for (int row = 1; row<=cellCountY+1; row++) {
            for (int column = 1; column<=cellCountX+1; column++) {
                Vector start = this.getUnconstrainedVectorAtGridPoint(column, row);
                this.getSubDrawableGroup().addDrawable(
                    new Text<>(
                        Integer.MAX_VALUE,
                        "x: " + column + ", y: " + row,
                        new Font(
                            "System",
                            Font.PLAIN,
                            10
                        ),
                        Color.yellow,
                        start.plusY(10).plusX(3),
                        this.getParentEngine(),
                        this.getParentScene()
                    )
                );
                this.getSubDrawableGroup().addDrawable(
                    new Image<>(
                        Integer.MAX_VALUE,
                        Texture.colorTexture(
                            Color.RED,
                            Dimensions.Square(3)
                        ),
                        start.plusY(-1).plusX(-1),
                        this.getParentEngine(),
                        this.getParentScene()
                    )
                );
            }
        }
    }

    @Override
    protected void update() {
        // Not implemented.
    }
}
