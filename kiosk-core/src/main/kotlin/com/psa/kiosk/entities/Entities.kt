package com.psa.kiosk.entities

/**
 * This file defines all existing entities.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */

data class Channel(val title : String, val link : String, val description: String, val image : String, val items : List<Item>)

data class Item(val title: String, val link : String, val description: String, val enclosure: Enclosure, val pubDate : Long)

data class Enclosure(val url : String, val length : Long, val type : String)
