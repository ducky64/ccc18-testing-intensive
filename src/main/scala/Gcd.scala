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
}
