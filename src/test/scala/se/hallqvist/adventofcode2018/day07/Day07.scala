package se.hallqvist.adventofcode2018.day07

import org.scalatest._

import scala.collection.{SortedMap, SortedSet, mutable}

/*
--- Day 7: The Sum of Its Parts ---
You find yourself standing on a snow-covered coastline; apparently, you landed a little off course. The region is too hilly to see the North Pole from here, but you do spot some Elves that seem to be trying to unpack something that washed ashore. It's quite cold out, so you decide to risk creating a paradox by asking them for directions.

"Oh, are you the search party?" Somehow, you can understand whatever Elves from the year 1018 speak; you assume it's Ancient Nordic Elvish. Could the device on your wrist also be a translator? "Those clothes don't look very warm; take this." They hand you a heavy coat.

"We do need to find our way back to the North Pole, but we have higher priorities at the moment. You see, believe it or not, this box contains something that will solve all of Santa's transportation problems - at least, that's what it looks like from the pictures in the instructions." It doesn't seem like they can read whatever language it's in, but you can: "Sleigh kit. Some assembly required."

"'Sleigh'? What a wonderful name! You must help us assemble this 'sleigh' at once!" They start excitedly pulling more parts out of the box.

The instructions specify a series of steps and requirements about which steps must be finished before others can begin (your puzzle input). Each step is designated by a single letter. For example, suppose you have the following instructions:

Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.
Visually, these requirements look like this:


  -->A--->B--
 /    \      \
C      -->D----->E
 \           /
  ---->F-----
Your first goal is to determine the order in which the steps should be completed. If more than one step is ready, choose the step which is first alphabetically. In this example, the steps would be completed as follows:

Only C is available, and so it is done first.
Next, both A and F are available. A is first alphabetically, so it is done next.
Then, even though F was available earlier, steps B and D are now also available, and B is the first alphabetically of the three.
After that, only D and F are available. E is not available because only some of its prerequisites are complete. Therefore, D is completed next.
F is the only choice, so it is done next.
Finally, E is completed.
So, in this example, the correct order is CABDFE.

In what order should the steps in your instructions be completed?

--- Part Two ---
As you're about to begin construction, four of the Elves offer to help. "The sun will set soon; it'll go faster if we work together." Now, you need to account for multiple people working on steps simultaneously. If multiple steps are available, workers should still begin them in alphabetical order.

Each step takes 60 seconds plus an amount corresponding to its letter: A=1, B=2, C=3, and so on. So, step A takes 60+1=61 seconds, while step Z takes 60+26=86 seconds. No time is required between steps.

To simplify things for the example, however, suppose you only have help from one Elf (a total of two workers) and that each step takes 60 fewer seconds (so that step A takes 1 second and step Z takes 26 seconds). Then, using the same instructions as above, this is how each second would be spent:

Second   Worker 1   Worker 2   Done
   0        C          .
   1        C          .
   2        C          .
   3        A          F       C
   4        B          F       CA
   5        B          F       CA
   6        D          F       CAB
   7        D          F       CAB
   8        D          F       CAB
   9        D          .       CABF
  10        E          .       CABFD
  11        E          .       CABFD
  12        E          .       CABFD
  13        E          .       CABFD
  14        E          .       CABFD
  15        .          .       CABFDE
Each row represents one second of time. The Second column identifies how many seconds have passed as of the beginning of that second. Each worker column shows the step that worker is currently doing (or . if they are idle). The Done column shows completed steps.

Note that the order of the steps has changed; this is because steps now take time to finish and multiple workers can begin multiple steps simultaneously.

In this example, it would take 15 seconds for two workers to complete these steps.

With 5 workers and the 60+ second step durations described above, how long will it take to complete all of the steps?

*/

