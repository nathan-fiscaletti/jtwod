package jtwod.engine.metrics;

public final class AspectRatio {
    
    /**
     * The aspect ratio.
     */
    private Dimensions ratio;
    
    /**
     * The control Vector for the aspect ratio.
     */
    private AspectRatioControlAxis control;
    
    /**
     * Enumeration for which axis controls the aspect ratio.
     * 
     * @author Nathan
     */
    public enum AspectRatioControlAxis {
        WidthControlled,
        HeightControlled
    }
    
    /**
     * Construct the AspectRatio.
     *
     * @param ratio The ratio to use.
     * @param control The control axis.
     */
    public AspectRatio(Dimensions ratio, AspectRatioControlAxis control)
    {
        this.ratio = ratio;
        this.control = control;
    }
    
    /**
     * Retrieve the ratio.
     *
     * @return
     */
    public final Dimensions getRatio()
    {
        return this.ratio;
    }
    
    /**
     * Update the ratio.
     *
     * @param ratio
     */
    public final void setRatio(Dimensions ratio)
    {
        this.ratio = ratio;
    }
    
    /**
     * Retrieve the AspectRatioControlAxis.
     *
     * @return
     */
    public final AspectRatioControlAxis getControlAxis()
    {
        return this.control;
    }
    
    /**
     * Update the AspectRatioControlAxis.
     * @param control
     */
    public final void setControlAxis(AspectRatioControlAxis control)
    {
        this.control = control;
    }
}
