package jtwod.engine.drawable;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.Graphics;

/**
 * An Image that can be rendered out as a Drawable.
 * 
 * @param <ParentEngine> The ParentEngine type for this Image.
 */
public class Image<ParentEngine extends Engine> extends Shape<ParentEngine>
{
    /**
     * The Texture to draw.
     */
    private Texture texture;

    /**
     * The center constraint to use.
     */
    private Center center;

    /**
     * Construct the Image and default to Center.None
     *
     * @param texture The Texture to use for the Image.
     * @param position The initial Position to use for the Image.
     * @param engine The parent Engine for this Image.
     */
    public Image(int layer, Texture texture, Vector position, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.center = Center.None;
        this.texture = texture;
        this.setPosition(position);
        this.setSize(new Dimensions(texture.getWidth(), texture.getHeight()));
    }
    
    /**
     * Construct the Image and default to Center.Parent.
     *
     * @param texture The Texture to use for the Image.
     * @param engine The parent Engine for this Image.
     */
    public Image(int layer, Texture texture, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.center = Center.Parent;
        this.texture = texture;
        this.setPosition(Vector.Zero());
        this.setSize(new Dimensions(texture.getWidth(), texture.getHeight()));
    }
    
    /**
     * Construct the Image.
     *
     * @param texture The Texture to use for the Image.
     * @param center The Drawable.Center value to use.
     * @param position The position to use along with the Drawable.Center value.
     * @param engine The parent Engine for this Image.
     */
    public Image(int layer, Texture texture, Center center, Vector position, ParentEngine engine, Scene<ParentEngine> scene)
    {
        super(layer, engine, scene);
        this.texture = texture;
        this.center = center;
        this.setPosition(position);
    }

    /**
     * Render the Image out.
     *
     * @param graphics
     * @param screen
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> screen)
    {
        super.render(graphics, screen);

        switch (this.center) {
            case Vertically:
                graphics.drawImage(
                        this.texture.asBufferedImage(),
                        this.getPosition().getX(),
                        (
                                this.getParentEngine().getWindowSize().getHeight() / 2
                        ) - (
                                this.getSize().getHeight() / 2
                        ),
                        screen
                );
                break;
            case Horizontally:
                graphics.drawImage(
                        this.texture.asBufferedImage(),
                        (
                                this.getParentEngine().getWindowSize().getWidth() / 2
                        ) - (
                                this.getSize().getWidth() / 2
                        ),
                        this.getPosition().getY(),
                        screen
                );
                break;
            case Parent:
                graphics.drawImage(
                        this.texture.asBufferedImage(),
                        (
                                this.getParentEngine().getWindowSize().getWidth() / 2
                        ) - (
                                this.getSize().getWidth() / 2
                        ),
                        (
                                this.getParentEngine().getWindowSize().getHeight() / 2
                        ) - (
                                this.getSize().getHeight() / 2
                        ),
                        screen
                );
                break;
            case None:
                graphics.drawImage(
                        this.texture.asBufferedImage(),
                        this.getPosition().getX(),
                        this.getPosition().getY(),
                        screen
                );
                break;
        }
    }

    /**
     * Update the Texture for this Image.
     *
     * @param texture
     */
    public final void setTexture(Texture texture)
    {
        this.texture = (texture != null)
            ? texture
            : Texture.unknownTexture(new Dimensions(32, 32));

        this.setSize(
            new Dimensions(this.texture.getWidth(), this.texture.getHeight())
        );
    }

    /**
     * Retrieve the Texture for this Image.
     *
     * @return
     */
    public final Texture getTexture()
    {
        return (this.texture != null) 
            ? this.texture
            : Texture.unknownTexture(new Dimensions(32, 32));
    }

    /**
     * Retrieve the current Center constraint for this Image.
     *
     * @return
     */
    public final Center getCenter()
    {
        return center;
    }

    /**
     * Update the current position and Center constraint for this Image.
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
