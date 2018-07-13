/**
 *
 * @author  wangjc
 *          2018/7/12
 */
object 算子 {
  def main(args: Array[String]) {
    //flatMap1()
    //map1()


    val xs = Map("a" -> List(11,111,22), "b" -> List(22,222,2222)).flatMap(_._2)
    println(xs.mkString(","))

    // ys will be a Map[Int, Int]
    val ys = Map("a" -> List(1 -> 11,1 -> 111), "b" -> List(2 -> 22,2 -> 222)).flatMap(_._2)
    println(ys.mkString(","))

    //val xs = Map("a" -> "1212,21,12,12", "b" -> "1212,21,12,12").flatMap(_._2)

  }

  def flatMap1(): Unit = {
    val li = List(1,2,3)
    val res = li.flatMap(x => x match {
      case 3 => List('a','b')
      case _ => List(x*2)
    })
    println(res)
  }

  def map1(): Unit = {
    val li = List(1,2,3)
    val res = li.map(x => x match {
      case 3 => List('a','b')
      case _ => List(x*2)
    })
    println(res)
  }

}


