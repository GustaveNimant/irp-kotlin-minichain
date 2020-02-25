package io.ipfs.kotlin

import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * What is it : a copy of TestStats.kt not running yet.
 * Result : QmZYVoscbWWJJZWy7Ue19iGXC5SRh3kune3gKSYHv3kzKn for EA
 * Author : Emile Achadde 25 février 2020 at 09:31:47+01:00
 * What to do : implement the correct code 
 */

class TestPeerid : BaseIpfsWebserverTest() {

    @Test
    fun testBandWidthPeerid() {
        // setup
        server.enqueue(MockResponse().setBody("{\"TotalIn\":80461165,\"TotalOut\":70998948,\"RateIn\":1103.8830769540511,\"RateOut\":1814.6417381019044}\n"))

        // invoke
        val peeridBandWidth = ipfs.peerid.peerid()

        // assert
        assertThat(peeridBandWidth).isNotNull()
        assertThat(peeridBandWidth!!.TotalIn).isEqualTo(80461165)
        assertThat(peeridBandWidth.TotalOut).isEqualTo(70998948)
        assertThat(peeridBandWidth.RateIn).isEqualTo(1103.8830769540511)
        assertThat(peeridBandWidth.RateOut).isEqualTo(1814.6417381019044)

        val executedRequest = server.takeRequest()
        assertThat(executedRequest.path).isEqualTo("/config/peerid")

    }
}
