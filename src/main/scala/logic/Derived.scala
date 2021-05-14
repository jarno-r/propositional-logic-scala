package logic

// Derived things. This extends on the Core, but anything but here doesn't really break the core logic.
object Derived {

  import Core._

  type NOT[A <: Proposition] = IMP[A, FALSE]

  type AND[A <: Proposition, B <: Proposition] = NOT[IMP[A, NOT[B]]]

  // Disjunction by De Morgan's.
  type OR[A <: Proposition, B <: Proposition] = NOT[AND[NOT[A], NOT[B]]]

  // True proposition
  type TRUE = IMP[FALSE, FALSE]

  // System of Natural Deduction. Rules for manipulating proofs.
  // The actual bodies of these functions are not really relevant, only that there is one.

  // Introduction and elimination rules for negation.
  def iNot[A <: Proposition](p: Proof[A] => Proof[FALSE]): Proof[NOT[A]] = iImp(p)
  def eNot[A <: Proposition, C <: Proposition](pNotA: Proof[NOT[A]])(pA: Proof[A]): Proof[C] = pFalse(eImp(pNotA)(pA))

  // The double negation elimination rule doesn't follow the same pattern of introduction and elimination rules.
  def eNotNot[A <: Proposition](pA: Proof[NOT[NOT[A]]]): Proof[A] = pNotNot(pA)

  // Proof by contradiction
  def pContra[A <: Proposition](p:Proof[NOT[A]] => Proof[FALSE]) : Proof[A] = eNotNot(iNot(q => p(q)))

  // Introduction and elimination rules for conjunction.
  def iAnd[A <: Proposition, B <: Proposition](pA: Proof[A])(pB: Proof[B]): Proof[AND[A, B]] = iNot(p => eNot(eImp(p)(pA))(pB))
  def eAnd1[A <: Proposition, B <: Proposition](pAnd: Proof[AND[A, B]]): Proof[A] = pContra(pn => eNot(pAnd)(iImp(p => eNot(pn)(p))))
  def eAnd2[A <: Proposition, B <: Proposition](pAnd: Proof[AND[A, B]]): Proof[B] = pContra(p => eNot(pAnd)(iImp(_ => p)))

  // The derived connectives will use the above rules to prove their rules of deduction.

  def iOr1[A <: Proposition, B <: Proposition](pA: Proof[A]): Proof[OR[A, B]] =
    iNot((p => eNot(eAnd1(p))(pA)))

  def iOr2[A <: Proposition, B <: Proposition](pB: Proof[B]): Proof[OR[A, B]] =
    iNot((p => eNot(eAnd2(p))(pB)))

  def eOr[A <: Proposition, B <: Proposition, C <: Proposition]
  (pOr: Proof[OR[A, B]])(fA: Proof[A] => Proof[C])(fB: Proof[B] => Proof[C]): Proof[C] = {
    val pImp: Proof[IMP[NOT[A], NOT[NOT[B]]]] = eNotNot(pOr)

  }

  def pTrue : Proof[TRUE] = iImp(p => p)
}
