package com.psa.kiosk.network.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Model for the image tag.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
@Root(strict = false)
data class ImageModel(@field:Element var url : String = "")
