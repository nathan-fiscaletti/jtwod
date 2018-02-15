package jtwod.engine.drawable;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.*;
import java.util.Arrays;

/**
 * Class used to represent a Graph.
 * @param <ParentEngine> The parent Engine for this Graph.
 */
public abstract class Graph<ParentEngine extends Engine> extends Shape<ParentEngine>
{

    /**
     * The data sets being used for this Graph.
     */
    private double[][] dataSets;

    /**
     * The rate at which to update this Graph in ticks.
     */
    private int updateRate = 10;

    /**
     * Used to track the current tick.
     */
    private int curTick = 0;

    /**
     * Initialize a new Graph.
     *
     * @param layer The layer to draw this Graph to.
     * @param engine The engine that this Graph is attached to.
     * @param position The position to draw this Graph in.
     * @param size The Dimensions of this Graph.
     * @param dataSetCount The number of data sets in this Graph.
     * @param updateRate The update rate in ticks at which to update this Graph.
     */
    public Graph(int layer, ParentEngine engine, Scene<ParentEngine> scene, Vector position, Dimensions size, int dataSetCount, int updateRate)
    {
        super(layer, engine, scene);
        this.setSize(size);
        this.setPosition(position);
        this.updateRate = updateRate;
        this.dataSets = new double[dataSetCount][size.getWidth()];
    }

    /**
     * Retrieve the next value for a data set.
     *
     * @param dataSet The data set ID to use.
     * @return The next value for the data set.
     */
    public abstract double getNextValueForDataSet(int dataSet);

    /**
     * Retrieve the maximum possible value for a data set.
     *
     * @param dataSet The data set ID to use.
     * @return The maximum value for the data set.
     */
    public abstract double getMaxValueForDataSet(int dataSet);

    /**
     * Retrieve the color for a data set.
     *
     * @param dataSet The data set ID to use.
     * @return The color for the data set.
     */
    public abstract Color getColorForDataSet(int dataSet);

    /**
     * Update the graph by appending the next value for each data set.
     */
    @Override
    protected void update()
    {
        if (curTick % updateRate == 0) {
            for (int i = 0; i < dataSets.length; i++) {
                dataSets[i] = Arrays.stream(dataSets[i]).skip(1).toArray();
                dataSets[i] = Arrays.copyOf(dataSets[i], dataSets[i].length + 1);
                dataSets[i][this.getSize().getWidth() - 1] = getNextValueForDataSet(i);
            }
        }

        this.curTick++;
    }

    /**
     * Render the graph out.
     *
     * @param graphics The <code>{@link java.awt.Graphics Graphics}</code>
     *                 object to use for rendering.
     * @param scene The scene to render it out to.
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        for (int dataSetId = 0; dataSetId < dataSets.length; dataSetId++) {
            int lastY = this.getPosition().getY() + this.getSize().getHeight();
            for (int dataValueId = 0; dataValueId < dataSets[dataSetId].length; dataValueId++) {
                graphics.setColor(getColorForDataSet(dataSetId));
                int nextY = this.getPosition().getY() + this.getSize().getHeight()
                    - (1 + (dataSets[dataSetId][dataValueId] == 0 ? 0 : (int) (
                    (
                            dataSets[dataSetId][dataValueId]
                                    / this.getMaxValueForDataSet(dataSetId)
                    ) * this.getSize().getHeight()
                )));

                if (dataValueId != 0) {
                    graphics.drawLine(
                        this.getPosition().getX() + dataValueId - 1, lastY,
                        this.getPosition().getX() + dataValueId, nextY
                    );
                }

                graphics.drawRect(
                    this.getPosition().getX() + dataValueId,

                    nextY,

                    1, 1
                );

                lastY = nextY;
            }
        }
    }
}
