package example

object Hello {

  import logic.Core._
  import logic.Derived._

  trait GrassIsGreen extends Proposition

  //case object pGrassIsGreen extends Proof[GrassIsGreen]

  type Propostion1 = IMP[GrassIsGreen, GrassIsGreen]

  def main(args:Array[String]) {
    print("Hello")
  }
}
