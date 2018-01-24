package jtwod.engine.drawable;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.metrics.Vector;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

public final class Text<ParentEngine extends Engine> extends Shape<ParentEngine>
{

    /**
     * The text to display.
     */
    private String text;

    /**
     * The font to display.
     */
    private Font font;

    /**
     * The color at which to display the font.
     */
    private Color color;

    /**
     * The center constraint to use.
     */
    private Center center;

    /**
     * Create the new Text object.
     *
     * @param text
     * @param font
     * @param color
     * @param engine
     */
    public Text(String text, Font font, Color color, ParentEngine engine)
    {
        super(engine);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = Center.None;
    }

    /**
     * Create the new Text object.
     *
     * @param text
     * @param font
     * @param color
     * @param centerType
     * @param position
     * @param engine
     */
    public Text(String text, Font font, Color color, Center centerType, Vector position, ParentEngine engine)
    {
        super(engine);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = centerType;
        this.setPosition(position);
    }

    /**
     * Create the new Text object.
     *
     * @param text
     * @param font
     * @param color
     * @param position
     * @param engine
     */
    public Text(String text, Font font, Color color, Vector position, ParentEngine engine)
    {
        super(engine);
        this.text = text;
        this.font = font;
        this.color = color;
        this.center = Center.None;
        this.setPosition(position);
    }

    /**
     * Render the text out to the screen.
     *
     * @param graphics
     * @param screen
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> screen)
    {
        Font resetFont = graphics.getFont();
        Color resetColor = graphics.getColor();


        graphics.setFont(this.font);
        graphics.setColor(this.color);

        switch (this.center) {
            case Horizontally :
                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    (
                        this.getParentEngine().getWindowSize().getWidth() / 2
                    ) - (
                        graphics.getFontMetrics().stringWidth(text) / 2
                    ),
                    this.getPosition().getY()
                );
                break;
            case Vertically :
                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    this.getPosition().getX(),
                    (
                        this.getParentEngine().getWindowSize().getHeight() / 2
                    ) - (
                        graphics.getFontMetrics().getHeight() / 2
                    )
                );
                break;
            case None :
                graphics.drawChars(
                    this.text.toCharArray(),
                    0,
                    this.text.length(),
                    this.getPosition().getX(),
                    this.getPosition().getY()
                );
                break;
        }

        graphics.setFont(resetFont);
        graphics.setColor(resetColor);
    }

    /**
     * Retrieve the current text.
     *
     * @return
     */
    public final String getText()
    {
        return text;
    }

    /**
     * Update the current text.
     * @param text
     */
    public final void setText(String text)
    {
        this.text = text;
    }

    /**
     * Retrieve the current font.
     *
     * @return
     */
    public final Font getFont()
    {
        return this.font;
    }

    /**
     * Update the current Font.
     *
     * @param font
     */
    public final void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * Retrieve the current color.
     *
     * @return
     */
    public final Color getColor()
    {
        return color;
    }

    /**
     * Update the current Color.
     *
     * @param color
     */
    public final void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Retrieve the current Center constraint.
     *
     * @return
     */
    public final Center getCenter()
    {
        return center;
    }

    /**
     * Update the current position and Center constraint.
     *
     * @param center
     * @param position
     */
    public final void setPosition(Center center, Vector position)
    {
        this.center = center;
        this.setPosition(position);
    }
}
