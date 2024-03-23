package dev.epicsquid.squidink.data

import net.minecraft.world.level.ItemLike
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.registries.ForgeRegistries

fun ItemModelProvider.block(item: ItemLike) {
	withExistingParent(
		name(item),
		modLoc("block/${name(item)}")
	)
}

fun ItemModelProvider.name(item: ItemLike): String {
	return ForgeRegistries.ITEMS.getKey(item.asItem())!!.path
}