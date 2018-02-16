package jtwod.engine.drawable;

import jtwod.engine.Drawable;
import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Vector;

import java.awt.*;

/**
 * Class used to represent a Line as a Drawable.
 * @param <ParentEngine> The parent Engine type for this Line.
 */
public final class Line<ParentEngine extends Engine> extends Drawable<ParentEngine>
{
    /**
     * The starting point for the Line.
     */
    private Vector start;

    /**
     * The ending point for the Line.
     */
    private Vector end;

    /**
     * The Color of the Line.
     */
    private Color color;

    /**
     * The thickness of the Line.
     */
    private int thickness;

    /**
     * Construct a new Line.
     *
     * @param layer The layer to draw the Line on.
     * @param start The starting point for the Line.
     * @param end The ending point for the Line.
     * @param engine The Engine.
     * @param scene The parent Scene for the Line.
     */
    public Line(int layer, Vector start, Vector end, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.start = start;
        this.end = end;
        this.thickness = 1;
        this.color = Color.white;
    }

    /**
     * Construct a new Line.
     *
     * @param layer The layer to draw the Line on.
     * @param start The starting point for the Line.
     * @param end The ending point for the Line.
     * @param thickness The thickness of the Line.
     * @param engine The Engine.
     * @param scene The parent Scene for the Line.
     */
    public Line(int layer, Vector start, Vector end, int thickness, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.start = start;
        this.end = end;
        this.thickness = thickness;
        this.color = Color.white;
    }

    /**
     * Construct a new Line.
     *
     * @param layer The layer to draw the Line on.
     * @param start The starting point for the Line.
     * @param end The ending point for the Line.
     * @param color The Color of the Line.
     * @param engine The Engine.
     * @param scene The parent Scene for the Line.
     */
    public Line(int layer, Vector start, Vector end, Color color, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.start = start;
        this.end = end;
        this.thickness = 1;
        this.color = color;
    }

    /**
     * Construct a new Line.
     *
     * @param layer The layer to draw the Line on.
     * @param start The starting point for the Line.
     * @param end The ending point for the Line.
     * @param thickness The thickness of the Line.
     * @param color The Color of the Line.
     * @param engine The Engine.
     * @param scene The parent Scene for the Line.
     */
    public Line(int layer, Vector start, Vector end, int thickness, Color color, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.start = start;
        this.end = end;
        this.thickness = thickness;
        this.color = color;
    }

    /**
     * Update the starting point of the Line.
     *
     * @param start The new starting point.
     */
    public final void setStart(Vector start)
    {
        this.start = start;
    }

    /**
     * Update the ending point of the Line.
     *
     * @param end The new ending point.
     */
    public final void setEnd(Vector end)
    {
        this.end = end;
    }

    /**
     * Update the thickness of the line.
     *
     * @param thickness The new thickness.
     */
    public final void setThickness(int thickness)
    {
        this.thickness = thickness;
    }

    /**
     * Render the Line out.
     * @param graphics The <code>{@link java.awt.Graphics Graphics}</code>
     *                 object to use for rendering.
     * @param scene The <code>{@link jtwod.engine.Scene Scene}</code> on which
     */
    @Override
    protected final void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        super.render(graphics, scene);

        Color resetColor = graphics.getColor();
        Graphics2D g2d = (Graphics2D)graphics;
        Stroke resetStroke = g2d.getStroke();

        g2d.setColor(this.color);
        g2d.setStroke(new BasicStroke(this.thickness));
        g2d.drawLine(this.start.getX(), this.start.getY(), this.end.getX(), this.end.getY());

        g2d.setStroke(resetStroke);
        graphics.setColor(resetColor);
    }

    /**
     * Update the Line.
     */
    @Override
    protected final void update() {
        // Not implemented by default.
    }
}
