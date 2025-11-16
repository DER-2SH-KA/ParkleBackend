package ru.d2k.parkle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.d2k.parkle.utils.safety.extremism.ExtremismCSVLoader;
import ru.d2k.parkle.utils.safety.extremism.ExtremismMaterialLoader;

import java.nio.charset.StandardCharsets;

@Configuration
public class ExtremismLoaderConfig {
    @Bean
    public ExtremismMaterialLoader extremismLoader() {
        return new ExtremismCSVLoader(
                "exportfsm_fixed.csv",
                StandardCharsets.UTF_8,
                ";"
        );
    }
}
