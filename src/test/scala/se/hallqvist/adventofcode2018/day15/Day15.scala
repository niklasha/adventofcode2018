package se.hallqvist.adventofcode2018.day15

import org.scalatest._

import scala.annotation.tailrec
import scala.collection.mutable

/*
--- Day 15: Beverage Bandits ---
Having perfected their hot chocolate, the Elves have a new problem: the Goblins that live in these caves will do anything to steal it. Looks like they're here for a fight.

You scan the area, generating a map of the walls (#), open cavern (.), and starting position of every Goblin (G) and Elf (E) (your puzzle input).

Combat proceeds in rounds; in each round, each unit that is still alive takes a turn, resolving all of its actions before the next unit's turn begins. On each unit's turn, it tries to move into range of an enemy (if it isn't already) and then attack (if it is in range).

All units are very disciplined and always follow very strict combat rules. Units never move or attack diagonally, as doing so would be dishonorable. When multiple choices are equally valid, ties are broken in reading order: top-to-bottom, then left-to-right. For instance, the order in which units take their turns within a round is the reading order of their starting positions in that round, regardless of the type of unit or whether other units have moved after the round started. For example:

                 would take their
These units:   turns in this order:
  #######           #######
  #.G.E.#           #.1.2.#
  #E.G.E#           #3.4.5#
  #.G.E.#           #.6.7.#
  #######           #######
Each unit begins its turn by identifying all possible targets (enemy units). If no targets remain, combat ends.

Then, the unit identifies all of the open squares (.) that are in range of each target; these are the squares which are adjacent (immediately up, down, left, or right) to any target and which aren't already occupied by a wall or another unit. Alternatively, the unit might already be in range of a target. If the unit is not already in range of a target, and there are no open squares which are in range of a target, the unit ends its turn.

If the unit is already in range of a target, it does not move, but continues its turn with an attack. Otherwise, since it is not in range of a target, it moves.

To move, the unit first considers the squares that are in range and determines which of those squares it could reach in the fewest steps. A step is a single movement to any adjacent (immediately up, down, left, or right) open (.) square. Units cannot move into walls or other units. The unit does this while considering the current positions of units and does not do any prediction about where units will be later. If the unit cannot reach (find an open path to) any of the squares that are in range, it ends its turn. If multiple squares are in range and tied for being reachable in the fewest steps, the square which is first in reading order is chosen. For example:

Targets:      In range:     Reachable:    Nearest:      Chosen:
#######       #######       #######       #######       #######
#E..G.#       #E.?G?#       #E.@G.#       #E.!G.#       #E.+G.#
#...#.#  -->  #.?.#?#  -->  #.@.#.#  -->  #.!.#.#  -->  #...#.#
#.G.#G#       #?G?#G#       #@G@#G#       #!G.#G#       #.G.#G#
#######       #######       #######       #######       #######
In the above scenario, the Elf has three targets (the three Goblins):

Each of the Goblins has open, adjacent squares which are in range (marked with a ? on the map).
Of those squares, four are reachable (marked @); the other two (on the right) would require moving through a wall or unit to reach.
Three of these reachable squares are nearest, requiring the fewest steps (only 2) to reach (marked !).
Of those, the square which is first in reading order is chosen (+).
The unit then takes a single step toward the chosen square along the shortest path to that square. If multiple steps would put the unit equally closer to its destination, the unit chooses the step which is first in reading order. (This requires knowing when there is more than one shortest path so that you can consider the first step of each such path.) For example:

In range:     Nearest:      Chosen:       Distance:     Step:
#######       #######       #######       #######       #######
#.E...#       #.E...#       #.E...#       #4E212#       #..E..#
#...?.#  -->  #...!.#  -->  #...+.#  -->  #32101#  -->  #.....#
#..?G?#       #..!G.#       #...G.#       #432G2#       #...G.#
#######       #######       #######       #######       #######
The Elf sees three squares in range of a target (?), two of which are nearest (!), and so the first in reading order is chosen (+). Under "Distance", each open square is marked with its distance from the destination square; the two squares to which the Elf could move on this turn (down and to the right) are both equally good moves and would leave the Elf 2 steps from being in range of the Goblin. Because the step which is first in reading order is chosen, the Elf moves right one square.

Here's a larger example of movement:

Initially:
#########
#G..G..G#
#.......#
#.......#
#G..E..G#
#.......#
#.......#
#G..G..G#
#########

After 1 round:
#########
#.G...G.#
#...G...#
#...E..G#
#.G.....#
#.......#
#G..G..G#
#.......#
#########

After 2 rounds:
#########
#..G.G..#
#...G...#
#.G.E.G.#
#.......#
#G..G..G#
#.......#
#.......#
#########

After 3 rounds:
#########
#.......#
#..GGG..#
#..GEG..#
#G..G...#
#......G#
#.......#
#.......#
#########
Once the Goblins and Elf reach the positions above, they all are either in range of a target or cannot find any square in range of a target, and so none of the units can move until a unit dies.

After moving (or if the unit began its turn in range of a target), the unit attacks.

To attack, the unit first determines all of the targets that are in range of it by being immediately adjacent to it. If there are no such targets, the unit ends its turn. Otherwise, the adjacent target with the fewest hit points is selected; in a tie, the adjacent target with the fewest hit points which is first in reading order is selected.

The unit deals damage equal to its attack power to the selected target, reducing its hit points by that amount. If this reduces its hit points to 0 or fewer, the selected target dies: its square becomes . and it takes no further turns.

Each unit, either Goblin or Elf, has 3 attack power and starts with 200 hit points.

For example, suppose the only Elf is about to attack:

       HP:            HP:
G....  9       G....  9
..G..  4       ..G..  4
..EG.  2  -->  ..E..
..G..  2       ..G..  2
...G.  1       ...G.  1
The "HP" column shows the hit points of the Goblin to the left in the corresponding row. The Elf is in range of three targets: the Goblin above it (with 4 hit points), the Goblin to its right (with 2 hit points), and the Goblin below it (also with 2 hit points). Because three targets are in range, the ones with the lowest hit points are selected: the two Goblins with 2 hit points each (one to the right of the Elf and one below the Elf). Of those, the Goblin first in reading order (the one to the right of the Elf) is selected. The selected Goblin's hit points (2) are reduced by the Elf's attack power (3), reducing its hit points to -1, killing it.

After attacking, the unit's turn ends. Regardless of how the unit's turn ends, the next unit in the round takes its turn. If all units have taken turns in this round, the round ends, and a new round begins.

The Elves look quite outnumbered. You need to determine the outcome of the battle: the number of full rounds that were completed (not counting the round in which combat ends) multiplied by the sum of the hit points of all remaining units at the moment combat ends. (Combat only ends when a unit finds no targets during its turn.)

Below is an entire sample combat. Next to each map, each row's units' hit points are listed from left to right.

Initially:
#######
#.G...#   G(200)
#...EG#   E(200), G(200)
#.#.#G#   G(200)
#..G#E#   G(200), E(200)
#.....#
#######

After 1 round:
#######
#..G..#   G(200)
#...EG#   E(197), G(197)
#.#G#G#   G(200), G(197)
#...#E#   E(197)
#.....#
#######

After 2 rounds:
#######
#...G.#   G(200)
#..GEG#   G(200), E(188), G(194)
#.#.#G#   G(194)
#...#E#   E(194)
#.....#
#######

Combat ensues; eventually, the top Elf dies:

After 23 rounds:
#######
#...G.#   G(200)
#..G.G#   G(200), G(131)
#.#.#G#   G(131)
#...#E#   E(131)
#.....#
#######

After 24 rounds:
#######
#..G..#   G(200)
#...G.#   G(131)
#.#G#G#   G(200), G(128)
#...#E#   E(128)
#.....#
#######

After 25 rounds:
#######
#.G...#   G(200)
#..G..#   G(131)
#.#.#G#   G(125)
#..G#E#   G(200), E(125)
#.....#
#######

After 26 rounds:
#######
#G....#   G(200)
#.G...#   G(131)
#.#.#G#   G(122)
#...#E#   E(122)
#..G..#   G(200)
#######

After 27 rounds:
#######
#G....#   G(200)
#.G...#   G(131)
#.#.#G#   G(119)
#...#E#   E(119)
#...G.#   G(200)
#######

After 28 rounds:
#######
#G....#   G(200)
#.G...#   G(131)
#.#.#G#   G(116)
#...#E#   E(113)
#....G#   G(200)
#######

More combat ensues; eventually, the bottom Elf dies:

After 47 rounds:
#######
#G....#   G(200)
#.G...#   G(131)
#.#.#G#   G(59)
#...#.#
#....G#   G(200)
#######
Before the 48th round can finish, the top-left Goblin finds that there are no targets remaining, and so combat ends. So, the number of full rounds that were completed is 47, and the sum of the hit points of all remaining units is 200+131+59+200 = 590. From these, the outcome of the battle is 47 * 590 = 27730.

Here are a few example summarized combats:

#######       #######
#G..#E#       #...#E#   E(200)
#E#E.E#       #E#...#   E(197)
#G.##.#  -->  #.E##.#   E(185)
#...#E#       #E..#E#   E(200), E(200)
#...E.#       #.....#
#######       #######

Combat ends after 37 full rounds
Elves win with 982 total hit points left
Outcome: 37 * 982 = 36334
#######       #######
#E..EG#       #.E.E.#   E(164), E(197)
#.#G.E#       #.#E..#   E(200)
#E.##E#  -->  #E.##.#   E(98)
#G..#.#       #.E.#.#   E(200)
#..E#.#       #...#.#
#######       #######

Combat ends after 46 full rounds
Elves win with 859 total hit points left
Outcome: 46 * 859 = 39514
#######       #######
#E.G#.#       #G.G#.#   G(200), G(98)
#.#G..#       #.#G..#   G(200)
#G.#.G#  -->  #..#..#
#G..#.#       #...#G#   G(95)
#...E.#       #...G.#   G(200)
#######       #######

Combat ends after 35 full rounds
Goblins win with 793 total hit points left
Outcome: 35 * 793 = 27755
#######       #######
#.E...#       #.....#
#.#..G#       #.#G..#   G(200)
#.###.#  -->  #.###.#
#E#G#G#       #.#.#.#
#...#G#       #G.G#G#   G(98), G(38), G(200)
#######       #######

Combat ends after 54 full rounds
Goblins win with 536 total hit points left
Outcome: 54 * 536 = 28944
#########       #########
#G......#       #.G.....#   G(137)
#.E.#...#       #G.G#...#   G(200), G(200)
#..##..G#       #.G##...#   G(200)
#...##..#  -->  #...##..#
#...#...#       #.G.#...#   G(200)
#.G...G.#       #.......#
#.....G.#       #.......#
#########       #########

Combat ends after 20 full rounds
Goblins win with 937 total hit points left
Outcome: 20 * 937 = 18740
What is the outcome of the combat described in your puzzle input?

--- Part Two ---
According to your calculations, the Elves are going to lose badly. Surely, you won't mess up the timeline too much if you give them just a little advanced technology, right?

You need to make sure the Elves not only win, but also suffer no losses: even the death of a single Elf is unacceptable.

However, you can't go too far: larger changes will be more likely to permanently alter spacetime.

So, you need to find the outcome of the battle in which the Elves have the lowest integer attack power (at least 4) that allows them to win without a single death. The Goblins always have an attack power of 3.

In the first summarized example above, the lowest attack power the Elves need to win without losses is 15:

#######       #######
#.G...#       #..E..#   E(158)
#...EG#       #...E.#   E(14)
#.#.#G#  -->  #.#.#.#
#..G#E#       #...#.#
#.....#       #.....#
#######       #######

Combat ends after 29 full rounds
Elves win with 172 total hit points left
Outcome: 29 * 172 = 4988
In the second example above, the Elves need only 4 attack power:

#######       #######
#E..EG#       #.E.E.#   E(200), E(23)
#.#G.E#       #.#E..#   E(200)
#E.##E#  -->  #E.##E#   E(125), E(200)
#G..#.#       #.E.#.#   E(200)
#..E#.#       #...#.#
#######       #######

Combat ends after 33 full rounds
Elves win with 948 total hit points left
Outcome: 33 * 948 = 31284
In the third example above, the Elves need 15 attack power:

#######       #######
#E.G#.#       #.E.#.#   E(8)
#.#G..#       #.#E..#   E(86)
#G.#.G#  -->  #..#..#
#G..#.#       #...#.#
#...E.#       #.....#
#######       #######

Combat ends after 37 full rounds
Elves win with 94 total hit points left
Outcome: 37 * 94 = 3478
In the fourth example above, the Elves need 12 attack power:

#######       #######
#.E...#       #...E.#   E(14)
#.#..G#       #.#..E#   E(152)
#.###.#  -->  #.###.#
#E#G#G#       #.#.#.#
#...#G#       #...#.#
#######       #######

Combat ends after 39 full rounds
Elves win with 166 total hit points left
Outcome: 39 * 166 = 6474
In the last example above, the lone Elf needs 34 attack power:

#########       #########
#G......#       #.......#
#.E.#...#       #.E.#...#   E(38)
#..##..G#       #..##...#
#...##..#  -->  #...##..#
#...#...#       #...#...#
#.G...G.#       #.......#
#.....G.#       #.......#
#########       #########

Combat ends after 30 full rounds
Elves win with 38 total hit points left
Outcome: 30 * 38 = 1140
After increasing the Elves' attack power until it is just barely enough for them to win without any Elves dying, what is the outcome of the combat described in your puzzle input?
*/

