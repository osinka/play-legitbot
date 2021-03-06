package com.osinka.play.legitbot

import play.api.http.HeaderNames
import play.api.test.FakeRequest
import org.scalatest.{FunSpec, Matchers}

class BaiduspiderSpec extends FunSpec with Matchers {
  describe("Baiduspider check") {
    it("doesn't like reverse-forward match") {
      BotMatches.reverseForwardMatch("baidu.com")("123.125.71.116") should be('empty)
    }
    it("likes reverse-only match") {
      BotMatches.reverseMatch("baidu.com")("123.125.71.116") should be('defined)
    }

    it("recognizes 123.125.7.116 as malicious") {
      val request = FakeRequest("GET", "/").withHeaders(HeaderNames.USER_AGENT -> """Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)""").copy(remoteAddress = "123.125.7.116")

      Baiduspider.isDefinedAt(request) should equal(true)
      Baiduspider.apply(request) should equal(false)
    }

    // 123.125.71.116 [12/Nov/2015:10:44:05 +0400] 200 "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)"
    it("recognizes 123.125.71.116 as valid") {
      val request = FakeRequest("GET", "/").withHeaders(HeaderNames.USER_AGENT -> """Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)""").copy(remoteAddress = "123.125.71.116")

      Baiduspider.isDefinedAt(request) should equal(true)
      Baiduspider.apply(request) should equal(true)
    }
  }
}