class Day07 extends FlatSpec {
  val input = """Step L must be finished before step T can begin.
                |Step B must be finished before step I can begin.
                |Step A must be finished before step T can begin.
                |Step F must be finished before step T can begin.
                |Step D must be finished before step J can begin.
                |Step N must be finished before step R can begin.
                |Step J must be finished before step U can begin.
                |Step C must be finished before step Z can begin.
                |Step V must be finished before step H can begin.
                |Step W must be finished before step H can begin.
                |Step H must be finished before step I can begin.
                |Step R must be finished before step K can begin.
                |Step M must be finished before step X can begin.
                |Step T must be finished before step O can begin.
                |Step Q must be finished before step P can begin.
                |Step I must be finished before step E can begin.
                |Step E must be finished before step Y can begin.
                |Step K must be finished before step Y can begin.
                |Step X must be finished before step O can begin.
                |Step U must be finished before step G can begin.
                |Step Z must be finished before step P can begin.
                |Step O must be finished before step S can begin.
                |Step S must be finished before step G can begin.
                |Step Y must be finished before step G can begin.
                |Step P must be finished before step G can begin.
                |Step C must be finished before step P can begin.
                |Step N must be finished before step K can begin.
                |Step E must be finished before step U can begin.
                |Step C must be finished before step T can begin.
                |Step F must be finished before step I can begin.
                |Step Q must be finished before step Y can begin.
                |Step E must be finished before step S can begin.
                |Step T must be finished before step P can begin.
                |Step K must be finished before step O can begin.
                |Step H must be finished before step Y can begin.
                |Step Q must be finished before step G can begin.
                |Step K must be finished before step P can begin.
                |Step R must be finished before step O can begin.
                |Step W must be finished before step T can begin.
                |Step O must be finished before step P can begin.
                |Step Q must be finished before step X can begin.
                |Step D must be finished before step I can begin.
                |Step R must be finished before step T can begin.
                |Step I must be finished before step K can begin.
                |Step I must be finished before step G can begin.
                |Step K must be finished before step G can begin.
                |Step N must be finished before step U can begin.
                |Step A must be finished before step Y can begin.
                |Step X must be finished before step Y can begin.
                |Step N must be finished before step H can begin.
                |Step R must be finished before step Z can begin.
                |Step C must be finished before step Q can begin.
                |Step F must be finished before step O can begin.
                |Step B must be finished before step Z can begin.
                |Step Z must be finished before step S can begin.
                |Step U must be finished before step S can begin.
                |Step A must be finished before step K can begin.
                |Step B must be finished before step N can begin.
                |Step T must be finished before step E can begin.
                |Step A must be finished before step N can begin.
                |Step F must be finished before step V can begin.
                |Step D must be finished before step C can begin.
                |Step M must be finished before step P can begin.
                |Step D must be finished before step V can begin.
                |Step V must be finished before step Q can begin.
                |Step O must be finished before step Y can begin.
                |Step W must be finished before step I can begin.
                |Step E must be finished before step Z can begin.
                |Step B must be finished before step R can begin.
                |Step C must be finished before step X can begin.
                |Step J must be finished before step T can begin.
                |Step A must be finished before step W can begin.
                |Step Q must be finished before step U can begin.
                |Step I must be finished before step Z can begin.
                |Step N must be finished before step P can begin.
                |Step W must be finished before step U can begin.
                |Step Y must be finished before step P can begin.
                |Step J must be finished before step P can begin.
                |Step F must be finished before step Q can begin.
                |Step L must be finished before step M can begin.
                |Step E must be finished before step G can begin.
                |Step B must be finished before step P can begin.
                |Step H must be finished before step X can begin.
                |Step W must be finished before step S can begin.
                |Step N must be finished before step Q can begin.
                |Step J must be finished before step I can begin.
                |Step L must be finished before step F can begin.
                |Step S must be finished before step Y can begin.
                |Step J must be finished before step X can begin.
                |Step A must be finished before step H can begin.
                |Step T must be finished before step U can begin.
                |Step H must be finished before step Z can begin.
                |Step W must be finished before step R can begin.
                |Step X must be finished before step Z can begin.
                |Step T must be finished before step Y can begin.
                |Step H must be finished before step T can begin.
                |Step K must be finished before step U can begin.
                |Step H must be finished before step G can begin.
                |Step U must be finished before step O can begin.
                |Step W must be finished before step P can begin.
                |Step A must be finished before step D can begin.""".stripMargin
  val p = """Step (.+) must be finished before step (.+) can begin\.""".r

