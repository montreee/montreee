package montreee.plugin.classloder

import amber.trial.trial
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.net.URLStreamHandler
import java.util.*
import java.util.jar.JarInputStream


class JarInputStreamClassLoader(private val inputStreams: MutableList<JarInputStream>, parent: ClassLoader? = null) :
        ClassLoader(
                parent ?: Thread.currentThread().contextClassLoader
        ) {

    constructor(vararg input: JarInputStream) : this(input.toMutableList())

    private val classes = Hashtable<String, Class<*>>()

    private var _jarEntries = loadSources()
    val jarEntries get() = _jarEntries

    private fun loadSources(): List<MutableMap<String, ByteArray>> =
            mutableListOf<MutableMap<String, ByteArray>>().apply {
                trial {
                    _jarEntries.forEach {
                        add(it)
                    }
                }
                inputStreams.forEach {
                    add(mutableMapOf<String, ByteArray>().apply map@{
                        with(it) {
                            while (true) {
                                val nextEntry = nextJarEntry ?: break
                                val est = nextEntry.size.toInt()
                                var data = ByteArray(if (est > 0) est else 1024)
                                var real = 0
                                var r = read(data)
                                while (r > 0) {
                                    real += r
                                    if (data.size == real) data = data.copyOf(data.size * 2)
                                    r = read(data, real, data.size - real)
                                }
                                if (real != data.size) data = data.copyOf(real)
                                this@map["/" + nextEntry.name] = data
                            }
                        }
                    })
                }
            }

    fun addSource(source: JarInputStream) {
        inputStreams.add(source)
        _jarEntries = loadSources()
    }

    @Throws(ClassNotFoundException::class)
    public override fun findClass(name: String): Class<*>? {
        var result: Class<*>? = null
        try {
            val classFileName = "/${name.replace(".", "/")}.class"
            val classData = jarEntries.find {
                it.filterKeys { it == classFileName }.entries.isNotEmpty()
            }?.filterKeys { it == classFileName }?.entries?.first()!!.value
            result = defineClass(name, classData, 0, classData.size, null)
            classes.put(name, result)
            return result
        } catch (se: SecurityException) {
            result = super.loadClass(name, true)
        } catch (e: Exception) {
            return null
        }
        return result
    }

    public override fun findResource(name: String?): URL? {
        return trial<URL?> {
            val resourceFileName = "/$name"
            URL(
                    null,
                    "bytes:///$name",
                    BytesHandler(jarEntries.find { it.filterKeys { it == resourceFileName }.entries.isNotEmpty() }!!)
            )
        } alternate { null }
    }

    private inner class BytesHandler(val entries: Map<String, ByteArray>) : URLStreamHandler() {
        override fun openConnection(u: URL): URLConnection {
            return ByteUrlConnection(u, entries)
        }
    }

    private inner class ByteUrlConnection(url: URL, val entries: Map<String, ByteArray>) : URLConnection(url) {
        override fun connect() {}
        override fun getInputStream(): InputStream {
            return ByteArrayInputStream(entries[this.getURL().path.replace("///", "")])
        }
    }

    fun loadAllClasses(): List<Class<out Any>> {
        val classes = jarEntries.flatMap {
            it.filter { it.key.endsWith(".class") }.map {
                it.key.replaceFirst("/", "").replace("/", ".").removeSuffix(".class")
            }.map {
                trial<Class<*>?> { loadClass(it) } alternate { null }
            }
        }
        return classes.filterNotNull()
    }
}
