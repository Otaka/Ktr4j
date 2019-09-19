package com.kotor4j.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Dmitry
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordTag {
    public int value();
}
