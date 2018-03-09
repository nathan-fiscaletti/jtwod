package jtwod.engine.drawable;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;

/**
 * Text that can be rendered out as a Drawable.
 *
 * @param <ParentEngine> The ParentEngine type for this Text object.
 */
public class Text<ParentEngine extends Engine> extends Shape<ParentEngine>
{
    /**
     * The text to display for this Text object.
     */
    private String text;

    /**
     * The font to display for this Text object.
     */
    private Font font;

    /**
     * The color at which to display the font for this Text object.
     */
    private Color color;

    /**
     * The center constraint to use for this Text object.
     */
    private Center center;

    /**
     * The position of this Text objects parent container.
     */
    private Vector parentStart;

    /**
     * The Dimensions of this Text objects parent container.
     */
    private Dimensions parentDimensions;

    /**
     * The clickable Shape to attach to this Text Object.
     */
    private Shape<ParentEngine> clickableShape;

    /**
     * Create the new Text object and default to Center.Parent.
     *
     * @param layer The layer to render this Drawable on.
     * @param text The text to display.
     * @param font The font to use.
     * @param color The color to make the text.
     * @param engine The parent Engine associated with this Drawable.
     */
    public Text(int layer, String text, Font font, Color color, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = Center.Parent;
        this.parentStart = Vector.Zero();
        this.parentDimensions = this.getParentEngine().getWindowSize();

        this.initializeClickableShape();
    }

    /**
     * Create the new Text object and default to Center.None.
     *
     * @param text The text to display.
     * @param font THe font to use.
     * @param color The color to make the text.
     * @param position The position in which to put the Text.
     * @param engine The parent Engine for this Text object.
     */
    public Text(int layer, String text, Font font, Color color, Vector position, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = Center.None;
        this.setPosition(position);

        this.parentStart = Vector.Zero();
        this.parentDimensions = this.getParentEngine().getWindowSize();

        this.initializeClickableShape();
    }

    /**
     * Create the new Text object.
     *
     * @param text The text to display.
     * @param font THe font to use.
     * @param color The color to make the text.
     * @param centerType The Drawable.Center value to use.
     * @param position The position in which to put the Text working along side the Drawable.Center value.
     * @param engine The parent Engine for this Text object.
     */
    public Text(int layer, String text, Font font, Color color, Center centerType, Vector position, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = centerType;
        this.setPosition(position);

        this.parentStart = Vector.Zero();
        this.parentDimensions = this.getParentEngine().getWindowSize();

        this.initializeClickableShape();
    }

    /**
     * Create the new Text object.
     *
     * @param text The text to display.
     * @param font THe font to use.
     * @param color The color to make the text.
     * @param parentStart The starting point for the parent of this Text object.
     * @param parentDimensions The dimensions of the parent of this Text Object.
     * @param engine The parent Engine for this Text object.
     */
    public Text(int layer, String text, Font font, Color color, Vector parentStart, Dimensions parentDimensions, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = Center.Parent;

        this.parentStart = parentStart;
        this.parentDimensions = parentDimensions;

        this.initializeClickableShape();
    }

    /**
     * Create the new Text object.
     *
     * @param text The text to display.
     * @param font The font to use.
     * @param color The color to make the text.
     * @param centerType The Drawable.Center value to use.
     * @param position The position in which to put the Text working along side the Drawable.Center value.
     * @param parentStart The starting point for the parent of this Text object.
     * @param parentDimensions The dimensions of the parent of this Text Object.
     * @param engine The parent Engine for this Text object.
     */
    public Text(int layer, String text, Font font, Color color, Center centerType, Vector position, Vector parentStart, Dimensions parentDimensions, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = centerType;

        this.parentStart = parentStart;
        this.parentDimensions = parentDimensions;
        this.setPosition(position);

        this.initializeClickableShape();
    }

    private void initializeClickableShape()
    {
        this.clickableShape = new Shape<ParentEngine>(
            this.getLayer() + 1,
            this.getParentEngine(),
            this.getParentScene()
        ) {
            @Override
            protected void mouseClicked(int button, Vector location) {
                Text.this.mouseClicked(button, location);
            }

            @Override
            protected void mousePressed(int button, Vector location) {
                Text.this.mousePressed(button, location);
            }

            @Override
            protected void mouseReleased(int button, Vector location) {
                Text.this.mouseReleased(button, location);
            }
        };
    }

