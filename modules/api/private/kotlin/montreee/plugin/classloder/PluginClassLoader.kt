package montreee.plugin.classloder

import java.net.URL
import java.net.URLClassLoader

class PluginClassLoader(vararg val urls: URL) : URLClassLoader(urls)
