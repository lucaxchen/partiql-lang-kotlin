package org.partiql.cli

import org.partiql.spi.errors.PError

/**
 * This is used by PicoCLI for de-serializing user-input for converting warnings to errors.
 *
 * @see MainCommand.warningsAsErrors
 */
enum class ErrorCodeString(val code: Int) {
    ALL(-1),
    UNKNOWN(PError.UNKNOWN),
    INTERNAL_ERROR(PError.INTERNAL_ERROR),
    UNRECOGNIZED_TOKEN(PError.UNRECOGNIZED_TOKEN),
    UNEXPECTED_TOKEN(PError.UNEXPECTED_TOKEN),
    PATH_KEY_NEVER_SUCCEEDS(PError.PATH_KEY_NEVER_SUCCEEDS),
    PATH_SYMBOL_NEVER_SUCCEEDS(PError.PATH_SYMBOL_NEVER_SUCCEEDS),
    PATH_INDEX_NEVER_SUCCEEDS(PError.PATH_INDEX_NEVER_SUCCEEDS),
    FEATURE_NOT_SUPPORTED(PError.FEATURE_NOT_SUPPORTED),
    FUNCTION_NOT_FOUND(PError.FUNCTION_NOT_FOUND),
    FUNCTION_TYPE_MISMATCH(PError.FUNCTION_TYPE_MISMATCH),
    UNDEFINED_CAST(PError.UNDEFINED_CAST),
    VAR_REF_AMBIGUOUS(PError.VAR_REF_AMBIGUOUS),
    VAR_REF_NOT_FOUND(PError.VAR_REF_NOT_FOUND),
    ALWAYS_MISSING(PError.ALWAYS_MISSING),
}
