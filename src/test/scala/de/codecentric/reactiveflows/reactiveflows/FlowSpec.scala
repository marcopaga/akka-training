/*
 * Copyright 2016 Marco Paga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.reactiveflows.reactiveflows

import akka.testkit.TestProbe

class FlowSpec extends BaseAkkaSpec {
  import Flow._
  "A Flow actor" should {
    "correctly handle GetMessages and AddMessage commands" in {
      val sender: TestProbe = TestProbe()

      implicit val senderRef = sender.ref

      val flow = system.actorOf(Flow.props)
      flow ! GetMessages
      sender.expectMsg(Vector.empty)

      flow ! AddMessage("test")
      //sender.expectMsg(Vector(Message("test", _)))

    }
  }
}
