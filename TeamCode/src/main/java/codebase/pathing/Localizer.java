package codebase.pathing;

import codebase.Loop;
import codebase.geometry.FieldPosition;

public interface Localizer extends Loop {

    public FieldPosition getCurrentPosition();

    public FieldPosition init(FieldPosition initialPosition);
}
