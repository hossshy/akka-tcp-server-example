package hohee

import akka.actor.Actor

class SimplisticHandler extends Actor {
  import akka.io.Tcp._

  def receive = {
    case Received(data) =>
      //sender() ! Write(data)
      println(data.utf8String)
    case PeerClosed =>
      println("Client closed.")
      context stop self
  }
}
