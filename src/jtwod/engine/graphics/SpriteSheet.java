package jtwod.engine.graphics;

/**
 * A class for representing a Texture with several sub Textures.
 */
public final class SpriteSheet
{

    /**
     * The base texture for the SpriteSheet.
     */
    private Texture texture;

    /**
     * The width of each column on the SpriteSheet.
     */
    private int colWidth;

    /**
     * The height of each row on the SpriteSheet.
     */
    private int rowHeight;

    /**
     * Create a SpriteSheet from a Texture.
     *
     * @param texture The base Texture for the SpriteSheet.
     * @param colWidth The width of each column on the SpriteSheet.
     * @param rowHeight The height of each column on the SpriteSheet.
     */
    public SpriteSheet(Texture texture, int colWidth, int rowHeight)
    {
        this.texture = texture;
        this.colWidth = colWidth;
        this.rowHeight = rowHeight;
    }

    /**
     * Pull a Texture from the SpriteSheet.
     *
     * @param col The column to pull from.
     * @param row The row to pull from.
     * @return The Texture at the specified column and row.
     */
    public final Texture getTexture(int col, int row)
    {
        return this.texture.getSubTexture (
            ( col-1 ) * this.colWidth,
            ( row-1 ) * this.rowHeight,
            this.colWidth,
            this.rowHeight
        );
    }

    /**
     * Retrieves a Texture from the SpriteSheet.
     *
     * @param col The starting Column.
     * @param row The starting Row.
     * @param divergeX The divergence from the specified column parameter.
     * @param divergeY The divergence from the specified row parameter.
     * @param width The width of the Texture to retrieve.
     * @param height The height of the Texture to retrieve.
     *
     * @return The sub Texture from this SpriteSheet.
     */
    public final Texture getTextureStartingAt(int col, int row, int divergeX, int divergeY, int width, int height)
    {
        return this.texture.getSubTexture(
            (
                col*this.colWidth
            ) + divergeX,
            (
                row*this.rowHeight
            ) + divergeY,
            width,
            height
        );
    }
}
