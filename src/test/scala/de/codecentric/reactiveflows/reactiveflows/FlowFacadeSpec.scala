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

import akka.actor.{ ActorIdentity, Identify }
import akka.testkit.{ EventFilter, TestProbe }

class FlowFacadeSpec extends BaseAkkaSpec {
  import FlowFacade._
  "A FlowFacade actor" should {
    "correctly handle GetFlows, AddFlow and RemoveFlow commands" in {
      val sender: TestProbe = TestProbe()

      implicit val senderRef = sender.ref

      val flowFacade = system.actorOf(FlowFacade.props)
      flowFacade ! GetFlows
      sender.expectMsg(Set.empty)
      flowFacade ! AddFlow("Akka")
      sender.expectMsg(FlowAdded(FlowDescriptor("akka", "Akka")))
      flowFacade ! AddFlow("Akka")
      sender.expectMsg(FlowExists("Akka"))
      flowFacade ! GetFlows
      sender.expectMsg(Set(FlowDescriptor("akka", "Akka")))
      flowFacade ! RemoveFlow("akka")
      sender.expectMsg(FlowRemoved("akka"))
      flowFacade ! RemoveFlow("akka")
      sender.expectMsg(FlowUnknown("akka"))
      flowFacade ! GetFlows
      sender.expectMsg(Set.empty)
    }
  }
}