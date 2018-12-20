package se.hallqvist.adventofcode2018.day18

import org.scalatest._

import scala.collection.mutable

/*
--- Day 18: Settlers of The North Pole ---
On the outskirts of the North Pole base construction project, many Elves are collecting lumber.

The lumber collection area is 50 acres by 50 acres; each acre can be either open ground (.), trees (|), or a lumberyard (#). You take a scan of the area (your puzzle input).

Strange magic is at work here: each minute, the landscape looks entirely different. In exactly one minute, an open acre can fill with trees, a wooded acre can be converted to a lumberyard, or a lumberyard can be cleared to open ground (the lumber having been sent to other projects).

The change to each acre is based entirely on the contents of that acre as well as the number of open, wooded, or lumberyard acres adjacent to it at the start of each minute. Here, "adjacent" means any of the eight acres surrounding that acre. (Acres on the edges of the lumber collection area might have fewer than eight adjacent acres; the missing acres aren't counted.)

In particular:

An open acre will become filled with trees if three or more adjacent acres contained trees. Otherwise, nothing happens.
An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards. Otherwise, nothing happens.
An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open.
These changes happen across all acres simultaneously, each of them using the state of all acres at the beginning of the minute and changing to their new form by the end of that same minute. Changes that happen during the minute don't affect each other.

For example, suppose the lumber collection area is instead only 10 by 10 acres with this initial configuration:

Initial state:
.#.#...|#.
.....#|##|
.|..|...#.
..|#.....#
#.#|||#|#|
...#.||...
.|....|...
||...#|.#|
|.||||..|.
...#.|..|.

After 1 minute:
.......##.
......|###
.|..|...#.
..|#||...#
..##||.|#|
...#||||..
||...|||..
|||||.||.|
||||||||||
....||..|.

After 2 minutes:
.......#..
......|#..
.|.|||....
..##|||..#
..###|||#|
...#|||||.
|||||||||.
||||||||||
||||||||||
.|||||||||

After 3 minutes:
.......#..
....|||#..
.|.||||...
..###|||.#
...##|||#|
.||##|||||
||||||||||
||||||||||
||||||||||
||||||||||

After 4 minutes:
.....|.#..
...||||#..
.|.#||||..
..###||||#
...###||#|
|||##|||||
||||||||||
||||||||||
||||||||||
||||||||||

After 5 minutes:
....|||#..
...||||#..
.|.##||||.
..####|||#
.|.###||#|
|||###||||
||||||||||
||||||||||
||||||||||
||||||||||

After 6 minutes:
...||||#..
...||||#..
.|.###|||.
..#.##|||#
|||#.##|#|
|||###||||
||||#|||||
||||||||||
||||||||||
||||||||||

After 7 minutes:
...||||#..
..||#|##..
.|.####||.
||#..##||#
||##.##|#|
|||####|||
|||###||||
||||||||||
||||||||||
||||||||||

After 8 minutes:
..||||##..
..|#####..
|||#####|.
||#...##|#
||##..###|
||##.###||
|||####|||
||||#|||||
||||||||||
||||||||||

After 9 minutes:
..||###...
.||#####..
||##...##.
||#....###
|##....##|
||##..###|
||######||
|||###||||
||||||||||
||||||||||

After 10 minutes:
.||##.....
||###.....
||##......
|##.....##
|##.....##
|##....##|
||##.####|
||#####|||
||||#|||||
||||||||||
After 10 minutes, there are 37 wooded acres and 31 lumberyards. Multiplying the number of wooded acres by the number of lumberyards gives the total resource value after ten minutes: 37 * 31 = 1147.

What will the total resource value of the lumber collection area be after 10 minutes?

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
This important natural resource will need to last for at least thousands of years. Are the Elves collecting this lumber sustainably?

What will the total resource value of the lumber collection area be after 1000000000 minutes?
*/

