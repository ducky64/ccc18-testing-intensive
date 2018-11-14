package ccc18TestIntensiveExample

import org.scalatest._
import chisel3.iotesters._

class GcdTester(c: Gcd) extends PeekPokeTester(c)  {
  poke(c.io.in.bits.a, 15)
  poke(c.io.in.bits.b, 6)
  poke(c.io.in.valid, 1)
  step(1)
  poke(c.io.in.valid, 0)

  while (peek(c.io.out.valid) == 0) {
    step(1)
  }
  expect(c.io.out.bits, 3)
}

class GcdSpec extends FlatSpec with Matchers {
  behavior of "GCDSpec"

  it should "compute gcd" in {
    Driver.execute(() => new Gcd(32), new TesterOptionsManager) { c =>
      new GcdTester(c)
    } should be(true)
  }
}
