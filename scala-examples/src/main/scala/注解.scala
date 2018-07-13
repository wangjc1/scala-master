/**
 *
 * @author  wangjc
 *          2018/6/29
 */
object 注解 {

  def main(args: Array[String]) {
    fact(5)
  }

  @deprecated(message = "Use factorial(n: BigInt) instead")
  def fact(n: Int): Int = {n*n}

}
