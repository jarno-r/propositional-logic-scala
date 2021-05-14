# Propositional logic in Scala
Toy example of Curry-Howard isomorphism in Scala

With suitably chosen types, a function `f: A => B` is proof of the proposition *B*, given that proposition *A* is true, i.e. *A* implies *B*.
The problem with applying this principle to a language like Scala is that function calls do not always evaluate to a value. In Scala, this is for three reasons:
1. A function can throw an exception
2. A function can return null
3. A function might never terminate.

The third problem is fundamental. For example, proof by contradiction becomes problematic, the existence of a fuction `f: A => FALSE` does not prove that you can actually prove *FALSE* from *A*. One approach is to test `f` with a dummy proof, but then `f` can steal the dummy proof and break the system.
