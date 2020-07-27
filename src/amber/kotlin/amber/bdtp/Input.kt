package amber.bdtp

import kotlinx.coroutines.channels.Channel

class Input : Channel<String> by Channel()
