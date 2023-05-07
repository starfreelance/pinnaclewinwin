package com.pinnacle.winwin.utils

class ResponseSplit {

    companion object {
        private const val TAG = "ResponseSplit"

        @JvmStatic
        fun getMapFromQuery(queryString: String): Map<String, String> {
            val map = mutableMapOf<String, String>()
            val keyValuePairs = queryString
                .split("&")
                .map { param ->
                    val split = param.split("=")

                    param.split("=").let {
                        //Handling java.lang.IndexOutOfBoundsException: Index: 1, Size: 1
                        if (split.size == 2) {
                            Pair(it[0], it[1])
                        } else {
                            Pair(it[0], "null")
                        }
                    }
//                    param.split("=").let { Pair(it[0], it[1]) }
                }
            map.putAll(keyValuePairs)
            return map
        }
    }

}