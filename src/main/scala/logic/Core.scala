package logic

// The core logic. This is the trusted part of the logic system. If this is sound, then the derived logic is sound.
object Core {

  // Supertype of all propositions.
  trait Proposition {}


  // Implication
  sealed trait IMP[A <: Proposition, B <: Proposition] extends Proposition {}

  // Sealed trait of proofs. By being sealed, it prevents false axiomatic proofs from being introduced.
  sealed trait Proof[P <: Proposition] { }

  // This dummy evidence is used when a proof has to be produced without proof.
  // It is not allowed for users to build objects of this class directly.
  private final case class TestDummy[A<: Proposition]() extends Proof[A]{
    override def toString = "You stole my dummy!"
  }

  // Evidence of an implication. Turns a function into a Proof.
  private final case class ImpEvidence[A <: Proposition, B <: Proposition](p: Proof[A] => Proof[B]) extends Proof[IMP[A, B]] {
    {
      // Test that p actually returns a value.
      // Warning: If p steals the dummy proof, it breaks the system.
      require(p(TestDummy()) != null)
    }
  }

  // Implication introduction and elimination rules.
  def iImp[A <: Proposition, B <: Proposition](p: Proof[A] => Proof[B]): Proof[IMP[A,B]] = ImpEvidence(p)
  def eImp[A <: Proposition, B <: Proposition](pImp: Proof[IMP[A, B]])(pA: Proof[A]): Proof[B] = {
    // In principle, we could just always return a TestDummy[B](), but expanding on this, we could actually produce
    // a readable proof as a tree-like structure of nested Proofs.
    pImp match {
      case e: ImpEvidence[A, B] => e.p(pA)
      case _: NotNotEvidence[IMP[A, B]] => TestDummy[B]() // This really shouldn't return a dummy.
      case _: FalseEvidence[IMP[A, B]] => TestDummy[B]() // This really shouldn't return a dummy.
      case _: TestDummy[IMP[A, B]] => TestDummy[B]()

        // Scalac doesn't seem to be able to warn about non-exhaustive match in this case, so throw a decent error.
      case _ => throw new Exception("This shouldn't happen! " + pImp.getClass)
    }
  }

  // False proposition
  // Limitations of Scala 2 type system forces inclusion of a FALSE.
  sealed trait FALSE extends Proposition {}
  final case class FalseEvidence[A <: Proposition](p : Proof[FALSE]) extends Proof[A]

  // Double negation elimination. Equivalent to reductio ad absurdum or the excluded middle.
  final case class NotNotEvidence[A <: Proposition](p: Proof[IMP[IMP[A, FALSE], FALSE]]) extends Proof[A]
}
