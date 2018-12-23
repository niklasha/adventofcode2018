package se.hallqvist.adventofcode2018.day19

import org.scalatest._

import scala.collection.mutable

/*
--- Day 19: Go With The Flow ---
With the Elves well on their way constructing the North Pole base, you turn your attention back to understanding the inner workings of programming the device.

You can't help but notice that the device's opcodes don't contain any flow control like jump instructions. The device's manual goes on to explain:

"In programs where flow control is required, the instruction pointer can be bound to a register so that it can be manipulated directly. This way, setr/seti can function as absolute jumps, addr/addi can function as relative jumps, and other opcodes can cause truly fascinating effects."

This mechanism is achieved through a declaration like #ip 1, which would modify register 1 so that accesses to it let the program indirectly access the instruction pointer itself. To compensate for this kind of binding, there are now six registers (numbered 0 through 5); the five not bound to the instruction pointer behave as normal. Otherwise, the same rules apply as the last time you worked with this device.

When the instruction pointer is bound to a register, its value is written to that register just before each instruction is executed, and the value of that register is written back to the instruction pointer immediately after each instruction finishes execution. Afterward, move to the next instruction by adding one to the instruction pointer, even if the value in the instruction pointer was just updated by an instruction. (Because of this, instructions must effectively set the instruction pointer to the instruction before the one they want executed next.)

The instruction pointer is 0 during the first instruction, 1 during the second, and so on. If the instruction pointer ever causes the device to attempt to load an instruction outside the instructions defined in the program, the program instead immediately halts. The instruction pointer starts at 0.

It turns out that this new information is already proving useful: the CPU in the device is not very powerful, and a background process is occupying most of its time. You dump the background process' declarations and instructions to a file (your puzzle input), making sure to use the names of the opcodes rather than the numbers.

For example, suppose you have the following program:

#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5
When executed, the following instructions are executed. Each line contains the value of the instruction pointer at the time the instruction started, the values of the six registers before executing the instructions (in square brackets), the instruction itself, and the values of the six registers after executing the instruction (also in square brackets).

ip=0 [0, 0, 0, 0, 0, 0] seti 5 0 1 [0, 5, 0, 0, 0, 0]
ip=1 [1, 5, 0, 0, 0, 0] seti 6 0 2 [1, 5, 6, 0, 0, 0]
ip=2 [2, 5, 6, 0, 0, 0] addi 0 1 0 [3, 5, 6, 0, 0, 0]
ip=4 [4, 5, 6, 0, 0, 0] setr 1 0 0 [5, 5, 6, 0, 0, 0]
ip=6 [6, 5, 6, 0, 0, 0] seti 9 0 5 [6, 5, 6, 0, 0, 9]
In detail, when running this program, the following events occur:

The first line (#ip 0) indicates that the instruction pointer should be bound to register 0 in this program. This is not an instruction, and so the value of the instruction pointer does not change during the processing of this line.
The instruction pointer contains 0, and so the first instruction is executed (seti 5 0 1). It updates register 0 to the current instruction pointer value (0), sets register 1 to 5, sets the instruction pointer to the value of register 0 (which has no effect, as the instruction did not modify register 0), and then adds one to the instruction pointer.
The instruction pointer contains 1, and so the second instruction, seti 6 0 2, is executed. This is very similar to the instruction before it: 6 is stored in register 2, and the instruction pointer is left with the value 2.
The instruction pointer is 2, which points at the instruction addi 0 1 0. This is like a relative jump: the value of the instruction pointer, 2, is loaded into register 0. Then, addi finds the result of adding the value in register 0 and the value 1, storing the result, 3, back in register 0. Register 0 is then copied back to the instruction pointer, which will cause it to end up 1 larger than it would have otherwise and skip the next instruction (addr 1 2 3) entirely. Finally, 1 is added to the instruction pointer.
The instruction pointer is 4, so the instruction setr 1 0 0 is run. This is like an absolute jump: it copies the value contained in register 1, 5, into register 0, which causes it to end up in the instruction pointer. The instruction pointer is then incremented, leaving it at 6.
The instruction pointer is 6, so the instruction seti 9 0 5 stores 9 into register 5. The instruction pointer is incremented, causing it to point outside the program, and so the program ends.
What value is left in register 0 when the background process halts?

--- Part Two ---
A new background process immediately spins up in its place. It appears identical, but on closer inspection, you notice that this time, register 0 started with the value 1.

What value is left in register 0 when this new background process halts?

*/

