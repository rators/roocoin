import com.crankuptheamps.client.{Client, Command}
import com.crankuptheamps.client.exception.AMPSException

import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._
/**
  * The main.
  */
object Main extends App {
  val client = new Client("TestPublisher-Client")
  try {
    client.connect("tcp://127.0.0.1:9007/amps/json")
    client.logon()
    client.publish("messages", "{ \"message\" : \"Hello, world!\" }")
  } catch {
    case e: Exception => throw e
  } finally {
    client.close()
  }

}

object Main2 extends App {
  val subscribeClient = new Client("subscribe")
  subscribeClient.connect("tcp://127.0.0.1:9007/amps/json")
  subscribeClient.logon()
  try {
    val command = new Command("subscribe")
      .setTopic("messages")
    Try(subscribeClient.execute(command)) match {
      case Success(value) => println(value.iterator().asScala.foreach(e => println(e.getData)))
      case Failure(e: AMPSException) => throw e;
    }
  } finally {
    subscribeClient.close()
  }
}
