package logic

trait Proposition {

}

trait PropA extends Proposition {

}

trait PropB  extends Proposition {

}

trait AND[A <: Proposition, B <: Proposition] {}
trait NOT[A <: Proposition] {}

