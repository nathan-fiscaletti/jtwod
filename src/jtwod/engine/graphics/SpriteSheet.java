package jtwod.engine.graphics;

/**
 * A SpriteSheet object.
 * @author Nathan
 *
 */
public final class SpriteSheet {

    /**
     * The base texture.
     */
    private Texture texture;

    /**
     * The width of each column.
     */
    private int colWidth;

    /**
     * The height of each row.
     */
    private int rowHeight;

    /**
     * Create a SpriteSheet from a Texture.
     *
     * @param texture
     * @param colWidth
     * @param rowHeight
     */
    public SpriteSheet(Texture texture, int colWidth, int rowHeight){
        this.texture = texture;
        this.colWidth = colWidth;
        this.rowHeight = rowHeight;
    }

    /**
     * Pull a Texture from the SpriteSheet.
     *
     * @param col
     * @param row
     * @return
     */
    public Texture getTexture(int col, int row){
        return this.texture.getSubTexture (
                ( col-1 ) * this.colWidth,
                ( row-1 ) * this.rowHeight,
                this.colWidth,
                this.rowHeight
        );
    }

}
