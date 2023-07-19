package dev.epicsquid.squidink.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.TranslatableContents

/**
 * Returns a [MutableComponent] with the given arguments applied if the component is a [TranslatableContents].
 * Otherwise, returns the component as-is.
 * @param args The arguments to apply to the component.
 * @return The component with the arguments applied, or the component as-is.
 */
fun MutableComponent.withArgs(vararg args: Any?): MutableComponent =
	if (contents is TranslatableContents) {
		Component.translatable((contents as TranslatableContents).key, *args)
	} else this