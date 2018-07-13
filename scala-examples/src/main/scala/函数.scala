/**
 *
 * @author  wangjc
 *          2018/6/28
 */
object 函数 {

  def main(args: Array[String]) {

    //使用“=>”符号定义函数
    //f是一个Int => String类型的函数
    //myInt => "The value of myInt is: " + myInt.toString()是函数的实现
    val f: Int => String = myInt => "The value of myInt is: " + myInt.toString()
    println(f(5)) //The value of myInt is: 5


    //关于=>和=在定义函数时的区别
    val f_1 = (x:Int) => x*x  //=>通常用来定义匿名函数
    def f_2(x:Int) = x*x //=通常用在def定义的函数
    val f_3 :(Int) => Int = 3*_ // f_3是(Int) => Int类型的函数，_代表一个参数

    //_ + 1可以表示一个函数，_表示一个空格，调用的时候先不填数，后面传入x的时候，把_给填充上了
    //op相当于：val op :(Double) => Double =_ + 1，第一次调用op(x)=5+1，第二次调用op(6)=6+1
    def twice(op: Double => Double, x: Double) = op(op(x))
    twice(_ + 1, 5) //7.0
  }



}


