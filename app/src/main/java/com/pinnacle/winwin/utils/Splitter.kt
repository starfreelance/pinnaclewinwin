package com.pinnacle.winwin.utils

object Splitter {

    @JvmStatic
    internal fun getMapFromQuery(queryString: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val keyValuePairs = queryString
            .split("&")
            .map { param ->
                param.split("=").let { Pair(it[0], it[1]) }
            }
        map.putAll(keyValuePairs)
        return map
    }
}