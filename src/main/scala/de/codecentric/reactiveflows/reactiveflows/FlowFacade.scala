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

import java.net.URLEncoder

import akka.actor.{ Props, Actor, ActorLogging }
import de.codecentric.reactiveflows.reactiveflows.FlowFacade._

object FlowFacade {
  final val Name = "flow-facade";

  def props: Props = Props(new FlowFacade)

  case object GetFlows

  case class FlowDescriptor(name: String, label: String)

  case class AddFlow(label: String)

  case class FlowAdded(flowDescriptor: FlowDescriptor)

  case class FlowExists(label: String)

  case class RemoveFlow(name: String)

  case class FlowRemoved(name: String)

  case class FlowUnknown(name: String)

}

class FlowFacade extends Actor with ActorLogging {

  import FlowFacade._

  var nameToFlow: Map[String, FlowDescriptor] = Map.empty

  def createName(label: String) = {
    URLEncoder.encode(label.toLowerCase(), "UTF-8")
  }

  override def receive: Receive = {
    case GetFlows => sender() ! nameToFlow.valuesIterator.to[Set]

    case AddFlow(label) if !nameToFlow.contains(createName(label)) => {
      addFlow(label)
    }
    case AddFlow(label) => {
      sender() ! FlowExists(label)
    }
    case RemoveFlow(name) if nameToFlow.contains(name) => {
      removeFlow(name)
    }
    case RemoveFlow(name) => {
      sender() ! FlowUnknown(name)
    }

  }

  def removeFlow(name: String): Unit = {
    nameToFlow -= name
    sender() ! FlowRemoved(name)
  }

  def addFlow(label: String): Unit = {
    val myFlowDescriptor = FlowDescriptor(createName(label), label)
    nameToFlow += createName(label) -> myFlowDescriptor
    sender() ! FlowAdded(myFlowDescriptor)
  }
}
