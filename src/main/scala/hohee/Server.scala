package hohee

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.{IO, Tcp}

object Server {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Server")
    system.actorOf(Props[Server])
  }
}

class Server extends Actor {
  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))

  def receive = {
    case b @ Bound(localAddress) =>
      println(s"Bound to ${localAddress.getHostName}:${localAddress.getPort}")
      context.parent ! b
    case CommandFailed(_: Bind) => context stop self
    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[SimplisticHandler])
      val connection = sender()
      connection ! Register(handler)
  }
}
