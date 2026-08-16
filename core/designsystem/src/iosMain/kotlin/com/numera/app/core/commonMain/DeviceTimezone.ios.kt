package com.numera.app.core.commonMain

import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.name

// Unverified on-device - no Mac/Xcode toolchain available in this environment to compile or run
// the iOS target (same limitation noted for Info.plist elsewhere in this project). NSTimeZone
// .localTimeZone.name is the commonly documented way to read the IANA id in Kotlin/Native; if it
// doesn't resolve as written, the fallback is NSTimeZone.localTimeZone.name() as a function call.
actual fun deviceTimezoneId(): String = NSTimeZone.localTimeZone.name
