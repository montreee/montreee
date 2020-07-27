package amber.bdtp

import amber.source.Source

class Message(val session: Session, val id: String, val contentType: String, val path: String) : Source<String> {

    private val _packages = mutableListOf<Package>()

    val packages: List<Package>
        get() {
            if (!isComplete()) return listOf<Package>()

            return _packages.sortedBy { it.id }
        }

    private var totalPackages = 1L

    internal fun receive(pack: Package, totalPackages: Long = 0) {
        if (totalPackages > this.totalPackages) this.totalPackages = totalPackages

        if (pack.id == null) return

        if (!_packages.none { it.id == pack.id }) return

        _packages.add(pack)
    }

    override fun read() = buildString {
        packages.forEach {
            append(it.contend)
        }
    }

    fun isComplete() = _packages.size.toLong() == totalPackages
}
