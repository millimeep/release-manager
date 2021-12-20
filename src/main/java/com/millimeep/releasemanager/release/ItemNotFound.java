package com.millimeep.releasemanager.release;

public class ItemNotFound extends RuntimeException {
    public ItemNotFound(Long id) {
        super("Couldn't find release component %d".formatted(id));
    }

    public ItemNotFound(String message) {
        super(message);
    }

    public static ItemNotFound componentNotFound(Long id) {
        return new ItemNotFound("Couldn't find release component %d".formatted(id));
    }

    public static ItemNotFound buildNotFound(Long id) {
        return new ItemNotFound("Couldn't find build %d".formatted(id));
    }
}
