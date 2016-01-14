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

import java.time.LocalDateTime

import akka.actor.{ Actor, Props }

object Flow {

  case object GetMessages
  case class Message(text: String, time: LocalDateTime)
  case class AddMessage(text: String)
  case class MessageAdded(flowName: String, message: Message)

  final val Name = "flow"
  def props: Props = Props(new Flow)
}

class Flow extends Actor {
  import Flow._

  private var messages = Vector.empty[Message]

  def addMessage(text: String): Unit = {
    val message: Message = new Message(text, LocalDateTime.now())
    messages :+= message
    sender ! MessageAdded(self.path.name, message)
  }

  override def receive: Receive = {
    case GetMessages      => sender() ! messages
    case AddMessage(text) => addMessage(text)
  }
}
