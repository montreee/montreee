package amber.sevices

class ServiceGroup(vararg services: Service) : Service() {

    private val services = ArrayList<Service>()

    init {
        this.services.addAll(services)
    }

    override fun onStart() {
        services.forEach {
            if (!it.isRunning) it.start()
        }
    }

    override fun onStop() {
        services.forEach {
            if (it.isRunning) it.stop()
        }
    }
}