class Day15 extends FlatSpec {
  val input = """################################
                |#####...#.....#####..G...#######
                |######..##.#..#####...#..#######
                |####...G.G.....####......#######
                |####.G..........##.......#######
                |#####..##.###G..#......G.#######
                |########.G##....#........#######
                |########..###............#######
                |########.#####...G........######
                |########.G................######
                |#######........G.....E....######
                |########GE.................#####
                |########....G.#####.E.....######
                |##########..G#######....E.######
                |#########G..#########....#######
                |########....#########...########
                |#######G....#########...#..#####
                |####........#########......#####
                |####........#########.G....#####
                |##......G....#######....#.#.####
                |#..E..........#####..........###
                |#.#.........G.............E.#..#
                |####.........########..E.#....E#
                |##........E##########...######.#
                |##.##.....###########.##########
                |#..#...E.#######################
                |##G.G....#######################
                |##.....#########################
                |##.....#########################
                |##...###########################
                |####.###########################
                |################################""".stripMargin

  val hpInit = 200

  def add(a: (Int, Int), b:(Int, Int)) = (a._1 + b._1, a._2 + b._2)
  def lt(a: (Int, Int), b: (Int, Int)) =
    a._2 < b._2 || (a._2 == b._2 && a._1 < b._1)

  abstract class Cell(var p: (Int, Int)) {
    override def toString = s"${getClass.getSimpleName} $p"
    def adjacent() =
      Seq((0, -1), (-1, 0), (1, 0), (0, 1)).map(d => add(d, p))
  }
  case class Wall(pInit: (Int, Int)) extends Cell(pInit)
  case class Open(pInit: (Int, Int)) extends Cell(pInit)
  abstract class Unit(var pInit: (Int, Int), var hp: Int = hpInit)
    extends Cell(pInit) {
    override def toString = s"${super.toString} ($hp)"
  }
  class Goblin(pInit: (Int, Int)) extends Unit(pInit)
  class Elf(pInit: (Int, Int)) extends Unit(pInit)

