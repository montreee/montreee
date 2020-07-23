package de.contentup.montreee.util.api

import amber.api.client.BdtpApiClient
import amber.api.server.Call
import amber.api.server.impl.bdtp.BdtpApiImplSession
import amber.bdtp.Connection

val Call.connection: Connection get() = (this.session as BdtpApiImplSession).messageEvent.connection

fun Call.client(requestTimeout: Long = -1): BdtpApiClient = BdtpApiClient(connection, requestTimeout)

val Call.client: BdtpApiClient get() = client()

fun Connection.client(requestTimeout: Long = -1): BdtpApiClient = BdtpApiClient(this, requestTimeout)

val Connection.client: BdtpApiClient get() = client()
