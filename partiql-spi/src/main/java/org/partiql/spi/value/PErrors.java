package org.partiql.spi.value;

import org.jetbrains.annotations.NotNull;
import org.partiql.spi.errors.PError;
import org.partiql.spi.errors.PRuntimeException;
import org.partiql.spi.errors.PErrorKind;
import org.partiql.spi.errors.Severity;
import org.partiql.spi.types.PType;

import java.util.HashMap;

/**
 * TO REMAIN PACKAGE-PRIVATE
 */
class PErrors {

    @NotNull
    static PRuntimeException numericValueOutOfRangeException(@NotNull String value, @NotNull PType type) {
        PError pError = new PError(
                PError.NUMERIC_VALUE_OUT_OF_RANGE,
                Severity.ERROR(),
                PErrorKind.EXECUTION(),
                null,
                new HashMap<String, Object>() {{
                    put("VALUE", value);
                    put("TYPE", type);
                }}
        );
        return new PRuntimeException(pError);
    }

    @NotNull
    static PRuntimeException wrappedException(@NotNull Throwable t) {
        PError pError = new PError(
                PError.INTERNAL_ERROR,
                Severity.ERROR(),
                PErrorKind.EXECUTION(),
                null,
                new HashMap<String, Object>() {{
                    put("CAUSE", t);
                }}
        );
        return new PRuntimeException(pError);
    }
}
