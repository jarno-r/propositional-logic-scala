package logic

// Derived things. This extends on the Core, but anything but here doesn't really break the core logic.
object Derived {

  import Core._

  // Implication defined in terms of the primitive connectives.
  type IMP[A <: Proposition, B <: Proposition] = NOT[AND[A, NOT[B]]]

  // Disjunction by De Morgan's.
  type OR[A <: Proposition, B <: Proposition] = NOT[AND[NOT[A], NOT[B]]]

  // True proposition
  type TRUE = IMP[FALSE, FALSE]

  // System of Natural Deduction. Rules for manipulating proofs.
  // The actual bodies of these functions are not really relevant, only that there is one.

  // Introduction and elimination rules for conjunction.
  def iAnd[A <: Proposition, B <: Proposition](a: Proof[A])(b: Proof[B]): Proof[AND[A, B]] = pAnd(a, b)
  def eAnd1[A <: Proposition, B <: Proposition](and: Proof[AND[A, B]]): Proof[A] = and.asInstanceOf[pAnd[A, B]].a
  def eAnd2[A <: Proposition, B <: Proposition](and: Proof[AND[A, B]]): Proof[B] = and.asInstanceOf[pAnd[A, B]].b

  // Introduction and elimination rules for negation.
  def iNot[A <: Proposition](p: Proof[A] => Proof[FALSE]): Proof[NOT[A]] = pNot(p)
  def eNot[A <: Proposition, C <: Proposition](pA: Proof[A])(pNotA: Proof[NOT[A]]): Proof[C] = pFalse(pNotA.asInstanceOf[pNot[A]].p(pA))

  // The double negation elimination rule doesn't follow the same pattern of introduction and elimination rules.
  def eNotNot[A <: Proposition](pA: Proof[NOT[NOT[A]]]): Proof[A] = pNotNot(pA)

  // The derived connectives will use the above rules to prove their rules of deduction.

  // Proof by contradiction
  def pContra[A <: Proposition](p:Proof[NOT[A]] => Proof[FALSE]) : Proof[A] = eNotNot(iNot(q => p(q)))

  // Implication.
  def iImp[A <: Proposition, B <: Proposition](fImp: Proof[A] => Proof[B]): Proof[IMP[A, B]] =
    iNot(p1 => {
      val pA: Proof[A] = eAnd1(p1)
      val pNotB: Proof[NOT[B]] = eAnd2(p1)
      val pB: Proof[B] = fImp(pA)
      eNot(pB)(pNotB) : Proof[FALSE]
    })

  def eImp[A <: Proposition, B <: Proposition](pImp: Proof[IMP[A, B]])(pA: Proof[A]): Proof[B] =
    pContra((p:Proof[NOT[B]]) => eNot(iAnd(pA)(p))(pImp))

  def iOr1[A <: Proposition, B <: Proposition](pA: Proof[A]): Proof[OR[A, B]] =
    iNot((p => eNot(pA)(eAnd1(p))))

  def iOr2[A <: Proposition, B <: Proposition](pB: Proof[B]): Proof[OR[A, B]] =
    iNot((p => eNot(pB)(eAnd2(p))))

  def eOr[A <: Proposition, B <: Proposition, C <: Proposition]
  (fA: Proof[A] => Proof[C])(fB: Proof[B] => Proof[C])(pOr: Proof[OR[A, B]]): Proof[C] =
    pContra((p:Proof[NOT[C]]) => ???)

  def pTrue : Proof[TRUE] = iImp(p => p)
}
