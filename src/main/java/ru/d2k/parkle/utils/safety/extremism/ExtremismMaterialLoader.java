package ru.d2k.parkle.utils.safety.extremism;

import java.util.Set;

import java.nio.charset.Charset;

public sealed abstract class ExtremismMaterialLoader permits ExtremismCSVLoader {
    abstract Set<String> getExtremismLinks(String fileName, Charset charset);
}
