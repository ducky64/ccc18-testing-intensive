package ccc18TestIntensiveExample

import org.scalatest._
import chisel3._  // for literal constructors
import chisel3.tester._
import chisel3.tester.TestAdapters._  // Decoupled test utils

// for the second ScalaCheck example
import org.scalatest.prop._
import org.scalacheck.Gen

class T2GcdSpec extends FlatSpec with ChiselScalatestTester {
  behavior of "Gcd with Testers2"

  it should "compute gcd" in {
    test(new Gcd(32)) { c =>
      val inDriver = new ReadyValidSource(c.io.in, c.clock)
      val outDriver = new ReadyValidSink(c.io.out, c.clock)
      inDriver.enqueue(c.io.in.bits.Lit(15.U, 6.U))
      outDriver.dequeueExpect(3.U)
    }
  }
}

class T2GcdScalaCheckSpec extends FlatSpec with GeneratorDrivenPropertyChecks with ChiselScalatestTester {
  behavior of "Gcd with Testers2"

  it should "compute gcd" in {
    // Again, we need our reference expected outputs
    def gcd(a: Int,b: Int): Int = {
      if(b ==0) a else gcd(b, a%b)
    }

    // ScalaCheck will run a few test with randomly chosen inputs from the generator
    val inputInts = for (n <- Gen.choose(1, 256)) yield n

    test(new Gcd(32)) { c =>
      val inDriver = new ReadyValidSource(c.io.in, c.clock)
      val outDriver = new ReadyValidSink(c.io.out, c.clock)
      forAll (inputInts, inputInts) { (a: Int, b: Int) =>
        inDriver.enqueue(c.io.in.bits.Lit(a.U, b.U))
        outDriver.dequeueExpect(gcd(a, b).U)
      }
    }
  }
}
