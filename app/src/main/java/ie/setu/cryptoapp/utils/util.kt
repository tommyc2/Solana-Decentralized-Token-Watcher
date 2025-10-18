package ie.setu.cryptoapp.utils

import ie.setu.cryptoapp.api.API

object Utility {
    fun isValidAlias(alias: String): Boolean {
        return alias.isNotEmpty() && alias.length <= 10
    }
}