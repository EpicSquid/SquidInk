package dev.epicsquid.squidink.config

import net.minecraftforge.common.ForgeConfigSpec.Builder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun Builder.stack(path: String, block: Builder.() -> Unit) {
	contract {
		callsInPlace(block, EXACTLY_ONCE)
	}
	push(path)
	block()
	pop()
}