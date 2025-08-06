package codebase.pathing;

import codebase.Loop;
import codebase.geometry.FieldPosition;

public interface Localizer extends Loop {

    FieldPosition getCurrentPosition();

    FieldPosition init(FieldPosition initialPosition);
}
