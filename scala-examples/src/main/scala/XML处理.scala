import scala.xml.XML

/**
 *
 * @author  wangjc
 *          2018/6/29
 */
object XML处理 {

  def main(args: Array[String]) {
    loadXML()
  }

  def loadXML(): Unit = {
    val xml = XML.loadFile("symbol.xml")
  }
}
