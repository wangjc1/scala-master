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

    //val定义值时，会做call-by-value操作，
    //def则会做call-by-name操作,就是先声明一个函数或变量而不计算后面的内容。
    //使用 def 每次都会重新进行取值。
    //那么对应上面的情况，如果文件内容改变，由于方法体在被调用时会重新执行，将得到被修改后文件的内容。
    {
    // 这是一个死循环
    def loop: Boolean = loop
    // 用val定义时会做call-by-value，以下语句会block住
    val x = loop
    // 用def定义时，是做的call-by-name。故以下语句暂时不会执行，在用到y的时候才做evaluation
    def y = loop
    }

  }



}


