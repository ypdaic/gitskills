package com.daiyanping.cms;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(ImportSelectorTest.class)
public @interface EnableImportSelectorTest {

    int testValue();
}
