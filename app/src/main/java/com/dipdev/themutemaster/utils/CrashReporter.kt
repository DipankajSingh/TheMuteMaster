package com.dipdev.themutemaster.utils

/**
 * A centralized interface for crash reporting and silent error logging.
 *
 * Use this to:
 *  - [log]           Leave a breadcrumb so we know what happened before an error.
 *  - [recordNonFatal] Capture a caught (silent) exception without crashing the app.
 *  - [setKey]         Attach custom key-value state to every report.
 */
interface CrashReporter {
    fun log(message: String)
    fun recordNonFatal(throwable: Throwable, context: String = "")
    fun setKey(key: String, value: String)
    fun setKey(key: String, value: Boolean)
    fun setKey(key: String, value: Int)
}