  def part1(s: String) = {
    val m = s.split("\n").map(_ match { case p(a, b) => (a, b) })
    val ns = m.flatMap(e => Seq(e._1, e._2)).toSet
    val d = m.foldLeft(Map[String, Set[String]]())(
      (f, e) => f + (e._1 -> ((if (f contains e._1) f(e._1) else Set[String]()) + e._2)))
    def r(p: String, ns: Set[String], d: Map[String, Set[String]]): String = {
      if (ns.isEmpty) p else {
        val n = (ns -- d.flatMap(_._2)).toSeq.sorted.head
        r(p + n, ns - n, d.map { case (a, b) => (a -> (b - n)) }.filter { case (a, b) => a != n && !b.isEmpty })
      }
    }
    r("", ns, d)
  }

  "part1" should "satisfy the examples given" in {
    assertResult("CABDFE")(part1("""Step C must be finished before step A can begin.
                                   |Step C must be finished before step F can begin.
                                   |Step A must be finished before step B can begin.
                                   |Step A must be finished before step D can begin.
                                   |Step B must be finished before step E can begin.
                                   |Step D must be finished before step E can begin.
                                   |Step F must be finished before step E can begin.
                                   |""".stripMargin))
  }

  "part1" should "succeed" in { info(part1(input).toString)}

  // Ugly iterating solution
  def part2(wc: Int, o: Int, s: String) = {
    val m = s.split("\n").map(_ match { case p(a, b) => (a, b) })
    var ns = m.flatMap(e => Seq(e._1, e._2)).toSet
    var dm = m.foldLeft(Map[String, Set[String]]())(
      (f, e) => f + (e._1 -> ((if (f contains e._1) f(e._1) else Set[String]()) + e._2)))
    def pt(n: String) = n.charAt(0) - 'A' + 1
    def cns(ns: Set[String], dm: Map[String, Set[String]]) =
      (ns -- dm.flatMap(_._2)).map(n => (n, pt(n))).toSeq.sortBy(_._2)
    var t = 0
    val fws = mutable.Set[Int]() ++ (1 to wc)
    case class Job(w: Int, n: String, d: Int)
    val ts = mutable.SortedMap[Int, Set[Job]]()
    while (!ns.isEmpty || !ts.isEmpty) {
      if (!fws.isEmpty)
        cns(ns, dm).zip(fws).map(e => Job(e._2, e._1._1, o + e._1._2)).map { j =>
          fws -= j.w
          ns -= j.n
          val et = t + j.d
          ts(et) = (if (ts contains et) ts(et) + j else Set(j))
        }
      val (nt, js) = ts.head
      ts -= nt
      t = nt
      js.map{ j =>
        fws += j.w
        dm = dm.map { case (a, b) => (a -> (b - j.n)) }.
          filter { case (a, b) => a != j.n && !b.isEmpty }
      }
    }
    t
  }

  "part2" should "satisfy the examples given" in {
    assertResult(15)(part2(2, 0, """Step C must be finished before step A can begin.
                                   |Step C must be finished before step F can begin.
                                   |Step A must be finished before step B can begin.
                                   |Step A must be finished before step D can begin.
                                   |Step B must be finished before step E can begin.
                                   |Step D must be finished before step E can begin.
                                   |Step F must be finished before step E can begin.
                                   |""".stripMargin))
  }

  "part2" should "succeed" in { info(part2(5, 60, input).toString)}
}
