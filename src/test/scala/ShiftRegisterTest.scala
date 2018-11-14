package ccc18TestIntensiveExample

import org.scalatest._

import chisel3._
import chisel3.experimental._
import chisel3.util._
import chisel3.tester._

class ShiftRegisterTest extends FlatSpec with ChiselScalatestTester {
  behavior of "Testers2"

  it should "test shift registers with abstractions" in {
    // Define a test helper
    def shiftTest(in: UInt, out: UInt, clk: Clock, value: UInt) {
      timescope {
        in.poke(value)
        clk.step(1)
      }
      clk.step(3)
      out.expect(value)
    }

    // In testers2 we can also inline the Module definition
    // and it works with MultiIOModules, which can have multiple top-level IOs
    test(new MultiIOModule {
      val in = IO(Input(UInt(8.W)))
      val out = IO(Output(UInt(8.W)))
      out := ShiftRegister(in, 4)
    }) { c =>
      fork { shiftTest(c.in, c.out, c.clock, 42.U) }
      c.clock.step(1)
      fork { shiftTest(c.in, c.out, c.clock, 43.U) }
      c.clock.step(1)
      fork { shiftTest(c.in, c.out, c.clock, 44.U) }
      c.clock.step(1)
      fork { shiftTest(c.in, c.out, c.clock, 45.U) }
      c.clock.step(1)
      fork { shiftTest(c.in, c.out, c.clock, 46.U) }
      c.clock.step(1)
      fork { shiftTest(c.in, c.out, c.clock, 47.U) }.join
    }
  }
}
