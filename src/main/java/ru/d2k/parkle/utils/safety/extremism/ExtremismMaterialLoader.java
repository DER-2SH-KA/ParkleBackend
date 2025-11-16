package ru.d2k.parkle.utils.safety.extremism;

import java.util.Set;

public abstract sealed class ExtremismMaterialLoader permits ExtremismCSVLoader {
    public abstract Set<String> getExtremismLinks();
}
