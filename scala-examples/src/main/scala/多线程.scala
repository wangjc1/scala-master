import scala.actors._, Actor._

/**
 *
 * @author  wangjc
 *          2018/7/4
 */
object 多线程 {
  def main(args: Array[String]) {

    //第一种创建线程的方法
    import scala.actors._
    object t1 extends Actor {
      def act() {
        for (i <- 1 to 5) {
          println("I'm acting!")
          Thread.sleep(1000)
        }
      }
    }
    t1.start()

    //第二种创建线程的方法，用匿名的方式创建,创建完自动启动，不需要调用start方法启动
    val t2 = actor
    {
      for(i<- 1 to 100)
      {
        println("RichFuther is:"+i)
      }
    }

    //receive是个偏函数，阻塞获取消息
    //intActor ! "hello" //自动忽略
    //intActor ! 12
    //self ! "hello" //self表示当前线程
    //self.receive { case x => x } //阻塞获取消息
    //self.receiveWithin(1000) { case x => x } // 超时等待wait a sec!
    import scala.actors.Actor._
    val intActor = actor {
      receive {
        case x: Int => // I only want Ints
          println("Got an Int: "+ x)
      }
    }


    val sillyActor2 = actor {
      def emoteLater() {
        val mainActor = self
        println("1=="+Thread.currentThread().getName)
        actor {
          Thread.sleep(1000)
          mainActor ! "Emote"

          println("2=="+Thread.currentThread().getName)
        }
      }
      var emoted = 0
      emoteLater()
      loop {
        react {
          case "Emote" =>
            println("I'm acting!")
            emoted += 1
            if (emoted < 5)
              emoteLater()
          case msg =>
            println("Received: "+ msg)
        }
      }
    }


  }
}


