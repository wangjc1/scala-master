import A.B.Bclass

object 包 {

  def main(args: Array[String]): Unit = {
    val b = new Bclass
    b.b()

    2.+(3)
  }
}

package A {
  package B {
    import A.B.C.Cclass
    class Bclass{
      def b(){
        val p = new Cclass()
        p.name = "abc"
        p.name
      }
    }

    package C{
      class Cclass {
        //只能在B包中使用name属性
        private[B] var name = ""
      }
    }
  }
}
