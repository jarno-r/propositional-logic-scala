package example

import scala.util.control.NonFatal

object Hello {

  import logic.Core._
  import logic.Derived._

  trait GrassIsGreen extends Proposition
  trait GirlsArePretty extends Proposition
  trait IAmHome extends Proposition
  type IsParadiseCity = IMP[AND[GrassIsGreen, GirlsArePretty], IAmHome]

  def pAmIHome(pGrass: Proof[GrassIsGreen], pGirls: Proof[GirlsArePretty], pParadise: Proof[IsParadiseCity]): Proof[IAmHome]
  = eImp(pParadise)(iAnd(pGrass)(pGirls))

  // Infinite recursion, exceptions and null can "prove" anything.
  // Therefore existence of a proof expression isn't evidence of a proof.
  // The evaluation of the expression must also terminate successfully.
  trait Anything extends Proposition
  def pAnything1 : Proof[Anything] = pAnything1
  def pAnything2 : Proof[Anything] = throw new Exception()
  def pAnything3 : Proof[Anything] = null

  def testPNot() = {
    // This shows why the pNot class must have a test and how the code could steal the dummy proof.
    try {
      val p: Proof[NOT[Anything]] = iNot(p => throw new Exception("Dummy:" + p))
    }catch {
      case NonFatal(e) => println(e)
    }
  }

  def main(args: Array[String]) {
    //val pIAmHome = pAmIHome(pGrassIsGreen, pGirlsArePretty, pParadise)
    //print(pIAmHome)

    println("Hello")
    testPNot()
    println("Done")
  }
}
