
/**
  * 泛型，类型变量界定，上下文界定，视图界定
  */
object 泛型和界定 {

  def main(args: Array[String]): Unit = {

    //上界
    assert(compareWithComparable("wang","Alex")=="wang")
    //视图界定
    assert(compareNoComparable(2,3)==3)
    //上下文界定
    assert(PairContext(1,2).smaller==1)

  }

  /**
    * 类型变量界定： 是指在泛型的基础上，对泛型的范围进行进一步的界定，从而缩下泛型的具体范围
    * 如果直接声明first、second，这时并不知道这两个变量是否具有compareTo方法
    * T <: Comparable[T]中的 <:表示上界，表示类型T必须实现Comparable接口，也就是T必须是Comparable的子类型
    */
  def compareWithComparable[T <: Comparable[T]](first:T,second:T)={
    if (first.compareTo(second)>0) //如果不指定Comparable[T]是T的上界，这里会报错
      first
    else
      second
  }

  /**
    *  >:表示下界，R >: T表示R必须是T的超类
    */
  case class Pair[T](val first:T,val second:T){
    //用子类替换成父类
    def replace[R >: T](first:R,second:T)= new Pair[R](first,second)
  }

  /**
    * 视图界定
    * T <% Comparable[T] 这里的<% 会自动帮你隐式转换成了实现Comparable接口类型，比如first是Int类型会隐式转换成RichInt类型
    */
  def compareNoComparable[T <% Comparable[T]](first:T,second:T)={
    if (first.compareTo(second)>0) //如果不指定Comparable[T]是T的上界，这里会报错
      first
    else
      second
  }


  /*
     上下文界定：
   * 视图界定T<%V 要求必须存在一个从T到V的隐式转换。
   * 上下文界定的形式为T:M, 其中M是另一个泛型类，它要求必须存在一个类型为M[T]的“隐形值”
   *  例如 class Pair[T: Ordering]
   * 上述定义要求必须存在一个类型为Ordering[T]的隐式值。 该隐式值可以被用在该类的方法中
   * Ordering 实现了Comparator接口
   * 当你声明一个使用隐式值的方法时，需要添加一个"隐式参数"，调用samller方法时，因为是隐式参数，所以不需要传入任何参数
   * @see 请看《隐式转换》中的参数转换内容
   */
  case class PairContext[T:Ordering](val first:T,val second : T){
    def smaller(implicit o: Ordering[T])=
      if(o.compare(first,second)<0) first else second
  }


}
