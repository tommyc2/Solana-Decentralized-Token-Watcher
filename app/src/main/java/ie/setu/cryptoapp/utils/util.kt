package ie.setu.cryptoapp.utils

import ie.setu.cryptoapp.api.API

object Utility {

    fun isValidContractAddress(ca: String) : Boolean {
        val contractAddressExists = API.solanaTokenExists(ca)
        if (contractAddressExists) {
            return true
        }
        return false
    }

}