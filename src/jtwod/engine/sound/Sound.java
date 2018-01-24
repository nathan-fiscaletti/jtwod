package jtwod.engine.sound;

import jtwod.engine.Engine;

/**
 * Wrapper class for tinysound Sound class.
 * @author Nathan
 *
 */
public final class Sound 
{

    /**
     * The tinysound Sound object to wrap around.
     */
    private kuusisto.tinysound.Sound sound;

    /**
     * Create a new sound object from a WAV file.
     * @param url
     */
    public Sound(String url)
    {
        this.sound = kuusisto.tinysound.TinySound.loadSound(url);
    }

    /**
     * Play the sound.
     * @param engine
     */
    public final void play(Engine engine)
    {
        if (! engine.isMuted()) {
            sound.play();
        }
    }
}
