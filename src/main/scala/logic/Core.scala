package logic

// The core logic. This is the trusted part of the logic system. Everything else is just derived from this.
object Core {

  // Supertype of all propositions.
  trait Proposition {}


  // Implication
  sealed trait IMP[A <: Proposition, B <: Proposition] extends Proposition {}

  // Sealed trait of proofs. By being sealed, it prevents false axiomatic proofs from being introduced.
  sealed trait Proof[P <: Proposition] { }

  // Axioms. These act as evidence for a proposition, without being proven themselves.

  private final case class TestDummy[A<: Proposition]() extends Proof[A]{
    override def toString() = "You stole my dummy!"
  }

  private final case class ImpEvidence[A <: Proposition, B <: Proposition](p: Proof[A] => Proof[B]) extends Proof[IMP[A, B]] {
    {
      // Test that p actually returns a value.
      // Warning: If p steals the dummy proof, it breaks the system.
      require(p(TestDummy()) != null)
    }
  }

  def iImp[A <: Proposition, B <: Proposition](p: Proof[A] => Proof[B]): Proof[IMP[A,B]] = ImpEvidence(p)
  def eImp[A <: Proposition, B <: Proposition](pImp : Proof[IMP[A,B]])(pA:Proof[A]) : Proof[B] = {
    if (pImp.isInstanceOf[ImpEvidence[A,B]]) pImp.asInstanceOf[ImpEvidence[A,B]].p(pA)
    else if (pImp.isInstanceOf[TestDummy[IMP[A,B]]]) TestDummy[B]()
    else throw new Exception("This shouldn't happen! "+pImp.getClass)
  }


  // False proposition
  // Limitation of Scala 2 type system forces inclusion of FALSE.
  sealed trait FALSE extends Proposition {}
  final case class pFalse[A <: Proposition](p : Proof[FALSE]) extends Proof[A]

  // Double negation elimination or reductio ad absurdum
  final case class pNotNot[A <: Proposition](p: Proof[IMP[IMP[A, FALSE], FALSE]]) extends Proof[A]
}
