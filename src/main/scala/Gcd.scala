package ccc18TestIntensiveExample

import chisel3._
import chisel3.util._

class Gcd(width: Int) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Bundle {
      val a = UInt(width.W)
      val b = UInt(width.W)
    }))
    val out = Decoupled(UInt(width.W))
  })

  val x = RegInit(0.U(width.W))
  val y = Reg(UInt(width.W))

  when (x > y)   { x := x -% y }
  .otherwise     { y := y -% x }

  when (io.in.valid) { x := io.in.bits.a; y := io.in.bits.b }
  io.out.bits := x
  io.out.valid := y === 0.U
  io.in.ready := y === 0.U

  // For debugging, we can have internal state printed out every cycle.
  // See https://github.com/freechipsproject/chisel3/wiki/Printing-in-Chisel
//  printf(p"x=$x, y=$y, v=${io.out.valid}\n")

  // Prints can also be gated on a condition, eg if we only wanted to print when we have a solution
//  when (io.out.valid) {
//    printf(p"result=${io.out.bits}, v=${io.out.valid}\n")
//  }

  // Note: we kind of ignore output ready here, so this is a misuse of Decoupled
  // since backpressure is ignored.
}
