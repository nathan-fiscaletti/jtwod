package jtwod.engine.graphics;

import jtwod.engine.metrics.Dimensions;

import java.util.HashMap;

/**
 * Class used to manage Textures for a game.
 */
public final class TextureGroup
{
    /**
     * The Textures stored in this TextureGroup.
     */
    private HashMap<String, Texture> textures;

    /**
     * Construct the TextureGroup.
     */
    public TextureGroup()
    {
        this.textures = new HashMap<>();
    }

    /**
     * Retrieve a Texture from the TextureGroup.
     *
     * @param textureName The name of the Texture to retrieve.
     * @return The Texture associated with the specified name.
     */
    public final Texture getTexture(String textureName)
    {
        return this.textures.containsKey(textureName)
            ? this.textures.get(textureName)
            : Texture.unknownTexture(new Dimensions(32, 32));
    }

    /**
     * Add a Texture to the TextureGroup.
     *
     * @param name The name of the Texture to add.
     * @param texture The Texture to add.
     */
    public final void addTexture(String name, Texture texture)
    {
        this.textures.put(name, texture);
    }
}
