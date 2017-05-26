package com.psa.kiosk.network.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Model for the item.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
@Root(strict = false)
data class ItemModel(
        @field:Element(required = false) var title : String = "",
        @field:Element(required = false) var link : String = "",
        @field:Element(required = false) var description : String = "",
        @field:Element(required = false) var pubDate : String = "",
        @field:Element(required = false) var enclosure : EnclosureModel = EnclosureModel("", "0", "")
)
