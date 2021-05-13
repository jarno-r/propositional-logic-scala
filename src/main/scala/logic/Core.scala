package logic

// The core logic. This is the trusted part of the logic system. Everything else is just derived from this.
object Core {

  // Supertype of all propositions.
  trait Proposition {}

  // False proposition
  sealed trait FALSE extends Proposition {}

  // A complete set of logical connectives: conjunction and negation.
  sealed trait AND[A <: Proposition, B <: Proposition] extends Proposition {}
  sealed trait NOT[A <: Proposition] extends Proposition {}


  // Sealed trait of proofs. By being sealed, it prevents false proofs from being introduced.
  sealed trait Proof[P <: Proposition] { }

  // Axioms. These act as evidence for a proposition, without being proven themselves.
  sealed case class pAnd[A <: Proposition, B <: Proposition](a: Proof[A], b: Proof[B]) extends Proof[AND[A, B]]

  // These two I would like to write as p[C]: Proof[A] => Proof[C], where C is a generic (universal) type. But I don't think that's possible in Scala.
  sealed case class pNot[A <: Proposition](p: Proof[A] => Proof[FALSE]) extends Proof[NOT[A]]
  sealed case class pFalse[A <: Proposition](a : Proof[FALSE]) extends Proof[A]

  // This one is special. It is equivalent to the law of excluded middle.
  sealed case class pNotNot[A <: Proposition](a: Proof[NOT[NOT[A]]]) extends Proof[A]
}
