package logic

// The core logic. This is the trusted part of the logic system. If this is sound, then the derived logic is sound.
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
  def eImp[A <: Proposition, B <: Proposition](pImp: Proof[IMP[A, B]])(pA: Proof[A]): Proof[B] = {
    pImp match {
      case e: ImpEvidence[A, B] => e.p(pA)
      case _: NotNotEvidence[IMP[A, B]] => TestDummy[B]()
      case _: FalseEvidence[IMP[A, B]] => TestDummy[B]()
      case _: TestDummy[IMP[A, B]] => TestDummy[B]()
      case _ => throw new Exception("This shouldn't happen! " + pImp.getClass)
    }
  }

  // False proposition
  // Limitations of Scala 2 type system forces inclusion of FALSE.
  sealed trait FALSE extends Proposition {}
  final case class FalseEvidence[A <: Proposition](p : Proof[FALSE]) extends Proof[A]

  // Double negation elimination. Equivalent to reductio ad absurdum or the excluded middle.
  final case class NotNotEvidence[A <: Proposition](p: Proof[IMP[IMP[A, FALSE], FALSE]]) extends Proof[A]
}
