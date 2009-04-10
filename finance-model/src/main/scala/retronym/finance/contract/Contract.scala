package retronym.finance.contract

case class Cashflow(amount: Double, currency: Currency) {
  def scale(factor: Double) : Cashflow = Cashflow(factor * amount, currency)
  def negate : Cashflow = scale(-1)
}

case class Evolution(c: Contract, cf: List[Cashflow])

object Evolution {
  implicit def ContractToEvolution(c: Contract): Evolution = Evolution(c, List[Cashflow]())

  def evolvePair(c1: Contract, c2: Contract, i: Instant,
                cashflowCombiner: (List[Cashflow], List[Cashflow]) => List[Cashflow],
                contractCombiner: (Contract, Contract) => Contract) = {
    (c1.evolve(i), c2.evolve(i)) match {
      case (Evolution(c1, cf1), Evolution(c2, cf2)) =>
        {
          val contract: Contract = contractCombiner(c1, c2)
          val cashflows: List[Cashflow] = cashflowCombiner(cf1, cf2)
          Evolution(contract, cashflows)
        }
    }
  }

  def evolve(c1: Contract, i: Instant,
            cashflowCombiner: (List[Cashflow]) => List[Cashflow],
            contractCombiner: (Contract) => Contract) = {
    c1.evolve(i) match {
      case Evolution(c1, cf1) => Evolution(contractCombiner(c1), cashflowCombiner(cf1))
    }
  }

}

trait Contract {
  def evolve(i: Instant): Evolution = this

  def acquire(i: Instant): Contract = this
}

trait Obs[T] {
  def observe(i: Instant): T
}


trait Instant

//trait Obs1[M[_], T] extends Obs[T] {
//  observe(t: Instant): M[T]
//}

case class Timestep(i: Int) extends Instant

case class Currency(code: String)

case object Zero extends Contract

case class One(c: Currency) extends Contract {
  // unify acquire and evolve to be able to pass back a cashflow on acquisition.
//  def acquire(i: Instant) = Evolution(Zero, List(Cashflow(1, c)))
  override def acquire(i: Instant) = Zero
}

case class Give(c: Contract) extends Contract {
  override def acquire(i: Instant) = Give(c.acquire(i))

  override def evolve(i: Instant) = c.evolve(i) match {
    case Evolution(c1, cf) => Evolution(Give(c1), cf.map(_.negate))
  }
}

case class And(c1: Contract, c2: Contract) extends Contract {
  override def acquire(i: Instant) = And(c1.acquire(i), c2.acquire(i))

  override def evolve(i: Instant) = Evolution.evolvePair(c1, c2, i, _ ::: _, And(_, _))
}

case class Or(c1: Contract, c2: Contract) extends Contract {
  // need to pass in the choice of contract here...
  override def acquire(i: Instant) = Or(c1.acquire(i), c2.acquire(i))
  // evolve not needed as this contract will simpify on aqcuisition.

}

case class Cond(o: Obs[Boolean], c1: Contract, c2: Contract) extends Contract {
  override def acquire(i: Instant) = (if (o.observe(i)) c1 else c2).acquire(i)

  // evolve not needed as this contract will simpify on aqcuisition.
}

case class Scale(o: Obs[Double], c: Contract) extends Contract {
  override def acquire(i: Instant) = Scale(o, c.acquire(i))

  override def evolve(i: Instant) = Evolution.evolve(c, i, _.map(x => x.scale(o.observe(i))), Scale(o, _))

}

case class When(o: Obs[Boolean], c: Contract) extends Contract {
  override def evolve(i: Instant) = if (o.observe(i)) c.acquire(i) else this
}

case class Anytime(o: Obs[Boolean], c: Contract) extends Contract {
  // add param to signal intent to aqcuire.
  override def evolve(i: Instant) = {
//    if (o.isPermanently(i, false)) {
//      Zero
//    } else {
      Evolution.evolve(c, i, _ => List[Cashflow](), Anytime(o, _))
//    }
  }

  override def acquire(i: Instant) = Anytime(o, c)
}

case class Until(o: Obs[Boolean], c: Contract) extends Contract {
  override def evolve(i: Instant) = {
    if (o.observe(i) == false) {
      Zero
    } else {
      Evolution.evolve(c, i, identity _, Until(o, _))
    }
  }
}


object ContractCombinators {
  def and = And.apply _

  def give = Give.apply _

  def andGive(c: Contract, d: Contract) = and(c, give(d))
}



object Observable {

  def const[A](a: A): Obs[A] = new Obs[A] {
    def observe(i: Instant) = a
  }

  def lift[A, B](f: A => B)(o: Obs[A]): Obs[B] = new Obs[B] {
    def observe(i: Instant) = f(o.observe(i))
  }

  def lift2[A, B, C](f: (A, B) => C)(o1: Obs[A], o2: Obs[B]): Obs[C] = new Obs[C] {
    def observe(i: Instant) = f(o1.observe(i), o2.observe(i))
  }

  def date(t: Instant): Obs[Instant] = new Obs[Instant] {
    def observe(i: Instant) = t
  }
}

class ObsW[T <% Numeric[T]](o1: Obs[T]) {
  import Observable._
  def +(o2: Obs[T]) = error("todo")
}

trait Numeric[T] {
  def +(t: T): T
}

/*
  performance_payoff_option: C

  PRE:
  Initial Fixing: or(S - K, 0)
  Final Fixing  : S - K

  S - K


  forward_start: (t: Instant, f: (X => Contract)
*/