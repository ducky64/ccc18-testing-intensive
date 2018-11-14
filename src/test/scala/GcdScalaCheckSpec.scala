package ccc18TestIntensiveExample

import org.scalatest._
import org.scalatest.prop._
import org.scalacheck.Gen
import chisel3.iotesters._

// This version of the testdriver is parameterized by the testvector a, b, z
class GcdParameterizedTester(c: Gcd, a: Int, b: Int, z: Int) extends PeekPokeTester(c)  {
  poke(c.io.in.bits.a, a)
  poke(c.io.in.bits.b, b)

  poke(c.io.in.valid, 1)
  step(1)
  poke(c.io.in.valid, 0)

  while (peek(c.io.out.valid) == 0) {
    step(1)
  }

  expect(c.io.out.bits, z)
}

class GcdScalaCheckSpec extends FlatSpec with GeneratorDrivenPropertyChecks with Matchers {
  behavior of "Gcd"

  it should "compute gcd" in {
    // Again, we need our reference expected outputs
    def gcd(a: Int,b: Int): Int = {
      if(b ==0) a else gcd(b, a%b)
    }

    // ScalaCheck will run a few test with randomly chosen inputs from the generator
    val inputInts = for (n <- Gen.choose(1, 256)) yield n

    forAll (inputInts, inputInts) { (a: Int, b: Int) =>
      Driver.execute(() => new Gcd(32), new TesterOptionsManager) { c =>
        new GcdParameterizedTester(c, a, b, gcd(a, b))
      } should be(true)
    }
  }
}
