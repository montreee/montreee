package de.contentup.montreee.modules.webui.repository

fun Repository.find(path: String): Element? = find(Path(path))
fun Repository.childes(path: String): List<Element> = childes(Path(path))
fun Repository.delete(path: String): Element? = delete(Path(path))
