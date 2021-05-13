package example

object Hello {

  import logic.Core._
  import logic.Derived._

  trait GrassIsGreen extends Proposition
  trait GirlsArePretty extends Proposition
  trait IAmHome extends Proposition
  type IsParadiseCity = IMP[AND[GrassIsGreen, GirlsArePretty], IAmHome]

  def pAmIHome(pGrass: Proof[GrassIsGreen], pGirls: Proof[GirlsArePretty], pParadise: Proof[IsParadiseCity]): Proof[IAmHome]
  = eImp(pParadise)(iAnd(pGrass)(pGirls))

  def main(args: Array[String]) {
    val pGirlsArePretty: Proof[GirlsArePretty] = pAxiomatic()
    val pGrassIsGreen: Proof[GrassIsGreen] = pAxiomatic()
    val pParadise: Proof[IsParadiseCity] = pAxiomatic()

    val pIAmHome = pAmIHome(pGrassIsGreen, pGirlsArePretty, pParadise)
    print(pIAmHome)
  }
}
