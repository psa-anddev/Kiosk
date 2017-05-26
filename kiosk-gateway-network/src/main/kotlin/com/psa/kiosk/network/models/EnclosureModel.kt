package com.psa.kiosk.network.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element

/**
 * Model for the enclosure.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
data class EnclosureModel (
        @field:Attribute var url : String = "",
        @field:Attribute var length : String = "0",
        @field:Attribute var type : String = ""
)