  class CombatEnd extends Exception
  class ElfDeath extends Exception

  def part1(s: String) = {
    val field = mutable.Map[(Int, Int), Cell]()
    field ++= s.split("\n").zipWithIndex.flatMap { case (l, y) =>
      l.zipWithIndex.map { case (c, x) =>
        val p = (x, y)
        (p -> (c match {
          case '#' => Wall(p)
          case '.' => Open(p)
          case 'G' => new Goblin(p)
          case 'E' => new Elf(p)
        }))
      }
    }.toMap
    val xMax = field.map(e => e._1._1).max
    val yMax = field.map(e => e._1._2).max
    var n = 0

    def seqMap[A](f: (Cell) => A) =
      (0 to yMax).flatMap(y => (0 to xMax).map(x => f(field((x, y)))))

    abstract class Class() {}
    case class Attack() extends Class
    case class Move() extends Class
    case class Verbose() extends Class
    case class Field() extends Class
    case class Prio() extends Class

    def print(c: Class) = {
      (0 to yMax).foreach { y =>
        log(c)((0 to xMax).map { x =>
          field((x, y)) match {
            case _: Wall => "#"
            case _: Open => "."
            case _: Elf => "E"
            case _: Goblin => "G"
          }
        }.mkString)
      }
    }

    def units(): Seq[Unit] = seqMap(identity).collect { case u: Unit => u }

    def logCond() = false
    def logClasses(): Set[Class] = Set(/*Attack(),*/ Prio())

    def log(c: Class)(s: String) =
      if (logCond || logClasses.contains(c)) println(s)

    def attack(u: Unit) = {
      log(Attack())(s"$u attacks")
      val ts = u.adjacent.map(field(_)).collect { case u: Unit => u }.
        filter(_.getClass != u.getClass).groupBy(_.hp).minBy(_._1)._2
      field(ts.map(_.p).sortWith(lt).head)
      match { case t: Unit =>
        if (ts.size > 1) {
	  log(Attack())(s"$ts")
	  log(Attack())(s"target $t")
        }
        t.hp -= 3
        if (t.hp <= 0) field(t.p) = Open(t.p)
        log(Attack())(s"after strike: ${field(t.p)}")
      }
    }

    def move(u: Unit, targets: Set[(Int, Int)]) = {
      val c = Move() //if (n == 2 && u.p == (10, 13)) Prio() else Move()
      log(/*Move()*/c)(s"$u approaches $targets)")
      // Find closest reachables in order
      @tailrec
      def r(ps: Set[(Int, Int)],
            visited: Set[(Int, Int)],
            starts: Map[(Int, Int), (Int, Int)]):
      Seq[((Int, Int), (Int, Int))] = {
	log(Move())(s"r(ps = $ps, visited = $visited starts = $starts)")
        val candidates = ps.foldLeft(Set.empty[((Int, Int), (Int, Int))])(
	  (cs, p) =>
	    cs ++ field(p).adjacent.filter(a =>
	      !visited.contains(a) && field(a).isInstanceOf[Open]).
	      map(a => (a, starts.getOrElse(p, a))))
	  .groupBy(_._1).
	  map { case (a, g) => (a, g.map(_._2).toSeq.sortWith(lt).head) }.toSet
        val reached =
	  candidates.toSeq.sortWith((a, b) => lt(a._1, b._1)).filter(
	    targets contains _._1)
	log(/*Move()*/c)(s"candidates = $candidates reached = $reached")
        if (candidates.isEmpty || !reached.isEmpty) reached
        else r(
	  candidates.map(_._1),
	  visited ++ candidates.map(_._1),
	  starts ++ candidates)
      }
      r(Set(u.p), Set(u.p), Map()).sortWith((a, b) => lt(a._1, b._1)).
	headOption.map { case (p, m) =>
	log(Move())(s"moving $u to $m planning for $p")
	field(u.p) = Open(u.p)
	u.p = m
	field(u.p) = u
      }
    }

    try {
      while (true) {
	log(Field())(s"n $n")
	print(Field())
	val sum = units.collect { case g: Goblin => g }.map(_.hp).sum
	log(Field())(s"standings $n $sum ${n * sum} $units")
        units.foreach(u => if (u.hp > 0) {
	  val targets = units.filter(_.getClass != u.getClass)
	  if (targets.isEmpty) throw new CombatEnd()
	  val ps = targets.flatMap(t =>
	    t.adjacent.filter(p => field(p).isInstanceOf[Open] || p == u.p)).
	    toSet
	  if (!ps.contains(u.p)) move(u, ps)
	  if (ps contains u.p) attack(u)
	})
        n += 1
      }
    } catch { case _: CombatEnd => }
    val sum = units.map(_.hp).sum
    print(Field())
    log(Field())(s"standings $n $sum ${n * sum} $units")
    units.groupBy(_.p._2).toSeq.sortBy(_._1).
      foreach { case (y, us) => log(Verbose())(s"$y $us") }
    n * sum
  }

