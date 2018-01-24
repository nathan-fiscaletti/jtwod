package jtwod.engine.graphics;

import jtwod.engine.metrics.Dimensions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Arrays;

public final class Texture
{
    /**
     * The base image for this texture.
     */
    private BufferedImage image;

    /**
     * Create a new texture.
     *
     * @param path
     */
    public Texture(String path)
    {
        try {
            this.image = ImageIO.read(
                (new Object()).getClass().getResource(path)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct the Texture.
     *
     * @param image
     */
    public Texture(BufferedImage image)
    {
        this.image = image;
    }

    /**
     * Get this Texture as a BufferedImage.
     *
     * @return
     */
    public final BufferedImage asBufferedImage()
    {
        return this.image;
    }

    /**
     * Get a sub texture from this Texture.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public final Texture getSubTexture(int x, int y, int width, int height)
    {
        return new Texture(image.getSubimage(x, y, width, height));
    }

    /**
     * Retrieve a black texture of the defined size.
     *
     * @param size
     * @return
     */
    public final static Texture blackTexture(Dimensions size)
    {
        return new Texture(new BufferedImage (
                size.getWidth(),
                size.getHeight(),
                BufferedImage.TYPE_INT_RGB
        ));
    }

    /**
     * Retrieve a white texture of the defined size.
     *
     * @param size
     * @return
     */
    public final static Texture whiteTexture(Dimensions size)
    {
        BufferedImage image = blackTexture(size).asBufferedImage();
        int[]data=((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        Arrays.fill(data, Color.WHITE.getRGB());

        return new Texture(image);
    }

    /**
     * Retrieve the placeholder Texture for the unknown texture.
     *
     * @param size
     * @return
     */
    public final static Texture unknownTexture(Dimensions size)
    {
        BufferedImage image = blackTexture(size).asBufferedImage();

        Graphics graphics = image.getGraphics();
        graphics.setColor( Color.white );

        int squareSize = size.getWidth() / 5;

        int cols = size.getWidth() / squareSize;
        int rows = size.getHeight() / squareSize;

        int startCol = 0;
        for ( int row = 0; row < rows; row+=1 )
        {
            for ( int col = startCol ; col < cols ; col+=2 )
            {
                graphics.fillRect( col * squareSize, row * squareSize, squareSize, squareSize );
            }
            startCol = (startCol == 0) ? 1 : 0;
        }

        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ariel", Font.BOLD, size.getHeight()));
        graphics.drawChars("?".toCharArray(), 0, 1, (size.getWidth() / 2) - (graphics.getFontMetrics().stringWidth("?") / 2), size.getHeight() - (size.getHeight() / 4));

        return new Texture(image);
    }

    /**
     * Get the gray scale version of this Texture.
     *
     * @return
     */
    public final Texture asGrayScaleTexture()
    {
        BufferedImage ret = this.image;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        ret = op.filter(ret, null);

        return new Texture(ret);
    }

    /**
     * Retrieve the width.
     *
     * @return
     */
    public final int getWidth()
    {
        return this.image.getWidth();
    }

    /**
     * Retrieve the height.
     * @return
     */
    public final int getHeight()
    {
        return this.image.getHeight();
    }
}
