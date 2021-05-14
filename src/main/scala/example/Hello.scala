package example

import scala.util.control.NonFatal

object Hello {

  import logic.Core._
  import logic.Derived._

  // Infinite recursion, exceptions and null can "prove" anything.
  // Therefore existence of a proof expression isn't evidence of a proof.
  // The evaluation of the expression must also terminate successfully.
  trait Anything extends Proposition
  def pAnything1 : Proof[Anything] = pAnything1
  def pAnything2 : Proof[Anything] = throw new Exception()
  def pAnything3 : Proof[Anything] = null

  {
    // This shows why the pNot class must have a test and how the code could steal the dummy proof.
    try {
      val p: Proof[NOT[Anything]] = iNot(p => throw new Exception("Dummy:" + p))
    }catch {
      case NonFatal(e) => println(e)
    }
  }

  trait Apples extends Proposition
  trait Bananas extends Proposition
  trait Cake extends Proposition

  // A non-trivial tautology.
  // (A and (B or C)) -> ((A and B) or (A and C))
  type ComplexProposition = IMP[AND[Apples, OR[Bananas, Cake]], OR[AND[Apples, Bananas], AND[Apples, Cake]]]

  def main(args: Array[String]) {

  }
}
