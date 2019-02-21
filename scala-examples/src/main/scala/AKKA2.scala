import scala.actors.Actor.{actor, loop, receive}

/**
  *
  * @author: wangjc
  *          2019/2/19
  */
object AKKA2 {


    def main(args: Array[String]): Unit = {
        val a1 = actor {
            loop {
                receive {
                    case m => println("Receive : "+ m)
                }
            }
        }
        a1 ! "hello"
    }
}
