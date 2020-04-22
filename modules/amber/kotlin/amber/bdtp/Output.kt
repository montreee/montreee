package amber.bdtp

import kotlinx.coroutines.channels.Channel

class Output : Channel<String> by Channel()
