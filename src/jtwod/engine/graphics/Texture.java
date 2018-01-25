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

/**
 * Class for representing a Texture.
 */
public final class Texture
{
    /**
     * The base BufferedImage for this Texture.
     */
    private BufferedImage image;

    /**
     * Create a new Texture.
     *
     * @param path The path to the image on disk.
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
     * @param image The BufferedImage object to use as a base for the Texture.
     */
    public Texture(BufferedImage image)
    {
        this.image = image;
    }

    /**
     * Get this Texture as a BufferedImage.
     *
     * @return This Texture as a BufferedImage.
     */
    public final BufferedImage asBufferedImage()
    {
        return this.image;
    }

    /**
     * Get a sub Texture from this Texture.
     *
     * @param x The x coordinate in which to look for the sub Texture.
     * @param y The y coordinate in which to look for the sub Texture.
     * @param width The width of the Texture you'd like to pull.
     * @param height The height of the Texture you'd like to pull.
     * @return The sub Texture .
     */
    public final Texture getSubTexture(int x, int y, int width, int height)
    {
        return new Texture(image.getSubimage(x, y, width, height));
    }

    /**
     * Get the gray scale version of this Texture.
     *
     * @return The gray scale version of this Texture.
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
     * @return The width of this Texture.
     */
    public final int getWidth()
    {
        return this.image.getWidth();
    }

    /**
     * Retrieve the height.
     * 
     * @return The height of this Texture.
     */
    public final int getHeight()
    {
        return this.image.getHeight();
    }
    
    /**
     * Retrieve a Texture of a specific color and size.
     *
     * @param colo The color of the Texture.
     * @param size The size of the Texture.
     * @return A Texture of the specified Color and Size.
     */
    public final static Texture colorTexture(Color color, Dimensions size)
    {
        BufferedImage image = new BufferedImage (
                size.getWidth(),
                size.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        
        int[]data=((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        Arrays.fill(data, color.getRGB());

        return new Texture(image);
    }

    /**
     * Retrieve the placeholder Texture for the unknown texture.
     *
     * @param size The size of the Texture.
     * @return The placeholder Texture for unknown Textures of the specified size.
     */
    public final static Texture unknownTexture(Dimensions size)
    {
        BufferedImage image = Texture.colorTexture(Color.BLACK, size).asBufferedImage();

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
        graphics.drawChars(
            "?".toCharArray(), 
            0, 1, 
            (size.getWidth() / 2) 
          - (graphics.getFontMetrics().stringWidth("?") / 2), 
            size.getHeight() 
          - (size.getHeight() / 4)
        );

        return new Texture(image);
    }
}
