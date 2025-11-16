package ru.d2k.parkle.utils.safety.extremism;

import java.util.Set;
import java.util.HashSet;

public class ExtremismUtil {
    private final ExtremismMaterialLoader loader;
    private final Set<String> links = new HashSet<>();

    /**
     * Extremism util class. Must have loader class.
     * @param loader {@link ExtremismMaterialLoader} realization class by load extremism links.
     * */
    public ExtremismUtil(ExtremismMaterialLoader loader) {
        this.loader = loader;

        this.loadExtremismLinks();
    }

    public boolean checkOnExtremism(String websiteUrl) {
        return links.contains(websiteUrl);
    }

    private void loadExtremismLinks() {
        links.addAll(loader.getExtremismLinks());
    }
}