  "part1" should "satisfy the examples given" in {
    assertResult(27730)(part1("""#######
                                |#.G...#
                                |#...EG#
                                |#.#.#G#
                                |#..G#E#
                                |#.....#
                                |#######""".stripMargin))
    assertResult(36334)(part1("""#######
                                |#G..#E#
                                |#E#E.E#
                                |#G.##.#
                                |#...#E#
                                |#...E.#
                                |#######""".stripMargin))
    assertResult(39514)(part1("""#######
                                |#E..EG#
                                |#.#G.E#
                                |#E.##E#
                                |#G..#.#
                                |#..E#.#
                                |#######""".stripMargin))
    assertResult(27755)(part1("""#######
                                |#E.G#.#
                                |#.#G..#
                                |#G.#.G#
                                |#G..#.#
                                |#...E.#
                                |#######""".stripMargin))
    assertResult(28944)(part1("""#######
                                |#.E...#
                                |#.#..G#
                                |#.###.#
                                |#E#G#G#
                                |#...#G#
                                |#######""".stripMargin))
    assertResult(18740)(part1("""#########
                                |#G......#
                                |#.E.#...#
                                |#..##..G#
                                |#...##..#
                                |#...#...#
                                |#.G...G.#
                                |#.....G.#
                                |#########""".stripMargin))
  }

