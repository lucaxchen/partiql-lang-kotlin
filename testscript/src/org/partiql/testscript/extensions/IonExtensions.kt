package org.partiql.testscript.extensions

import com.amazon.ion.*

internal fun IonValue.toIonText(): String {
    val sb = StringBuilder() 
    this.system.newTextWriter(sb).use { w -> this.writeTo(w) }
    
    return sb.toString()
}