package ru.d2k.parkle.utils.safety.extremism;

import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public interface ExtremismMaterialLoader {

    Set<String> getExtremismLinks();
}