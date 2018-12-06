package se.hallqvist.adventofcode2018.day06

import org.scalatest._

/*
--- Day 6: Chronal Coordinates ---
The device on your wrist beeps several times, and once again you feel like you're falling.

"Situation critical," the device announces. "Destination indeterminate. Chronal interference detected. Please specify new target coordinates."

The device then produces a list of coordinates (your puzzle input). Are they places it thinks are safe or dangerous? It recommends you check manual page 729. The Elves did not give you a manual.

If they're dangerous, maybe you can minimize the danger by finding the coordinate that gives the largest distance from the other points.

Using only the Manhattan distance, determine the area around each coordinate by counting the number of integer X,Y locations that are closest to that coordinate (and aren't tied in distance to any other coordinate).

Your goal is to find the size of the largest area that isn't infinite. For example, consider the following list of coordinates:

1, 1
1, 6
8, 3
3, 4
5, 5
8, 9
If we name these coordinates A through F, we can draw them on a grid, putting 0,0 at the top left:

..........
.A........
..........
........C.
...D......
.....E....
.B........
..........
..........
........F.
This view is partial - the actual grid extends infinitely in all directions. Using the Manhattan distance, each location's closest coordinate can be determined, shown here in lowercase:

aaaaa.cccc
aAaaa.cccc
aaaddecccc
aadddeccCc
..dDdeeccc
bb.deEeecc
bBb.eeee..
bbb.eeefff
bbb.eeffff
bbb.ffffFf
Locations shown as . are equally far from two or more coordinates, and so they don't count as being closest to any.

In this example, the areas of coordinates A, B, C, and F are infinite - while not shown here, their areas extend forever outside the visible grid. However, the areas of coordinates D and E are finite: D is closest to 9 locations, and E is closest to 17 (both including the coordinate's location itself). Therefore, in this example, the size of the largest area is 17.

What is the size of the largest area that isn't infinite?

--- Part Two ---
On the other hand, if the coordinates are safe, maybe the best you can do is try to find a region near as many coordinates as possible.

For example, suppose you want the sum of the Manhattan distance to all of the coordinates to be less than 32. For each location, add up the distances to all of the given coordinates; if the total of those distances is less than 32, that location is within the desired region. Using the same coordinates as above, the resulting region looks like this:

..........
.A........
..........
...###..C.
..#D###...
..###E#...
.B.###....
..........
..........
........F.
In particular, consider the highlighted location 4,3 located at the top middle of the region. Its calculation is as follows, where abs() is the absolute value function:

Distance to coordinate A: abs(4-1) + abs(3-1) =  5
Distance to coordinate B: abs(4-1) + abs(3-6) =  6
Distance to coordinate C: abs(4-8) + abs(3-3) =  4
Distance to coordinate D: abs(4-3) + abs(3-4) =  2
Distance to coordinate E: abs(4-5) + abs(3-5) =  3
Distance to coordinate F: abs(4-8) + abs(3-9) = 10
Total distance: 5 + 6 + 4 + 2 + 3 + 10 = 30
Because the total distance to all coordinates (30) is less than 32, the location is within the region.

This region, which also includes coordinates D and E, has a total size of 16.

Your actual region will need to be much larger than this example, though, instead including all locations with a total distance of less than 10000.

What is the size of the region containing all locations which have a total distance to all given coordinates of less than 10000?
*/

class Day06 extends FlatSpec {
  val input = """162, 168
                |86, 253
                |288, 359
                |290, 219
                |145, 343
                |41, 301
                |91, 214
                |166, 260
                |349, 353
                |178, 50
                |56, 79
                |273, 104
                |173, 118
                |165, 47
                |284, 235
                |153, 69
                |116, 153
                |276, 325
                |170, 58
                |211, 328
                |238, 346
                |333, 299
                |119, 328
                |173, 289
                |44, 223
                |241, 161
                |225, 159
                |266, 209
                |293, 95
                |89, 86
                |281, 289
                |50, 253
                |75, 347
                |298, 241
                |88, 158
                |40, 338
                |291, 156
                |330, 88
                |349, 289
                |165, 102
                |232, 131
                |338, 191
                |178, 335
                |318, 107
                |335, 339
                |153, 156
                |88, 119
                |163, 268
                |159, 183
                |162, 134""".stripMargin

  type Field = Map[(Int, Int), Set[Int]]
  val emptyField = Map[(Int, Int), Set[Int]]()

  def part1(cs: String) = {
    val i = cs.split("\n").map(_.split(", ").map(_.toInt)).zipWithIndex.
      map { case (a, i) => (a(0), a(1)) -> Set(i) }.toMap
    val xs = i.keys.map(_._1)
    val ys = i.keys.map(_._2)
    val (n, e, s, w) = (ys.min, xs.max, ys.max, xs.min)
    def v(c: (Int, Int)) = c._1 >= w && c._1 <= e && c._2 >= n && c._2 <= s
    def ns(c: (Int, Int)) =
      Set((c._1 - 1, c._2), (c._1 + 1, c._2), (c._1, c._2 - 1), (c._1, c._2 + 1)).filter(v)
    def a(f: Field, e: ((Int, Int), Set[Int])) =
      f + (if (f contains e._1) (e._1 -> (f(e._1) ++ e._2)) else e)
    val ccs = i.filter(m => m._1._1 > w && m._1._1 < e && m._1._2 > n && m._1._2 < s).flatMap(_._2)
    def ncs(f: Field, x: Field) =
      f.map(e => ns(e._1).filter(c => !(x contains c)).map(_ -> e._2).toMap)
    def r(f: Field, x: Field): Field = {
      val n = ncs(f, x).foldLeft(emptyField) { (f, nf) => nf.foldLeft(f)((f, e) => a(f, e)) }
      if (n.size == 0) x else r(n, n ++ x)
    }
    val fin = r(i, i)
    ccs.map(c => fin.filter(_._2 == Set(c)).size).max
  }

  "part1" should "satisfy the examples given" in {
    assertResult(17)(part1("""1, 1
                             |1, 6
                             |8, 3
                             |3, 4
                             |5, 5
                             |8, 9""".stripMargin))
  }

  "part1" should "succeed" in { info(part1(input).toString) }

  def part2(l: Int, cs: String) = {
    import scala.math.abs
    def d(a: (Int, Int), b: (Int, Int)) =  abs(a._1 - b._1) + abs(a._2 - b._2)
    val i = cs.split("\n").map(_.split(", ").map(_.toInt)).map(c => (c(0), c(1)))
    val xs = i.map(_._1)
    val ys = i.map(_._2)
    val (n, e, s, w) = (ys.min, xs.max, ys.max, xs.min)
    (n to s).flatMap(y => (w to e).map(x => (x, y))).filter(c => i.map{oc => d(c, oc) }.sum < l).size
  }

  "part2" should "satisfy the examples given" in {
    assertResult(16)(part2(32, """1, 1
                                 |1, 6
                                 |8, 3
                                 |3, 4
                                 |5, 5
                                 |8, 9""".stripMargin))
  }

  "part2" should "succeed" in {  info(part2(10000, input).toString) }
}
