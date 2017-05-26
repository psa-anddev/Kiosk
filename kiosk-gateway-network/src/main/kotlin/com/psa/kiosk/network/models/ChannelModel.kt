package com.psa.kiosk.network.models

import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

/**
 * Model for the channel tag.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
@Root
@Convert(ChannelModelConverter::class)
data class ChannelModel(
        val title: String = "",
        val link: String = "",
        val description: String = "",
        val image: ImageModel = ImageModel(""),
        val items: List<ItemModel> = emptyList()
)

class ChannelModelConverter : Converter<ChannelModel> {
    private val serializer = Persister(AnnotationStrategy())

    override fun read(node: InputNode?): ChannelModel =
            readChannel(node?.next, node)


    private fun readChannel(node: InputNode?, parent: InputNode?, channelModel: ChannelModel = ChannelModel()): ChannelModel =
            if (node != null) {
                val tempChannel = convertNodeToProperty(node, channelModel)
                readChannel(parent?.next, parent, tempChannel)
            } else
                channelModel

    private fun convertNodeToProperty(node: InputNode, channelModel: ChannelModel): ChannelModel =
            when (node.name) {
                "title" -> channelModel.copy(title = node.value ?: "")
                "link" -> channelModel.copy(link = node.value ?: "")
                "description" -> channelModel.copy(description = node.value ?: "")
                "item" -> channelModel.copy(items = channelModel.items + serializer.read(ItemModel::class.java, node))
                "image" ->
                    if (node.prefix == "")
                        channelModel.copy(image = serializer.read(ImageModel::class.java, node))
                    else
                        channelModel
                else -> channelModel.copy()
            }


    override fun write(node: OutputNode?, value: ChannelModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
