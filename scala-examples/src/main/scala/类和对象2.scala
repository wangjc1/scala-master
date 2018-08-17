package scala.examples.class2

/**
 *
 * @author  wangjc
 *          2018/7/6
 */
object 类和对象2 {

  def main(args: Array[String]) {
    println(Element.elem(Array("hello")) above Element.elem(Array("world!")))
    println(Element.elem(Array("one", "two")) beside Element.elem(Array("one")))
  }
}

object Element {
  protected class ArrayElement(val contents: Array[String])
    extends Element {
  }
  private class LineElement(s:String) extends ArrayElement(Array(s)) {
    override def width = s.length
    override def height = 1
  }
  private class UniformElement (ch :Char,
                                override val width:Int,
                                override val height:Int
                                 ) extends Element{
    private val line=ch.toString * width
    def contents = Array.fill(height)(line)
  }
  def elem(contents: Array[String]):Element =
    new ArrayElement(contents)
  def elem(chr:Char, width:Int, height:Int) :Element =
    new UniformElement(chr,width,height)
  def elem(line:String) :Element =
    new LineElement(line)
}


abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length
  def above(that: Element) :Element =
    Element.elem(this.contents ++ that.contents)
  def beside(that: Element) :Element = {
    Element.elem(
      for(
        (line1,line2) <- this.contents zip that.contents
      ) yield line1+line2
    )
  }
  override def toString = contents mkString "\n"
}

//case会自动生成伴生对象:object Point{def apply(a:Int,b:Int):Point=new Point(a,b)}
case class Point(a:Int,b:Int){}


