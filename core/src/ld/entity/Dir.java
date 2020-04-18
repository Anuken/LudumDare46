package ld.entity;

public enum Dir{
    left(true, ""),
    right(false, ""),
    up(false, "-back"),
    down(false, "-front");

    public final boolean flip;
    public final String suffix;

    Dir(boolean flip, String suffix){
        this.flip = flip;
        this.suffix = suffix;
    }
}
