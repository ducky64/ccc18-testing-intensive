package ccc18TestIntensiveExample

import org.scalatest._
import chisel3.iotesters._

class GcdTester(c: Gcd) extends PeekPokeTester(c)  {
  // Poke input bits
  poke(c.io.in.bits.a, 15)
  poke(c.io.in.bits.b, 6)

  // Enqueue inputs by cycling valid high for one cycle
  poke(c.io.in.valid, 1)
  step(1)
  poke(c.io.in.valid, 0)

  // Wait for output ready
  while (peek(c.io.out.valid) == 0) {
    // Since this code runs in lockstep with simulation, we can have it print out peeked values
//    println(s"out.valid=${peek(c.io.out.valid)}  out.bits=${peek(c.io.out.bits)}")
    step(1)
  }

  // Check output is correct
  expect(c.io.out.bits, 3)

  // Needed if we want a print to execute on the last cycle
  step(1)
}

class GcdSpec extends FlatSpec with Matchers {
  behavior of "Gcd"

  it should "compute gcd" in {
    // This can alternatively be run with Verilator, which will dump VCDs by default
//    Driver.execute(Array("--backend-name", "verilator"), () => new Gcd(32)) { c =>
    Driver.execute(Array(), () => new Gcd(32)) { c =>
      new GcdTester(c)
    } should be(true)
  }
}
