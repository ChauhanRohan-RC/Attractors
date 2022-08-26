package attractors;

import math.Vector;

import org.jetbrains.annotations.NotNull;

public interface AttractorI {

    @NotNull
    String getTitle();

    @NotNull
    Vector getStart();

    @NotNull
    Vector calculateNextPoint(@NotNull Vector v, float dt);

    @NotNull
    DrawConfig drawConfig();

}
