package se.hallqvist.adventofcode2018.day21

import org.scalatest._

import scala.collection.mutable

/*
--- Day 21: Chronal Conversion ---
You should have been watching where you were going, because as you wander the new North Pole base, you trip and fall into a very deep hole!

Just kidding. You're falling through time again.

If you keep up your current pace, you should have resolved all of the temporal anomalies by the next time the device activates. Since you have very little interest in browsing history in 500-year increments for the rest of your life, you need to find a way to get back to your present time.

After a little research, you discover two important facts about the behavior of the device:

First, you discover that the device is hard-wired to always send you back in time in 500-year increments. Changing this is probably not feasible.

Second, you discover the activation system (your puzzle input) for the time travel module. Currently, it appears to run forever without halting.

If you can cause the activation system to halt at a specific moment, maybe you can make the device send you so far back in time that you cause an integer underflow in time itself and wrap around back to your current time!

The device executes the program as specified in manual section one and manual section two.

Your goal is to figure out how the program works and cause it to halt. You can only control register 0; every other register begins at 0 as usual.

Because time travel is a dangerous activity, the activation system begins with a few instructions which verify that bitwise AND (via bani) does a numeric operation and not an operation as if the inputs were interpreted as strings. If the test fails, it enters an infinite loop re-running the test instead of allowing the program to execute normally. If the test passes, the program continues, and assumes that all other bitwise operations (banr, bori, and borr) also interpret their inputs as numbers. (Clearly, the Elves who wrote this system were worried that someone might introduce a bug while trying to emulate this system with a scripting language.)

What is the lowest non-negative integer value for register 0 that causes the program to halt after executing the fewest instructions? (Executing the same instruction multiple times counts as multiple instructions executed.)

--- Part Two ---
In order to determine the timing window for your underflow exploit, you also need an upper bound:

What is the lowest non-negative integer value for register 0 that causes the program to halt after executing the most instructions? (The program must actually halt; running forever does not count as halting.)
*/

class Day21 extends FlatSpec {
  val input = """seti 123 0 3
		|bani 3 456 3
		|eqri 3 72 3
		|addr 3 4 4
		|seti 0 0 4
		|seti 0 5 3
		|bori 3 65536 2
		|seti 10736359 9 3
		|bani 2 255 1
		|addr 3 1 3
		|bani 3 16777215 3
		|muli 3 65899 3
		|bani 3 16777215 3
		|gtir 256 2 1
		|addr 1 4 4
		|addi 4 1 4
		|seti 27 2 4
		|seti 0 3 1
		|addi 1 1 5
		|muli 5 256 5
		|gtrr 5 2 5
		|addr 5 4 4
		|addi 4 1 4
		|seti 25 8 4
		|addi 1 1 1
		|seti 17 6 4
		|setr 1 5 2
		|seti 7 7 4
		|eqrr 3 0 1
		|addr 1 4 4
		|seti 5 1 4""".stripMargin

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

  val trace = false

  def part1(ip: Int, s: String) = {
    def parseRegs(rs: String) =
      rs.split(", ").map(_.toLong).zipWithIndex.map { case (a, b) => (b.toLong, a) }.
	toMap
    def regs(rs: Map[Long, Long]) = rs.toSeq.sortBy(_._1).map(_._2).mkString(", ")
    val p =
      s.split("\n").zipWithIndex.map { case (o, i) => (i.toLong -> o.split(" ")) }.
	toMap
    var rs = parseRegs("0, 0, 0, 0, 0, 0")
    while (rs(ip) != 30) {
      val i = p(rs(ip))
      if (trace) println(f"${rs(ip)}%02d  ${i.mkString(" ")}    ${regs(rs)}")
      rs = all(i(0)).eval(rs, i(1).toLong, i(2).toLong, i(3).toLong)
      rs = rs.updated(ip, rs(ip) + 1)
    }
    rs(3)
  }

  "part1" should "succeed" in { info(part1(4, input).toString) }

  def part2(ip: Int, s: String) = {
    def parseRegs(rs: String) =
      rs.split(", ").map(_.toLong).zipWithIndex.map { case (a, b) => (b.toLong, a) }.
	toMap
    def regs(rs: Map[Long, Long]) = rs.toSeq.sortBy(_._1).map(_._2).mkString(", ")
    val p =
      s.split("\n").zipWithIndex.map { case (o, i) => (i.toLong -> o.split(" ")) }.
	toMap
    val seen = mutable.Set[Long]()
    var rs = parseRegs(s"-1, 0, 0, 0, 0, 0")
    var l: Option[Long] = None
    while (rs(ip) != 30 || !seen(rs(3))) {
      if (rs(ip) == 30) { seen.add(rs(3)); l = Some(rs(3)) }
      val i = p(rs(ip))
      if (trace) println(f"${rs(ip)}%02d  ${i.mkString(" ")}    ${regs(rs)}")
//      // Peephole optimisation, instructions 17..26 implements division of r2
//      // by 256, saves 15 mins of running time on a typical MacBook Pro 2014
//      if (rs(ip) == 17) {
//	rs = rs.updated(2, rs(2) / 256).updated(ip, 27)
//	// XXX approximation
//      } else {
	rs = all(i(0)).eval(rs, i(1).toLong, i(2).toLong, i(3).toLong)
	rs = rs.updated(ip, rs(ip) + 1)
//      }
    }
    l
  }

  "part2" should "succeed" in { info(part2(4, input).toString) }
}
