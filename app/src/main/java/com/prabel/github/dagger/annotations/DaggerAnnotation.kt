package com.prabel.github.dagger.annotations

import javax.inject.Qualifier
import kotlin.annotation.AnnotationTarget.*

object DaggerAnnotation {

    @Qualifier
    @Target(VALUE_PARAMETER, FIELD, CONSTRUCTOR, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ForApplication

    @Qualifier
    @Target(VALUE_PARAMETER, FIELD, CONSTRUCTOR, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ForActivity

}
