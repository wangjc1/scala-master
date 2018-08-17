
/**
 *
 * @author  wangjc
 *          2018/7/9
 */
object 枚举 {
  def main(args: Array[String]): Unit = {
  }
}

//枚举
object Color extends Enumeration {
  val Red, Green, Blue = Value
  val d = Value(1,"abc")
}
