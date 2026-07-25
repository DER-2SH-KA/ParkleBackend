package ru.d2k.parkle.utils.generator;

import com.fasterxml.uuid.Generators;
import java.util.UUID;

public class UuidGeneratorUtil {

    public static UUID generateNewUuidV7() {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }
}