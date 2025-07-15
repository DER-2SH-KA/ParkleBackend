package ru.d2k.parkle.utils.generator;

import com.fasterxml.uuid.Generators;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/** Class for generate UUID values. **/
public class Uuid7Generator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }

    /**
     * Generate new UUID by UUID V7.
     * @return Generated UUID V7 value.
     * **/
    public static UUID generateNewUUID() { return Generators.timeBasedEpochRandomGenerator().generate(); }
}