class Day19 extends FlatSpec {
  val input = """addi 4 16 4
                |seti 1 5 1
                |seti 1 2 2
                |mulr 1 2 3
                |eqrr 3 5 3
                |addr 3 4 4
                |addi 4 1 4
                |addr 1 0 0
                |addi 2 1 2
                |gtrr 2 5 3
                |addr 4 3 4
                |seti 2 7 4
                |addi 1 1 1
                |gtrr 1 5 3
                |addr 3 4 4
                |seti 1 9 4
                |mulr 4 4 4
                |addi 5 2 5
                |mulr 5 5 5
                |mulr 4 5 5
                |muli 5 11 5
                |addi 3 1 3
                |mulr 3 4 3
                |addi 3 18 3
                |addr 5 3 5
                |addr 4 0 4
                |seti 0 3 4
                |setr 4 2 3
                |mulr 3 4 3
                |addr 4 3 3
                |mulr 4 3 3
                |muli 3 14 3
                |mulr 3 4 3
                |addr 5 3 5
                |seti 0 4 0
                |seti 0 5 4""".stripMargin

  abstract class Opcode {
    def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long): Map[Long, Long]
  }
  case class Addr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) + rs(b))
  }
  case class Addi() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) + b)
  }
  case class Mulr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) * rs(b))
  }
  case class Muli() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) * b)
  }
  case class Banr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) & rs(b))
  }
  case class Bani() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) & b)
  }
  case class Borr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) | rs(b))
  }
  case class Bori() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a) | b)
  }
  case class Setr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, rs(a))
  }
  case class Seti() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, a)
  }
  case class Gtir() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, if (a > rs(b)) 1 else 0)
  }
  case class Gtri() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, if (rs(a) > b) 1 else 0)
  }
  case class Gtrr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, if (rs(a) > rs(b)) 1 else 0)
  }
  case class Eqir() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, if (a == rs(b)) 1 else 0)
  }
  case class Eqri() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, if (rs(a) == b) 1 else 0)
  }
  case class Eqrr() extends Opcode {
    override def eval(rs: Map[Long, Long], a : Long, b: Long, c: Long) =
      rs.updated(c, if (rs(a) == rs(b)) 1 else 0)
  }

  val all = Map(
    "addr" -> Addr(), "addi" -> Addi(), "mulr" -> Mulr(), "muli" -> Muli(),
    "banr" -> Banr(), "bani" -> Bani(), "borr" -> Borr(), "bori" -> Bori(),
    "setr" -> Setr(), "seti" -> Seti(), "gtir" -> Gtir(), "gtri" -> Gtri(),
    "gtrr" -> Gtrr(), "eqir" -> Eqir(), "eqri" -> Eqri(), "eqrr" -> Eqrr()
  )

  def part1(ip: Long, s: String, ir: String) = {
    def parseRegs(rs: String) =
      rs.split(", ").map(_.toLong).zipWithIndex.map { case (a, b) => (b.toLong, a) }.
        toMap
    def regs(rs: Map[Long, Long]) = rs.toSeq.sortBy(_._1).map(_._2).mkString(", ")
    val p =
      s.split("\n").zipWithIndex.map { case (o, i) => (i.toLong -> o.split(" ")) }.
        toMap
    var rs = parseRegs(ir)
    val h = mutable.Map[Long, Map[Long, Long]]()
    while (rs(ip) >= 0 && rs(ip) < p.size) {
      val i = p(rs(ip))
      rs = all(i(0)).eval(rs.toMap, i(1).toLong, i(2).toLong, i(3).toLong)
      rs = rs.updated(ip, rs(ip) + 1)
    }
//    println(s"${regs(rs)}")
    rs(0)
  }

  "part1" should "satisfy the examples given" in {
    assertResult(7)(part1(0, """seti 5 0 1
                               |seti 6 0 2
                               |addi 0 1 0
                               |addr 1 2 3
                               |setr 1 0 0
                               |seti 8 0 4
                               |seti 9 0 5""".stripMargin, "0, 0, 0, 0, 0, 0"))
  }

  "part1" should "succeed" in { info(part1(4, input, "0, 0, 0, 0, 0, 0").toString) }

  // Manually decompiled program showed it computed the sum of the distinct
  // prime factors of register 5 when it reached IP 1
  // XXX It would be nice if there was a peephole optimizer that rewrote the
  // innermost loop, getting it to be O(N) instead of O(N^2).
  def part2(ip: Long, s: String, ir: String) = {
    def parseRegs(rs: String) =
      rs.split(", ").map(_.toLong).zipWithIndex.map { case (a, b) => (b.toLong, a) }.
        toMap
    def regs(rs: Map[Long, Long]) = rs.toSeq.sortBy(_._1).map(_._2).mkString(", ")
    val p =
      s.split("\n").zipWithIndex.map { case (o, i) => (i.toLong -> o.split(" ")) }.
        toMap
    var rs = parseRegs(ir)
    val h = mutable.Map[Long, Map[Long, Long]]()
    while (rs(ip) != 1) {
      val i = p(rs(ip))
      rs = all(i(0)).eval(rs.toMap, i(1).toLong, i(2).toLong, i(3).toLong)
      rs = rs.updated(ip, rs(ip) + 1)
    }
    // Decompiled logic
    (1 to rs(5).toInt).filter(rs(5) % _ == 0).sum
  }

  "part2" should "succeed" in { info(part2(4, input, "1, 0, 0, 0, 0, 0").toString) }
}
