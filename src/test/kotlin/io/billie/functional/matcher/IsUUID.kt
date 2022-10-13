package io.billie.functional.matcher

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.util.*

object IsUUID {

    fun isUuid(): TypeSafeMatcher<String> {
        return object : TypeSafeMatcher<String>() {
            override fun describeTo(description: Description) {
                description.appendText("is not a valid UUID")
            }

            override fun matchesSafely(s: String?): Boolean {
                return try {
                    UUID.fromString(s)
                    true
                } catch (e: IllegalArgumentException) {
                    false
                }
            }
        }
    }


}
