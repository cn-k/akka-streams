import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import java.nio.file.Paths
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object Main extends App {

  implicit val system: ActorSystem = ActorSystem("QuickStart")

  val source: Source[Int, NotUsed] = Source(1 to 100)
  source.runForeach(i => println(i))

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String].map(s => ByteString(s + "\n")).toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  //factorials.map(_.toString).runWith(lineSink("factorial2.txt"))

  factorials
    .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
    .throttle(1, 1.second)
    .runForeach(println)
}
