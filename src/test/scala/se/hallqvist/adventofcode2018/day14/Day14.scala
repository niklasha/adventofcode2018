package se.hallqvist.adventofcode2018.day14

import org.scalatest._

/*
--- Day 14: Chocolate Charts ---
You finally have a chance to look at all of the produce moving around. Chocolate, cinnamon, mint, chili peppers, nutmeg, vanilla... the Elves must be growing these plants to make hot chocolate! As you realize this, you hear a conversation in the distance. When you go to investigate, you discover two Elves in what appears to be a makeshift underground kitchen/laboratory.

The Elves are trying to come up with the ultimate hot chocolate recipe; they're even maintaining a scoreboard which tracks the quality score (0-9) of each recipe.

Only two recipes are on the board: the first recipe got a score of 3, the second, 7. Each of the two Elves has a current recipe: the first Elf starts with the first recipe, and the second Elf starts with the second recipe.

To create new recipes, the two Elves combine their current recipes. This creates new recipes from the digits of the sum of the current recipes' scores. With the current recipes' scores of 3 and 7, their sum is 10, and so two new recipes would be created: the first with score 1 and the second with score 0. If the current recipes' scores were 2 and 3, the sum, 5, would only create one recipe (with a score of 5) with its single digit.

The new recipes are added to the end of the scoreboard in the order they are created. So, after the first round, the scoreboard is 3, 7, 1, 0.

After all new recipes are added to the scoreboard, each Elf picks a new current recipe. To do this, the Elf steps forward through the scoreboard a number of recipes equal to 1 plus the score of their current recipe. So, after the first round, the first Elf moves forward 1 + 3 = 4 times, while the second Elf moves forward 1 + 7 = 8 times. If they run out of recipes, they loop back around to the beginning. After the first round, both Elves happen to loop around until they land on the same recipe that they had in the beginning; in general, they will move to different recipes.

Drawing the first Elf as parentheses and the second Elf as square brackets, they continue this process:

(3)[7]
(3)[7] 1  0
 3  7  1 [0](1) 0
 3  7  1  0 [1] 0 (1)
(3) 7  1  0  1  0 [1] 2
 3  7  1  0 (1) 0  1  2 [4]
 3  7  1 [0] 1  0 (1) 2  4  5
 3  7  1  0 [1] 0  1  2 (4) 5  1
 3 (7) 1  0  1  0 [1] 2  4  5  1  5
 3  7  1  0  1  0  1  2 [4](5) 1  5  8
 3 (7) 1  0  1  0  1  2  4  5  1  5  8 [9]
 3  7  1  0  1  0  1 [2] 4 (5) 1  5  8  9  1  6
 3  7  1  0  1  0  1  2  4  5 [1] 5  8  9  1 (6) 7
 3  7  1  0 (1) 0  1  2  4  5  1  5 [8] 9  1  6  7  7
 3  7 [1] 0  1  0 (1) 2  4  5  1  5  8  9  1  6  7  7  9
 3  7  1  0 [1] 0  1  2 (4) 5  1  5  8  9  1  6  7  7  9  2
The Elves think their skill will improve after making a few recipes (your puzzle input). However, that could take ages; you can speed this up considerably by identifying the scores of the ten recipes after that. For example:

If the Elves think their skill will improve after making 9 recipes, the scores of the ten recipes after the first nine on the scoreboard would be 5158916779 (highlighted in the last line of the diagram).
After 5 recipes, the scores of the next ten would be 0124515891.
After 18 recipes, the scores of the next ten would be 9251071085.
After 2018 recipes, the scores of the next ten would be 5941429882.
What are the scores of the ten recipes immediately after the number of recipes in your puzzle input?

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
As it turns out, you got the Elves' plan backwards. They actually want to know how many recipes appear on the scoreboard to the left of the first recipes whose scores are the digits from your puzzle input.

51589 first appears after 9 recipes.
01245 first appears after 5 recipes.
92510 first appears after 18 recipes.
59414 first appears after 2018 recipes.
How many recipes appear on the scoreboard to the left of the score sequence in your puzzle input?
*/

class Day14 extends FlatSpec {
  val input = "681901"

  class Node[A](var p: Node[A], var n: Node[A], var a: A) {
    if (p == null) { p = this; n = this }
    def append(a: A) = {
      n.p = new Node(this, n, a)
      n = n.p
      n
    }
    def prev(i: Int) = (1 to i).foldLeft(this)((n, _) => n.p)
    def next(i: Int) = (1 to i).foldLeft(this)((n, _) => n.n)
  }

  def toL[A](n: Node[A], i: Int): Seq[A] = if (i == 1) Seq[A](n.a) else Seq(n.a) ++ toL(n.n, i - 1)

  def part1(s: String) = {
    val i = s.toInt
    val rs = new Node(null, null, 3)
    rs.append(7)
    def nrs(e1: Node[Int], e2: Node[Int], l: Int): (Node[Int], Node[Int], Int) = {
      val s = e1.a + e2.a
      if (s > 9) rs.p.append(s / 10)
      rs.p.append(s % 10)
      ((0 to e1.a).foldLeft(e1)((e, _) => e.n),
        (0 to e2.a).foldLeft(e2)((e, _) => e.n),
        l + (if (s > 9) 2 else 1))
    }
    val l = Iterator.continually(0).
      scanLeft((rs, rs.n, 2)) { case ((e1, e2, l), i) => nrs(e1, e2, l) }.dropWhile(_._3 < i + 10).next._3
    var n = rs.prev(10)
    if (l > i + 10) n = n.p
    toL(n, 10).mkString
  }

  "part1" should "satisfy the examples given" in {
    assertResult("5158916779")(part1("9"))
    assertResult("0124515891")(part1("5"))
    assertResult("9251071085")(part1("18"))
    assertResult("5941429882")(part1("2018"))
  }

  "part1" should "succeed" in { info(part1(input)) }

  def part2(s: String) = {
    var lr = new Node(null, null, 3)
    lr = lr.append(7)
    case class Found(l: Int) extends Exception
    def a(a: Int, l: Int) = {
      lr = lr.append(a)
      var sn = lr.prev(s.length - 1)
      var ss = toL(sn, s.length)
      if (l > s.length) sn.p = null
      if (ss.mkString == s) throw Found(l)
    }
    def nrs(e1: Node[Int], e2: Node[Int], l: Int): (Node[Int], Node[Int], Int) = {
      val s = e1.a + e2.a
      if (s > 9) a(s / 10, l + 1)
      a(s % 10, if (s > 9) l + 2 else (l + 1))
      ((0 to e1.a).foldLeft(e1)((e, _) => e.n),
        (0 to e2.a).foldLeft(e2)((e, _) => e.n),
        l + (if (s > 9) 2 else 1))
    }
    try {
      Iterator.continually(0).
        scanLeft((lr.p, lr, 2)) { case ((e1, e2, l), i) => nrs(e1, e2, l) }.size
    } catch {
      case f: Found => f.l - s.length
    }

  }

  "part2" should "satisfy the examples given" in {
    assertResult(9)(part2("51589"))
    assertResult(5)(part2("01245"))
    assertResult(18)(part2("92510"))
    assertResult(2018)(part2("59414"))
  }

  "part2" should "succeed" in { info(part2(input.toString).toString) }
}
