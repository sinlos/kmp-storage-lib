@file:Suppress("unused")

package world.chebur.kmp.storage

import kotlinx.serialization.Serializable

typealias KmpPrefsMap = Map<String, PreferenceValue>

@Serializable
sealed class PreferenceValue {
    @Serializable data class StringValue(val value: String) : PreferenceValue()
    @Serializable data class IntValue(val value: Int) : PreferenceValue()
    @Serializable data class LongValue(val value: Long) : PreferenceValue()
    @Serializable data class DoubleValue(val value: Double) : PreferenceValue()
    @Serializable data class BooleanValue(val value: Boolean) : PreferenceValue()
    @Serializable data class FloatValue(val value: Float) : PreferenceValue()

    // TODO: add serializable value

}

fun KmpPrefsMap.getString(key: String, defaultValue: String = ""): String =
    (this[key] as? PreferenceValue.StringValue)?.value ?: defaultValue

fun KmpPrefsMap.getInt(key: String, defaultValue: Int = 0): Int =
    (this[key] as? PreferenceValue.IntValue)?.value ?: defaultValue

fun KmpPrefsMap.getLong(key: String, defaultValue: Long = 0L): Long =
    (this[key] as? PreferenceValue.LongValue)?.value ?: defaultValue

fun KmpPrefsMap.getDouble(key: String, defaultValue: Double = 0.0): Double =
    (this[key] as? PreferenceValue.DoubleValue)?.value ?: defaultValue

fun KmpPrefsMap.getBoolean(key: String, defaultValue: Boolean = false): Boolean =
    (this[key] as? PreferenceValue.BooleanValue)?.value ?: defaultValue

fun KmpPrefsMap.getFloat(key: String, defaultValue: Float = 0f): Float =
    (this[key] as? PreferenceValue.FloatValue)?.value ?: defaultValue

fun KmpPrefsMap.withValue(key: String, value: String) = this + (key to PreferenceValue.StringValue(value))
fun KmpPrefsMap.withValue(key: String, value: Int) = this + (key to PreferenceValue.IntValue(value))
fun KmpPrefsMap.withValue(key: String, value: Long) = this + (key to PreferenceValue.LongValue(value))
fun KmpPrefsMap.withValue(key: String, value: Double) = this + (key to PreferenceValue.DoubleValue(value))
fun KmpPrefsMap.withValue(key: String, value: Boolean) = this + (key to PreferenceValue.BooleanValue(value))
fun KmpPrefsMap.withValue(key: String, value: Float) = this + (key to PreferenceValue.FloatValue(value))
