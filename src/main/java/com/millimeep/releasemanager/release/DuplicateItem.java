package com.millimeep.releasemanager.release;

import com.millimeep.releasemanager.build.Build;
import com.millimeep.releasemanager.component.ReleaseComponent;

public class DuplicateItem extends RuntimeException {

    public DuplicateItem(String message) {
        super(message);
    }

    public static DuplicateItem duplicateComponent(ReleaseComponent releaseComponent) {
        return new DuplicateItem("I've already got %s".formatted(
                releaseComponent.getName()));
    }

    public static DuplicateItem duplicateBuild(Build build) {
        return new DuplicateItem("Already got %s".formatted(build));
    }
}
