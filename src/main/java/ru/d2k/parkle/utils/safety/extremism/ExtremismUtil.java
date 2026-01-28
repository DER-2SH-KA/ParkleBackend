package ru.d2k.parkle.utils.safety.extremism;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ExtremismUtil {
    private final ExtremismMaterialLoader extremismLoader;
    private final Set<String> links = new HashSet<>();

    /**
     * Extremism util class. Must have loader class.
     * @param extremismLoader {@link ExtremismMaterialLoader} realization class by load extremism links.
     * */
    public ExtremismUtil(ExtremismMaterialLoader extremismLoader) {
        this.extremismLoader = extremismLoader;

        this.loadExtremismLinks();
    }

    public boolean checkOnExtremism(String websiteUrl) {
        Set<String> foundedLinksLikeWebsite = links.parallelStream()
                .filter(websiteUrl::contains)
                .collect(Collectors.toSet());

        System.out.println(Arrays.toString(foundedLinksLikeWebsite.toArray()));

        return !foundedLinksLikeWebsite.isEmpty();
    }

    private void loadExtremismLinks() {
        links.addAll(extremismLoader.getExtremismLinks());
    }
}
