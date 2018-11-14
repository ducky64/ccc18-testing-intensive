package ccc18TestIntensiveExample

import org.scalatest._
import chisel3.iotesters._

class GcdLoopTester(c: Gcd) extends PeekPokeTester(c)  {
  // Use a software GCD implementation to generate reference solutions
  // from https://gist.github.com/vsalvati/3947871
  // which basically implements Euclid's algorithm
  def gcd(a: Int,b: Int): Int = {
     if(b ==0) a else gcd(b, a%b)
  }

  // The testdriver code is just imperative software, so we can do loops
  // Here we spam a bunch of test inputs
  for {
    i <- 1 to 10
    j <- 1 to 10
  } {
    poke(c.io.in.bits.a, i)
    poke(c.io.in.bits.b, j)

    poke(c.io.in.valid, 1)
    step(1)
    poke(c.io.in.valid, 0)

    while (peek(c.io.out.valid) == 0) {
      step(1)
    }

    expect(c.io.out.bits, gcd(i, j))
  }
}

class GcdLoopSpec extends FlatSpec with Matchers {
  behavior of "GCDSpec"

  it should "compute gcd" in {
    Driver.execute(() => new Gcd(32), new TesterOptionsManager) { c =>
      new GcdLoopTester(c)
    } should be(true)
  }
}
