package de.showcase.tezos_viewer.domains.shared.extensions

fun String?.isNotNullOrEmpty(): Boolean{
    return this != null && this.isNotEmpty()
}
