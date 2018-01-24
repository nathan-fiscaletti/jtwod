package jtwod.engine.sound;

/**
 * Wrapper class for tinysound Music class.
 * @author Nathan
 *
 */
public final class Music 
{
    /**
     * The music object we're wrapping around.
     */
    public kuusisto.tinysound.Music music;

    /**
     * Create a new Music object from a WAV file.
     * @param url
     */
    public Music(String url)
    {
        this.music = kuusisto.tinysound.TinySound.loadMusic(url);
    }

    /**
     * Play the music.
     */
    public final void play()
    {
        music.play(true, 0.25f);
    }

    /**
     * Pause the music.
     */
    public final void pause()
    {
        music.pause();
    }
}
