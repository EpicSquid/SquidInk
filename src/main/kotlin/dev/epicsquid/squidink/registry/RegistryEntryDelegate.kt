package dev.epicsquid.squidink.registry

import com.tterrag.registrate.util.entry.RegistryEntry
import java.util.function.Supplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RegistryEntryDelegate<V>(private val registryEntry: RegistryEntry<V>) : ReadOnlyProperty<Any, V>, Supplier<V>, () -> V {
	override fun getValue(thisRef: Any, property: KProperty<*>): V = registryEntry.get()
	override fun get(): V = registryEntry.get()
	override fun invoke(): V = registryEntry.get()
}

inline fun <reified V : Any> registryEntry(
	supplier: () -> RegistryEntry<V>
) = RegistryEntryDelegate(supplier())