    /**
     * Render the text out to the scene.
     *
     * @param graphics The Graphics object to use.
     * @param scene The scene to draw the Graphics out to.
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> scene)
    {
        super.render(graphics, scene);
        Font resetFont = graphics.getFont();
        Color resetColor = graphics.getColor();


        graphics.setFont(this.font);
        graphics.setColor(this.color);

        switch (this.center) {
            case Horizontally:
                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    (
                        this.parentDimensions.getWidth() / 2
                    ) - (
                        graphics.getFontMetrics().stringWidth(text) / 2
                    ) + parentStart.getX(),
                    this.getPosition().getY()
                );

                // Update clickable shape.
                this.clickableShape.setPosition(
                    new Vector(
                        (
                            this.parentDimensions.getWidth() / 2
                        ) - (
                            graphics.getFontMetrics().stringWidth(text) / 2
                        ) + parentStart.getX(),

                        this.getPosition().getY()
                    ).plus(
                        0,
                        -(graphics.getFontMetrics().getHeight() / 2)
                    )
                );
                this.clickableShape.setSize(
                    new Dimensions(
                        graphics.getFontMetrics().stringWidth(text),
                        graphics.getFontMetrics().getHeight() / 2
                    )
                );
                break;
            case Vertically:
                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    this.getPosition().getX(),
                    (
                        this.parentDimensions.getHeight() / 2
                    ) - (
                        graphics.getFontMetrics().getHeight() / 2
                    ) + parentStart.getY() + (graphics.getFontMetrics().getHeight() / 2)
                );

                // Update clickable shape.
                this.clickableShape.setPosition(
                    new Vector(
                        this.getPosition().getX(),
                        (
                            (
                                this.parentDimensions.getHeight()
                                - graphics.getFontMetrics().getHeight()
                            ) / 2
                        ) + graphics.getFontMetrics().getAscent() + this.parentStart.getY()
                    ).plus(
                        0,
                        -(graphics.getFontMetrics().getHeight() / 2)
                    )
                );
                this.clickableShape.setSize(
                    new Dimensions(
                        graphics.getFontMetrics().stringWidth(text),
                        graphics.getFontMetrics().getHeight() / 2
                    )
                );
                break;
            case Parent:
                Rectangle2D textBounds = graphics.getFontMetrics().getStringBounds(text, graphics);

                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    (
                        this.parentDimensions.getWidth() / 2
                    ) - (
                        (int)textBounds.getWidth() / 2
                    ) + this.parentStart.getX(),
                    (
                        (
                            this.parentDimensions.getHeight()
                          - graphics.getFontMetrics().getHeight()
                        ) / 2
                    ) + graphics.getFontMetrics().getAscent() + this.parentStart.getY()
                );

                // Update clickable shape.
                this.clickableShape.setPosition(
                    new Vector(
                        (
                            this.parentDimensions.getWidth() / 2
                        ) - (
                            (int)textBounds.getWidth() / 2
                        ) + this.parentStart.getX(),
                        (
                            (
                                this.parentDimensions.getHeight()
                                - graphics.getFontMetrics().getHeight()
                            ) / 2
                        ) + graphics.getFontMetrics().getAscent() + this.parentStart.getY()
                    ).plus(
                        0,
                        -(graphics.getFontMetrics().getHeight() / 2)
                    )
                );
                this.clickableShape.setSize(
                    new Dimensions(
                        graphics.getFontMetrics().stringWidth(text),
                        graphics.getFontMetrics().getHeight() / 2
                    )
                );
                break;
            case None:
                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    this.getPosition().getX(),
                    this.getPosition().getY()
                );

                // Update clickable shape.
                this.clickableShape.setPosition(
                    new Vector(
                        this.getPosition().getX(),
                        this.getPosition().getY()
                    ).plus(
                        0,
                        -(graphics.getFontMetrics().getHeight() / 2)
                    )
                );
                this.clickableShape.setSize(
                    new Dimensions(
                        graphics.getFontMetrics().stringWidth(text),
                        graphics.getFontMetrics().getHeight() / 2
                    )
                );
                break;
        }

        graphics.setFont(resetFont);
        graphics.setColor(resetColor);
    }

    /**
     * Retrieve the current text for this Text object.
     *
     * @return The current text.
     */
    public final String getText()
    {
        return text;
    }

    /**
     * Update the current text for this Text object.
     *
     * @param text The new text.
     */
    public final void setText(String text)
    {
        this.text = text;
    }

    /**
     * Retrieve the current font for this Text object.
     *
     * @return The current font.
     */
    public final Font getFont()
    {
        return this.font;
    }

    /**
     * Update the current Font for this Text object.
     *
     * @param font The new Font.
     */
    public final void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * Retrieve the current color for this Text object.
     *
     * @return the current text Color.
     */
    public final Color getColor()
    {
        return color;
    }

    /**
     * Update the current Color for this Text object.
     *
     * @param color The new Color.
     */
    public final void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Retrieve the current Center constraint for this Text object.
     *
     * @return the current center constraint.
     */
    public final Center getCenter()
    {
        return center;
    }

    /**
     * Update the current position and Center constraint for this Text object.
     *
     * @param center The new center constraint.
     * @param position The new position.
     */
    public final void setPosition(Center center, Vector position)
    {
        this.center = center;
        this.setPosition(position);
    }

    /**
     * Override the getMouseAdapter function for this object
     * to return the MouseAdapter associated with the
     * clickable Shape.
     *
     * @return The MouseAdapter associated with this
     *         Text object.
     */
    @Override
    public final MouseAdapter getMouseAdapter()
    {
        return clickableShape.getMouseAdapter();
    }
}
