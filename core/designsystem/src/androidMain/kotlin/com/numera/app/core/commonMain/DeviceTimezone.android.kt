package com.numera.app.core.commonMain

import java.time.ZoneId

actual fun deviceTimezoneId(): String = ZoneId.systemDefault().id
