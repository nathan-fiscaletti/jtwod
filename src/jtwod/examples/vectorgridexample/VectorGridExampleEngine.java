package jtwod.examples.vectorgridexample;

import jtwod.engine.Engine;
import jtwod.engine.Scene;
import jtwod.engine.drawable.Text;
import jtwod.engine.metrics.Dimensions;
import jtwod.engine.metrics.Vector;
import jtwod.engine.metrics.VectorGrid;

import java.awt.*;

public class VectorGridExampleEngine extends Engine {

    // The size of the total screen.
    // Must be divisible by gridElementSize and be divisible by 2.
    public static final int screenSize = 800;

    // The size of a single grid element in the VectorGrid.
    // Must be divisible by 2.
    public static final int gridElementSize = 100;


    public static void main(String[] args)
    {
        new VectorGridExampleEngine().start();
    }

    public VectorGridExampleEngine() {
        super("Vector Grid Example", Dimensions.Square(screenSize));
    }

    @Override
    public void onEngineStart() {
        this.setScene(new Scene<VectorGridExampleEngine>("MainScene", this){
            VectorGrid<VectorGridExampleEngine> grid;
            Text<VectorGridExampleEngine> text;

            @Override
            public void allocate()
            {
                // Initialize the new VectorGrid with padding along the outside
                // equal to one half of a single grid element.
                grid = new VectorGrid<>(
                    Vector.Zero().plusY(gridElementSize / 2).plusX(gridElementSize / 2),
                    Dimensions.Square(gridElementSize),
                    Dimensions.Square(screenSize).plus(-gridElementSize, -gridElementSize),
                    this.getParentEngine(), this
                );

                // Get the center most grid element.
                Vector textParentStart = grid.getVectorAtGridPoint(
                    (screenSize / gridElementSize) / 2,
                    (screenSize / gridElementSize) / 2
                );

                // Initialize the Text using the center most grid element
                // as a starting point for it's parent and the size of
                // a grid element as it's parent Dimensions.
                //
                // The parent starting point and Dimensions will be used
                // to properly center the Text object.
                text = new Text<>(
                    Integer.MAX_VALUE,
                    "Center",
                    new Font(
                        "System",
                        Font.BOLD,
                        24
                    ),
                    Color.white,
                    textParentStart,
                    Dimensions.Square(gridElementSize),
                    this.getParentEngine(),
                    this
                );

                // Tell the Scene to render the VectorGrid.
                grid.startRenderingTo(this);

                // Add the Text object to the Scene's Drawables.
                this.getDrawableGroup().addDrawable(text);
            }
        });
    }
}