  "part1" should "succeed" in { info(part1(input).toString) }

  def part2(s: String) = {
    def initMap() = {
      val map = mutable.Map[(Int, Int), Cell]()
      map ++= s.split("\n").zipWithIndex.flatMap { case (l, y) =>
        l.zipWithIndex.map { case (c, x) =>
          val p = (x, y)
          (p -> (c match {
            case '#' => Wall(p)
            case '.' => Open(p)
            case 'G' => new Goblin(p)
            case 'E' => new Elf(p)
          }))
        }
      }.toMap
    }
    var map = initMap
    val xMax = map.map(e => e._1._1).max
    val yMax = map.map(e => e._1._2).max
    var n = 0

    def seqMap[A](f: (Cell) => A) =
      (0 to yMax).flatMap(y => (0 to xMax).map(x => f(map((x, y)))))

    def print() = {
      (0 to yMax).foreach { y =>
        println((0 to xMax).map { x =>
          map((x, y)) match {
            case _: Wall => "#"
            case _: Open => "."
            case _: Elf => "E"
            case _: Goblin => "G"
          }
        }.mkString)
      }
    }

    def units(): Seq[Unit] = seqMap(identity).collect { case u: Unit => u }

    def attack(u: Unit, ecp: Int) = {
      //      println(s"$u attacks")
      val ts = u.adjacent.map(map(_)).collect { case u: Unit => u }.
        filter(_.getClass != u.getClass).groupBy(_.hp).minBy(_._1)._2
      map(
        u.adjacent.map(map(_)).collect { case u: Unit => u }.
          filter(_.getClass != u.getClass).groupBy(_.hp).minBy(_._1)._2.map(_.p).
          sortWith(lt).head)
      match { case t: Unit =>
        //        if (ts.size > 1) {
        //          println(s"$ts")
        //          println(s"target $t")
        //        }
        t.hp -= (if (t.isInstanceOf[Goblin]) ecp else 3)
        if (t.hp <= 0) {
          if (t.isInstanceOf[Elf]) throw new ElfDeath()
          map(t.p) = Open(t.p)
        }
        //        println(s"after strike: ${map(t.p)}")
      }
    }

    def logCond() = false

    def move(u: Unit, targets: Set[(Int, Int)]) = {
      // Find closest reachables in order
      @tailrec
      def r(ps: Set[(Int, Int)],
	    visited: Set[(Int, Int)],
	    starts: Map[(Int, Int), (Int, Int)]):
      Seq[((Int, Int), (Int, Int))] = {
	val candidates = ps.foldLeft(Set.empty[((Int, Int), (Int, Int))])(
	  (cs, p) =>
	    cs ++ map(p).adjacent.filter(a =>
	      !visited.contains(a) && map(a).isInstanceOf[Open]).
	      map(a => (a, starts.getOrElse(p, a))))
	  .groupBy(_._1).
	  map { case (a, g) => (a, g.map(_._2).toSeq.sortWith(lt).head) }.toSet
	val reached =
	  candidates.toSeq.sortWith((a, b) => lt(a._1, b._1)).filter(
	    targets contains _._1)
	if (candidates.isEmpty || !reached.isEmpty) reached
	else r(
	  candidates.map(_._1),
	  visited ++ candidates.map(_._1),
	  starts ++ candidates)
      }
      r(Set(u.p), Set(u.p), Map()).sortWith((a, b) => lt(a._1, b._1)).
	headOption.map { case (p, m) =>
	map(u.p) = Open(u.p)
	u.p = m
	map(u.p) = u
      }
    }

    var cp = 4
    try {
      while (true) {
        n = 0
        map = initMap
        try {
          while (true) {
            units.foreach { u =>
              if (u.hp > 0) {
                val targets = units.filter(_.getClass != u.getClass)
                if (targets.isEmpty) throw new CombatEnd()
                val ps = targets.flatMap(t =>
                  t.adjacent.
                    filter { p => val c = map(p); c.isInstanceOf[Open] || c == u }).
                  toSet
                if (!(ps contains u.p)) move(u, ps.filter(_ != u.p))
                if (ps contains u.p) attack(u, cp)
              }
            }
            n += 1
          }
        } catch { case _: ElfDeath => }
        cp += 1
      }
    } catch { case _: CombatEnd => }
    if (logCond) {
      print()
      println(s"standings $cp $n $units ${units().map(_.hp).sum}")
    }
    n * units.map(_.hp).sum
  }

  "part2" should "satisfy the examples given" in {
    assertResult(4988)(part2("""#######
                               |#.G...#
                               |#...EG#
                               |#.#.#G#
                               |#..G#E#
                               |#.....#
                               |#######""".stripMargin))
    assertResult(31284)(part2("""#######
                                |#E..EG#
                                |#.#G.E#
                                |#E.##E#
                                |#G..#.#
                                |#..E#.#
                                |#######""".stripMargin))
    assertResult(3478)(part2("""#######
                               |#E.G#.#
                               |#.#G..#
                               |#G.#.G#
                               |#G..#.#
                               |#...E.#
                               |#######""".stripMargin))
    assertResult(6474)(part2("""#######
                               |#.E...#
                               |#.#..G#
                               |#.###.#
                               |#E#G#G#
                               |#...#G#
                               |#######""".stripMargin))
    assertResult(1140)(part2("""#########
                               |#G......#
                               |#.E.#...#
                               |#..##..G#
                               |#...##..#
                               |#...#...#
                               |#.G...G.#
                               |#.....G.#
                               |#########""".stripMargin))
  }

  "part2" should "succeed" in { info(part2(input).toString) }
}
