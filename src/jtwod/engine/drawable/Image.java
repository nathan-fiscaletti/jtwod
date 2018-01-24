package jtwod.engine.drawable;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.graphics.Texture;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;

import java.awt.Graphics;

public final class Image<ParentEngine extends Engine> extends Shape<ParentEngine> {
    /**
     * The image to draw.
     */
    private Texture texture;

    /**
     * The center constraint to use.
     */
    private Center center;

    /**
     * Construct the Image.
     *
     * @param engine
     */
    public Image(Texture texture, Vector position, ParentEngine engine) {
        super(engine);
        this.center = Center.None;
        this.texture = texture;
        this.setPosition(position);
        this.setSize(new Dimensions(texture.getWidth(), texture.getHeight()));
    }

    /**
     * Render the image out.
     *
     * @param graphics
     * @param screen
     */
    @Override
    public final void render(Graphics graphics, Scene<ParentEngine> screen)
    {
        switch (this.center) {
            case Vertically :
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
            case Horizontally :
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
            case None :
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
     * Update the texture.
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
     * Retrieve the image.
     *
     * @return
     */
    public final Texture getTexture()
    {
        return this.texture;
    }

    /**
     * Retrieve the current Center constraint.
     *
     * @return
     */
    public final Center getCenter() {
        return center;
    }

    /**
     * Update the current position and Center constraint.
     *
     * @param center
     * @param position
     */
    public final void setPosition(Center center, Vector position) {
        this.center = center;
        this.setPosition(position);
    }

}