class Day18 extends FlatSpec {
  val input = """.|#.#|..#...|..##||...|#..##..#..|#|....#.#|.|....
                |.||....#..#...|#....#.||....||...||...|..#|..||..|
                |......|.|.#.#.#..|.....#.###.....#........|.||..#|
                |..|.....||...#||#.#|#.....|##.|.|....|#....#|..#.#
                ||...#.|..#|#.#....|.#.#.|.#...#..#|#.....##|#..#.|
                |#....|#|......#.|||..#..#..||...#.#...|||##|..|#..
                |.#||.|....|.......#|##...|.#.....#.##...|.|.#...#|
                |....#|.|.|...##.......|#.....|..#......#...#|.#..#
                |...#.|....#.|.#...|||......##..|#|||#..#...|.|#.#.
                |.#..|...|..#.|##.#.#......#...||.||.#...|.#.#.#|..
                ||...#.|||...#|.#||.......|.##.....|..||...####...|
                |.##..|..##|...##.#...#...#.#|.|###...#............
                ||.....||...#.......|.#..|#.....|.|#.|..||.##.|#|..
                |.#..##|.#|.|..|.#..#.|.#..#|......##...#.#.......#
                |...#.##.|..|.#.....#....#..#.............|.|##||..
                |||.##.||.|.|..|..#.|.|.##|.|...|.#.|#.......#.|...
                |..|#.##..#|.#|#.#...........|.|........#...#|...#|
                |....|.#|..|.#|#|...|.|.#..#.....#|##|||##.#....#..
                |...###.#.....#.||......#|#..##...|#....#....|#|.#.
                |.##.|.##|.#.||....#|....|.#.|#.|....##..#.##|.....
                ||...|...#|....#....#...#|#...|..#.#.|.|.....|.#|..
                |.|.|.#.#.#|.#.|#....|.|###..#......|...|...#.|#...
                |..|...||.|..##|...|..|#|...|......#.||.#...#..|#.|
                |........|..#||..|....|.....|.|#..#....|#..|.#.....
                |#.|.|#....#...|..|....|.#.....||.....|..|........#
                |...||||....|.#.|....#..#....|###....|...#...##....
                ||||........|.#|.|......||#.|.....|.||#|.##....#|..
                |.....|#|#..||#...|##.|..||....####.|#.|..#....|.#.
                |.||..#||....#.....#.#|.|....|.##|..|.#..|##....##.
                |.|#.#|#|#|.....||..|.|.|.#......#..|.#..#..|.#||#.
                ||.|#.......|..#|#|....|.#.#.#.|...|.......##.|||#|
                |..|.....#...||.|....|##|...#..#.#.....|##|##.##...
                |.|.|..##.#|..|.|#.......#....#||.|...||#...|......
                ||.|##.#....|#..|....#..#..|##.|.##..#......#|##|..
                |..#....#.|#...#.#...|.....|.||.#.#|.#.|###..|..#.#
                |..|.##...........|..###.||.|.##.|....|.|.#|#.#.|#|
                |..|....|.|#|...#|#...|.#......#.#||...|.#|...#.|#.
                |..#.......|.||.....||.|....|#||..........#...|#...
                |.|..#....|#|||#..##||..#|.......|..|###..|.#...|.|
                ||..|.#|.#...#....|.....#.....#....#...|..|.|.#.|.#
                |....###.#....|.#..#...#...###.|.|.....#|...#.....|
                |..#....##.....##..|.#.||#.|.#|#||..|...#|..|.#....
                ||#..#.#|||#.|#..#........#......||...#.|..#|....#|
                |......#|...#.|...#...|.|...|#|#......#|.##.#|.|.#|
                |#||.#......#.##......#..||.##|.|.||..|#....#..#...
                |#.#...#.|.#|#||#.#......#....|##|.........##.#|...
                |.....###...#||....|####..#|||...#..#|.|....#|..#..
                |......|#..#.#.#..|.#|#||..||.|...#....##...|......
                |...#...|..#..##.||.#.#.....|.###.....##|#||..#..#|
                |.#..#||.#....||....|##..|||...|.||...#..##.#....#.""".stripMargin

  def part1(s: String) = {
    val w = s.indexOf('\n') + 1
    def tick(s: String) = s.zipWithIndex.map { case (c, i) =>
      val cnt = Seq(-w - 1, -w, -w + 1, -1, 1, w -1, w, w + 1).map(_ + i).
        filter(i => i >= 0 && i < s.length && s(i) != '\n').map(s(_)).
        foldLeft(Map(('.' -> 0), ('|' -> 0), ('#' -> 0)))(
          (cs, c) => cs.updated(c, cs(c) + 1))
      s(i) match {
        case '.' => if (cnt('|') >= 3) '|' else s(i)
        case '|' => if (cnt('#') >= 3) '#' else s(i)
        case '#' => if (cnt('#') >= 1 && cnt('|') >= 1) s(i) else '.'
        case _ => s(i)
      }
    }.mkString
    val end = (1 to 10).foldLeft(s) { (s, i) => tick(s) }
    end.count(_ == '|') * end.count(_ == '#')
  }

  "part1" should "satisfy the examples given" in {
    assertResult(1147)(part1(""".#.#...|#.
                               |.....#|##|
                               |.|..|...#.
                               |..|#.....#
                               |#.#|||#|#|
                               |...#.||...
                               |.|....|...
                               |||...#|.#|
                               ||.||||..|.
                               |...#.|..|.""".stripMargin))
  }

  "part1" should "succeed" in { info(part1(input).toString)}

  def part2(s: String) = {
    val w = s.indexOf('\n') + 1
    def tick(s: String) = s.zipWithIndex.map { case (c, i) =>
      val cnt = Seq(-w - 1, -w, -w + 1, -1, 1, w - 1, w, w + 1).map(_ + i).
        filter(i => i >= 0 && i < s.length && s(i) != '\n').map(s(_)).
        foldLeft(Map(('.' -> 0), ('|' -> 0), ('#' -> 0)))(
          (cs, c) => cs.updated(c, cs(c) + 1))
      s(i) match {
        case '.' => if (cnt('|') >= 3) '|' else s(i)
        case '|' => if (cnt('#') >= 3) '#' else s(i)
        case '#' => if (cnt('#') >= 1 && cnt('|') >= 1) s(i) else '.'
        case _ => s(i)
      }
    }.mkString
    val seen = mutable.Map(s -> 0)
    case class Cycle(s: Int, e: Int) extends Exception
    var end = ""
    try {
      Iterator.from(1).foldLeft(s) { (s, i) =>
        val next = tick(s)
        if (seen contains next) throw Cycle(seen(next), i)
        else seen(next) = i
        next
      }.last
    } catch { case Cycle(s, e) =>
      end = seen.map(e =>
        (e._2, e._1)).toMap.apply(s + (1000000000 - s) % (e - s))
    }
    end.count(_ == '|') * end.count(_ == '#')
  }

  "part2" should "succeed" in { info(part2(input).toString)}
}
