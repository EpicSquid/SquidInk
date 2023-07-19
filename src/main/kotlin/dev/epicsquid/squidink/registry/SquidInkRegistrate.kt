package dev.epicsquid.squidink.registry

import com.tterrag.registrate.AbstractRegistrate

class SquidInkRegistrate private constructor(modid: String) : AbstractRegistrate<SquidInkRegistrate>(modid) {

	companion object {
		fun create(modid: String) = SquidInkRegistrate(modid).apply {
			registerEventListeners(modEventBus)
		}
	}
}