package jtwod.engine.graphics;

import jtwod.engine.metrics.Dimensions;

import java.util.HashMap;

/**
 * Class used to manage TextureGroup for a game.
 * @author Nathan
 */
public final class TextureGroup
{
    /**
     * The Textures.
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
     * Retrieve a Texture.
     *
     * @param textureName
     */
    public final Texture getTexture(String textureName)
    {
        return this.textures.containsKey(textureName)
            ? this.textures.get(textureName)
            : Texture.unknownTexture(new Dimensions(32, 32));
    }

    /**
     * Set a Texture.
     *
     * @param name
     * @param texture
     */
    public final void addTexture(String name, Texture texture)
    {
        this.textures.put(name, texture);
    }
}
