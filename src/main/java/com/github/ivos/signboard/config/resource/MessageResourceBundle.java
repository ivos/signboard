package com.github.ivos.signboard.config.resource;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Target({ TYPE, FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface MessageResourceBundle {
}
