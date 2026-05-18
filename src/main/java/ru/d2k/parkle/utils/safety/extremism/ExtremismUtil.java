package ru.d2k.parkle.utils.safety.extremism;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.HashSet;

@Slf4j
@Component
public class ExtremismUtil {

    private final Set<String> links = new HashSet<>();

    @Autowired
    private final ExtremismMaterialLoader extremismLoader;

    /**
     * Extremism util class. Must have loader class.
     * @param extremismLoader {@link ExtremismMaterialLoader} realization class by load extremism links.
     * */
    public ExtremismUtil(ExtremismMaterialLoader extremismLoader) {
        this.extremismLoader = extremismLoader;
        this.loadExtremismLinks();
    }

    public boolean checkOnExtremism(String websiteUrl) {
        boolean isExtremismLink = links.contains(websiteUrl);

        if (isExtremismLink) {
            log.info("{} is extremism website!", websiteUrl);
        }

        return isExtremismLink;
    }

    private void loadExtremismLinks() {
        links.addAll(extremismLoader.getExtremismLinks());
    }
}