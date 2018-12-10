package se.hallqvist.adventofcode2018.day08

import org.scalatest._

/*
--- Day 8: Memory Maneuver ---
The sleigh is much easier to pull than you'd expect for something its weight. Unfortunately, neither you nor the Elves know which way the North Pole is from here.

You check your wrist device for anything that might help. It seems to have some kind of navigation system! Activating the navigation system produces more bad news: "Failed to start navigation system. Could not read software license file."

The navigation system's license file consists of a list of numbers (your puzzle input). The numbers define a data structure which, when processed, produces some kind of tree that can be used to calculate the license number.

The tree is made up of nodes; a single, outermost node forms the tree's root, and it contains all other nodes in the tree (or contains nodes that contain nodes, and so on).

Specifically, a node consists of:

A header, which is always exactly two numbers:
The quantity of child nodes.
The quantity of metadata entries.
Zero or more child nodes (as specified in the header).
One or more metadata entries (as specified in the header).
Each child node is itself a node that has its own header, child nodes, and metadata. For example:

2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
A----------------------------------
    B----------- C-----------
                     D-----
In this example, each node of the tree is also marked with an underline starting with a letter for easier identification. In it, there are four nodes:

A, which has 2 child nodes (B, C) and 3 metadata entries (1, 1, 2).
B, which has 0 child nodes and 3 metadata entries (10, 11, 12).
C, which has 1 child node (D) and 1 metadata entry (2).
D, which has 0 child nodes and 1 metadata entry (99).
The first check done on the license file is to simply add up all of the metadata entries. In this example, that sum is 1+1+2+10+11+12+2+99=138.

What is the sum of all metadata entries?

--- Part Two ---
The second check is slightly more complicated: you need to find the value of the root node (A in the example above).

The value of a node depends on whether it has child nodes.

If a node has no child nodes, its value is the sum of its metadata entries. So, the value of node B is 10+11+12=33, and the value of node D is 99.

However, if a node does have child nodes, the metadata entries become indexes which refer to those child nodes. A metadata entry of 1 refers to the first child node, 2 to the second, 3 to the third, and so on. The value of this node is the sum of the values of the child nodes referenced by the metadata entries. If a referenced child node does not exist, that reference is skipped. A child node can be referenced multiple time and counts each time it is referenced. A metadata entry of 0 does not refer to any child node.

For example, again using the above nodes:

Node C has one metadata entry, 2. Because node C has only one child node, 2 references a child node which does not exist, and so the value of node C is 0.
Node A has three metadata entries: 1, 1, and 2. The 1 references node A's first child node, B, and the 2 references node A's second child node, C. Because node B has a value of 33 and node C has a value of 0, the value of node A is 33+33+0=66.
So, in this example, the value of the root node is 66.

What is the value of the root node?
*/

class Day08 extends FlatSpec {
  val input = "8 11 7 2 5 3 3 5 1 5 0 8 1 8 1 5 3 6 2 7 1 1 1 1 2 1 6 0 9 4 1 1 6 4 1 7 9 9 2 3 1 2 3 2 1 7 0 10 8 3 8 2 1 7 8 5 6 6 3 1 3 3 1 1 1 3 2 3 5 4 3 7 1 9 0 6 2 8 1 2 9 1 1 1 1 2 2 1 3 3 2 1 8 0 6 2 4 2 9 1 8 2 3 1 2 3 2 1 1 1 5 0 11 4 2 5 5 2 3 3 3 1 1 8 1 2 1 1 2 1 4 2 1 2 3 5 3 7 1 8 0 11 3 1 8 6 1 7 4 3 7 2 6 2 2 3 2 1 1 1 2 1 6 0 7 5 9 1 1 1 7 6 1 1 3 1 1 2 1 9 0 10 9 1 4 1 2 1 3 6 6 2 1 2 3 3 3 1 1 3 2 5 1 2 2 1 5 3 3 5 1 6 0 8 1 9 5 1 7 3 2 7 1 2 1 1 1 3 1 9 0 9 9 3 2 4 2 7 4 1 2 2 1 1 2 3 2 2 1 1 1 7 0 10 2 8 1 1 8 8 2 1 1 4 1 2 3 3 1 2 2 2 2 4 2 5 3 5 1 8 0 10 4 8 5 1 2 6 1 1 2 6 2 2 1 2 1 3 1 1 1 5 0 9 1 7 1 4 3 8 4 9 7 1 1 1 2 3 1 5 0 8 4 8 1 5 9 3 3 1 1 3 1 3 1 1 5 2 1 2 7 5 4 4 5 3 4 1 7 0 6 1 3 8 1 4 1 1 1 3 3 3 2 2 1 6 0 9 9 4 9 7 1 4 9 4 5 2 2 1 2 2 1 1 6 0 10 3 2 5 1 1 2 1 6 9 2 1 1 2 1 3 3 3 3 2 5 3 4 1 9 0 6 3 3 8 1 6 1 1 1 1 1 3 1 2 2 1 1 7 0 7 7 3 5 3 8 8 1 2 1 2 1 1 3 2 1 7 0 10 7 6 3 3 6 1 5 6 9 5 3 1 1 2 1 3 2 5 1 4 1 3 5 1 9 0 9 6 3 7 4 3 4 1 6 3 2 1 3 2 2 1 3 2 1 1 5 0 9 2 4 8 8 8 1 7 8 2 2 1 3 1 1 1 6 0 7 8 9 1 5 4 7 7 3 2 2 2 2 1 1 4 3 5 3 3 7 1 5 0 9 3 5 5 3 5 1 8 9 7 1 2 1 1 1 1 8 0 7 2 1 5 3 7 3 4 2 2 2 1 3 1 2 2 1 9 0 6 5 1 3 9 6 4 2 1 1 3 2 2 2 1 1 4 4 2 5 2 3 4 1 4 3 5 4 5 5 3 7 1 5 0 6 1 8 2 5 1 8 1 1 1 1 1 1 8 0 10 9 6 5 9 6 1 3 4 7 6 2 1 2 1 3 3 1 2 1 5 0 10 9 1 3 7 1 1 1 5 2 6 3 3 3 1 1 5 1 2 5 1 1 2 3 5 1 6 0 7 8 9 5 6 2 1 4 3 2 1 1 2 1 1 5 0 10 1 6 7 2 4 1 4 3 9 9 1 1 1 2 2 1 7 0 9 2 8 3 4 3 1 1 2 2 2 1 1 1 3 1 2 2 1 3 5 2 3 6 1 8 0 10 7 6 8 3 8 2 7 3 1 1 1 1 1 1 1 1 1 3 1 5 0 8 8 8 1 9 7 8 1 6 3 2 1 1 3 1 6 0 6 7 3 1 1 9 7 3 3 1 1 2 1 2 1 5 5 3 1 3 4 1 6 0 6 4 5 1 2 4 2 3 1 2 1 2 3 1 7 0 9 5 9 2 4 9 7 7 3 1 1 2 1 1 1 1 1 1 9 0 9 4 2 1 2 7 4 8 2 6 2 2 3 1 3 1 3 3 3 5 3 2 2 3 6 1 5 0 7 7 7 3 3 1 3 1 1 3 1 1 2 1 6 0 10 5 2 1 5 4 4 7 1 9 7 3 2 1 1 3 1 1 8 0 6 1 3 7 6 2 7 1 3 3 3 1 3 1 2 4 5 1 3 4 1 1 6 4 2 4 5 5 3 7 1 7 0 7 4 3 1 1 5 7 4 1 1 2 1 3 2 1 1 7 0 9 2 3 9 7 2 9 1 4 7 1 2 2 2 3 1 2 1 5 0 11 3 2 1 3 6 5 9 5 4 7 9 1 3 3 1 2 1 4 2 3 2 2 3 3 7 1 6 0 7 9 1 9 7 6 3 8 2 3 2 3 1 2 1 9 0 7 8 7 3 3 7 7 1 3 2 2 2 2 1 1 2 3 1 6 0 10 3 3 3 8 4 2 1 8 7 2 1 3 3 2 2 3 2 5 1 3 2 2 4 3 4 1 9 0 9 4 7 6 3 3 1 3 5 1 2 2 1 2 1 3 3 1 1 1 7 0 9 3 1 7 2 2 4 9 7 6 2 1 1 3 3 3 1 1 5 0 6 7 7 7 8 3 1 2 1 2 3 1 2 1 1 2 3 7 1 5 0 6 1 3 7 5 5 1 1 3 3 3 1 1 7 0 6 4 2 1 1 1 1 3 1 1 1 3 2 2 1 8 0 10 1 2 1 4 1 3 7 8 5 7 3 3 1 1 3 1 1 2 1 5 4 5 2 3 1 3 5 1 5 0 8 2 6 4 7 6 2 1 4 2 1 1 3 2 1 5 0 6 2 7 1 2 4 1 3 1 1 1 1 1 8 0 8 8 4 7 7 2 9 8 1 3 2 2 1 1 2 2 3 5 2 1 2 2 1 1 6 2 4 5 3 3 7 1 7 0 11 2 9 7 6 1 1 1 3 7 6 8 2 1 2 2 2 2 2 1 9 0 10 1 3 8 1 4 5 4 6 6 3 1 1 2 1 3 3 1 2 3 1 7 0 8 1 1 2 7 2 9 9 3 2 3 2 3 2 1 1 4 4 3 2 1 5 3 3 5 1 7 0 10 7 4 9 3 5 1 1 1 5 2 1 2 1 1 1 3 1 1 9 0 6 6 7 7 2 6 1 3 1 2 3 1 1 1 3 1 1 6 0 11 9 7 4 3 1 5 5 7 7 5 3 1 1 3 3 2 3 3 3 3 4 5 3 5 1 8 0 10 6 4 4 9 4 1 8 5 1 7 2 2 1 1 1 2 1 3 1 8 0 7 1 1 4 9 8 2 8 1 3 1 2 2 3 3 2 1 6 0 8 5 4 5 7 1 4 2 3 3 3 1 1 2 1 1 3 1 1 3 3 7 1 8 0 10 6 1 3 5 2 5 6 8 3 2 2 3 1 1 3 1 2 1 1 5 0 6 7 9 2 2 1 6 1 3 2 3 3 1 8 0 11 9 3 2 4 5 1 4 8 6 2 1 2 3 1 1 2 1 3 3 2 2 5 1 2 2 3 3 7 1 9 0 10 5 1 1 6 5 7 7 5 8 6 3 1 2 2 1 3 1 3 2 1 9 0 6 1 6 8 5 7 4 2 2 1 1 1 1 3 3 2 1 9 0 11 3 3 6 1 5 4 7 6 7 9 8 1 3 3 1 3 1 1 2 3 2 3 4 5 4 1 5 4 7 3 5 5 3 4 1 9 0 11 6 8 5 9 1 2 6 7 6 1 5 3 1 1 2 2 3 1 1 3 1 5 0 10 6 1 5 6 4 7 2 5 3 2 1 2 2 3 2 1 9 0 10 5 2 1 2 7 9 9 1 9 6 2 2 2 1 1 2 1 3 1 1 3 1 3 3 7 1 8 0 6 2 5 8 2 1 9 1 1 3 2 1 1 2 3 1 5 0 6 9 7 3 3 8 1 3 2 3 1 1 1 7 0 9 2 2 1 4 2 4 6 6 6 1 3 1 3 3 2 2 1 3 5 3 2 4 3 3 4 1 5 0 10 5 1 5 6 1 1 9 9 8 1 2 1 3 2 1 1 9 0 10 4 6 9 7 8 1 8 3 1 1 3 3 1 2 3 1 1 3 3 1 9 0 10 2 4 7 1 3 6 3 1 1 7 3 2 3 3 3 1 1 1 1 2 2 3 3 3 4 1 8 0 6 4 1 8 1 9 5 1 3 2 2 1 1 1 3 1 5 0 7 1 2 6 5 1 9 9 2 3 2 1 2 1 8 0 7 1 8 3 2 4 7 2 3 2 1 2 2 1 3 3 2 2 3 5 3 4 1 5 0 11 1 6 2 4 4 6 5 1 2 3 8 2 2 1 1 2 1 5 0 11 4 5 2 6 1 1 6 7 3 1 9 1 1 1 3 1 1 9 0 10 9 6 1 9 9 9 2 1 9 8 3 1 3 1 2 2 1 2 3 5 1 3 2 1 3 3 2 7 5 5 3 7 1 7 0 9 1 9 5 7 1 6 3 8 9 3 2 1 3 2 3 1 1 7 0 10 9 8 7 9 2 7 5 6 1 2 1 1 2 1 1 1 1 1 6 0 8 5 1 7 9 9 4 5 5 1 3 3 1 3 1 5 2 1 3 1 3 3 3 6 1 5 0 9 6 1 5 4 8 7 5 1 6 1 2 1 2 3 1 5 0 11 6 8 9 9 5 9 7 1 1 9 7 3 1 1 1 1 1 5 0 11 6 1 4 5 1 5 5 6 8 3 1 2 1 2 2 1 4 4 2 4 2 4 3 7 1 8 0 6 7 4 1 7 2 9 1 3 2 2 2 2 1 2 1 6 0 11 9 2 4 5 6 6 5 1 4 5 7 3 2 3 2 3 1 1 7 0 11 6 1 1 1 1 4 1 6 3 2 2 1 3 1 2 3 3 2 2 2 1 3 3 2 2 3 6 1 8 0 6 1 1 3 3 5 8 3 1 1 3 2 1 1 1 1 8 0 8 7 4 7 1 2 5 7 9 1 1 2 3 1 1 3 2 1 7 0 9 6 1 8 8 6 8 1 1 9 2 1 1 1 1 2 3 3 5 3 5 1 1 3 4 1 6 0 8 1 6 7 2 1 1 4 2 2 1 2 1 3 2 1 7 0 8 7 1 1 5 1 6 2 7 3 1 3 1 3 1 1 1 6 0 8 3 4 9 8 1 9 9 6 2 2 3 3 1 2 1 4 3 3 6 7 2 7 2 8 3 6 2 5 4 3 7 1 7 0 7 1 5 3 4 8 8 4 1 2 2 1 1 1 3 1 7 0 10 4 3 4 4 9 9 2 3 1 1 2 2 3 1 3 3 3 1 5 0 11 1 3 6 9 5 4 4 3 6 8 1 3 1 3 1 2 3 5 2 2 5 4 1 3 5 1 5 0 11 5 8 4 3 2 6 8 1 6 4 3 1 3 1 3 3 1 6 0 8 7 6 5 4 9 5 1 2 3 2 1 2 2 1 1 5 0 7 1 6 4 8 6 1 8 2 1 2 3 1 2 2 4 4 2 3 7 1 8 0 6 1 9 5 9 5 1 1 1 3 1 2 3 1 3 1 5 0 9 8 1 3 3 3 1 7 2 3 2 1 1 2 2 1 7 0 6 3 4 1 9 1 4 1 1 1 1 3 1 1 1 2 2 3 4 1 4 3 6 1 7 0 6 4 8 3 6 1 8 1 1 2 1 1 3 1 1 7 0 10 1 8 1 6 5 2 3 8 5 8 3 2 3 1 2 1 1 1 9 0 7 7 1 6 5 5 8 3 2 3 3 1 3 1 1 1 3 3 3 1 3 1 1 3 4 1 8 0 8 1 5 2 9 2 1 3 2 3 2 2 3 2 1 1 1 1 9 0 9 3 5 2 6 4 7 1 3 3 2 1 1 3 3 3 2 2 1 1 9 0 10 8 3 5 1 2 9 4 6 5 1 1 1 1 3 1 1 1 1 2 1 1 2 1 5 6 3 1 4 4 3 4 1 7 0 6 1 7 3 9 6 8 3 1 1 3 3 1 1 1 6 0 9 1 6 5 7 6 8 3 3 4 3 1 2 1 3 3 1 9 0 7 9 6 2 9 4 1 9 1 3 3 3 1 2 3 1 1 4 2 3 4 3 7 1 7 0 8 3 2 8 1 7 9 2 9 2 2 2 2 2 1 3 1 8 0 10 5 5 2 8 1 3 4 8 5 7 3 1 2 2 1 1 3 1 1 8 0 6 1 4 1 4 2 9 2 1 1 1 2 2 1 3 4 1 1 3 2 3 3 3 5 1 6 0 10 9 9 7 4 1 4 5 1 5 7 1 1 3 1 1 2 1 5 0 11 6 8 8 1 1 7 8 9 1 1 3 3 1 2 2 1 1 8 0 9 1 9 9 7 8 5 4 3 9 3 2 3 2 1 3 1 3 3 3 2 2 4 3 6 1 7 0 9 8 7 7 1 4 1 8 7 7 3 3 1 3 3 2 3 1 7 0 10 1 5 7 9 5 2 6 3 1 8 3 1 1 2 1 1 3 1 5 0 11 2 2 3 3 1 8 5 6 1 1 4 3 1 3 1 2 4 5 3 4 5 3 1 6 4 1 5 5 3 4 1 9 0 7 6 8 7 1 1 1 8 1 2 1 1 3 2 1 2 1 1 9 0 7 5 8 3 1 7 5 3 3 1 2 2 3 3 1 3 1 1 9 0 11 7 8 3 1 5 3 7 3 1 9 9 1 1 2 2 1 1 1 2 1 2 2 4 1 3 5 1 6 0 7 5 2 6 1 4 7 1 3 2 3 2 1 2 1 8 0 6 5 1 8 6 4 3 1 2 1 1 1 2 1 3 1 6 0 9 7 3 9 9 1 1 1 5 1 2 2 3 1 1 2 4 2 1 5 3 3 6 1 8 0 8 8 1 8 3 4 4 1 8 2 2 1 2 1 1 2 1 1 7 0 6 8 1 2 4 8 1 3 1 3 2 2 1 2 1 8 0 7 8 1 7 9 2 2 6 1 3 1 1 3 2 2 3 3 1 5 1 4 1 3 6 1 7 0 7 7 1 8 7 5 5 2 2 1 1 1 2 2 3 1 7 0 10 8 7 1 7 2 5 9 2 7 7 2 1 3 3 3 3 3 1 5 0 6 8 3 6 7 4 1 2 2 1 2 1 1 5 3 4 2 5 3 4 1 5 0 9 7 1 1 5 2 2 1 6 4 1 2 1 2 3 1 6 0 8 2 5 1 9 9 3 2 4 3 2 3 3 2 1 1 7 0 6 4 3 1 1 4 9 3 1 2 1 3 1 1 2 2 4 1 4 3 2 3 1 5 4 3 4 1 5 0 7 7 5 9 6 1 3 4 1 1 1 1 1 1 7 0 8 2 2 9 1 6 1 9 2 1 1 3 1 2 1 1 1 9 0 10 7 6 5 1 6 8 5 6 9 7 3 3 3 1 1 2 2 3 2 2 5 2 3 3 4 1 6 0 8 3 8 1 8 7 1 1 4 2 3 1 3 2 3 1 9 0 8 9 8 4 1 5 7 7 2 1 1 1 2 3 2 3 2 1 1 6 0 6 1 1 3 3 4 3 1 1 3 3 2 3 5 2 1 3 3 7 1 6 0 11 5 2 3 5 8 1 8 9 8 2 4 1 1 3 3 1 1 1 9 0 10 6 8 7 1 1 3 8 9 4 5 1 3 3 2 2 1 1 1 1 1 6 0 9 7 1 1 9 9 2 1 7 7 2 3 1 1 1 1 1 2 4 1 3 2 4 3 6 1 9 0 11 6 8 1 2 3 4 1 1 4 5 1 2 1 1 2 2 1 2 1 3 1 5 0 7 8 1 8 1 2 4 5 1 2 2 3 2 1 6 0 7 1 1 6 3 7 3 9 2 2 2 1 1 1 3 1 2 3 2 5 3 4 1 9 0 10 9 3 5 2 5 5 1 3 3 1 2 2 1 3 1 3 3 1 1 1 6 0 6 1 4 7 9 9 7 3 3 1 2 2 1 1 5 0 7 5 7 6 1 3 7 5 2 2 1 1 3 2 1 2 2 4 7 1 3 4 4 3 5 1 8 0 9 9 2 1 9 9 9 3 4 2 1 2 3 2 2 2 3 2 1 8 0 7 8 6 2 1 7 2 8 1 2 1 2 2 3 1 3 1 8 0 8 1 3 4 1 3 6 8 9 1 3 2 3 3 1 2 1 1 1 4 3 1 3 7 1 7 0 7 3 7 3 4 9 6 1 1 3 3 2 1 1 1 1 6 0 9 5 7 6 8 5 7 1 8 8 3 1 1 3 2 3 1 5 0 8 4 1 2 6 1 7 6 4 1 3 3 3 1 1 4 3 1 5 4 5 3 4 1 8 0 11 9 2 7 3 6 8 9 1 8 2 1 3 1 2 2 2 3 2 1 1 8 0 9 3 1 4 7 4 8 4 7 5 1 3 3 1 2 1 1 2 1 9 0 8 7 9 1 6 5 8 9 5 1 1 3 2 2 1 3 1 2 5 5 3 2 3 4 1 5 0 9 9 1 9 8 2 7 2 1 2 3 2 3 1 1 1 6 0 10 5 2 4 1 6 8 6 8 3 7 2 1 2 1 1 2 1 9 0 7 9 4 3 4 9 5 1 3 1 3 3 2 1 3 2 2 3 3 1 1 6 4 6 4 5 3 3 5 1 5 0 10 9 5 6 4 8 1 4 9 2 8 1 3 2 2 1 1 8 0 6 1 2 6 6 7 1 1 2 1 1 1 1 3 1 1 8 0 7 1 2 6 9 3 7 3 3 3 2 1 3 3 1 3 2 5 2 3 1 3 7 1 7 0 9 8 8 9 7 5 8 4 4 1 1 3 1 3 2 3 1 1 7 0 11 3 8 2 7 1 3 5 4 2 1 8 3 1 3 1 1 3 1 1 7 0 9 4 1 1 3 1 1 7 1 8 2 1 1 2 1 1 2 1 5 3 5 2 3 3 3 5 1 5 0 7 4 8 3 1 7 7 2 2 2 2 1 1 1 5 0 7 4 9 4 1 1 8 5 2 2 1 1 2 1 9 0 6 7 9 8 1 7 7 3 3 2 1 1 3 2 2 3 5 4 4 1 3 3 7 1 7 0 6 9 1 4 5 4 9 2 2 2 3 3 1 3 1 5 0 9 5 7 1 8 7 1 7 9 3 1 1 1 1 3 1 6 0 8 6 1 9 8 9 7 5 1 3 2 3 1 2 1 3 2 2 1 5 5 1 3 5 1 8 0 6 1 9 1 5 6 4 3 3 1 1 1 3 3 2 1 8 0 8 4 6 8 5 6 7 1 6 3 3 3 3 2 3 2 1 1 9 0 6 5 6 1 5 5 7 1 3 2 1 1 1 3 1 3 1 1 2 3 3 1 4 1 8 2 6 2 5 5 3 4 1 9 0 7 1 8 2 8 1 1 6 2 3 3 1 3 2 2 1 2 1 7 0 10 6 5 2 4 7 6 3 1 1 9 1 3 2 2 3 3 1 1 6 0 11 1 4 8 1 7 6 9 6 6 4 4 3 3 1 2 1 1 3 2 1 4 3 6 1 8 0 8 2 1 8 7 5 7 9 2 2 1 2 2 1 3 1 2 1 7 0 6 4 1 9 4 1 3 3 1 1 2 3 2 3 1 7 0 7 6 7 6 8 8 1 8 1 2 1 1 3 1 1 1 5 1 2 4 5 3 6 1 5 0 6 1 7 5 7 6 1 1 1 2 2 2 1 8 0 7 2 1 5 7 8 1 6 3 1 2 1 2 1 2 3 1 9 0 10 7 5 1 9 9 1 7 5 3 6 1 1 1 1 2 1 1 1 2 1 3 5 4 4 4 3 5 1 7 0 6 5 1 9 5 3 6 2 1 3 3 3 1 2 1 5 0 7 1 5 7 1 3 7 7 3 3 1 2 3 1 7 0 11 5 3 1 9 5 1 4 1 9 5 2 1 3 2 1 1 1 2 4 4 4 4 2 3 5 1 8 0 10 1 6 1 3 5 6 7 7 1 6 3 3 3 2 2 1 1 1 1 7 0 10 1 7 8 1 3 2 7 6 3 7 1 1 3 1 1 2 1 1 6 0 7 8 4 4 7 5 2 1 3 1 2 2 2 1 3 1 4 5 4 3 5 1 2 3 4 4 3 7 1 6 0 10 2 5 7 8 5 7 5 1 9 4 2 3 1 2 2 3 1 5 0 11 1 3 1 6 9 8 5 4 7 8 9 3 1 3 2 3 1 5 0 9 4 1 8 1 3 2 1 5 2 3 1 1 2 3 1 5 4 1 4 3 1 3 5 1 5 0 9 3 3 4 5 1 1 8 5 1 2 1 2 1 1 1 9 0 8 4 6 5 1 4 3 3 5 1 3 1 2 1 3 3 2 2 1 6 0 9 6 2 3 1 2 3 3 9 9 2 2 3 1 3 1 5 4 5 4 3 3 4 1 9 0 7 8 1 8 1 5 7 9 3 2 2 3 1 3 1 1 1 1 9 0 7 1 9 9 3 1 7 7 2 1 2 1 3 3 1 2 3 1 6 0 8 3 8 3 1 5 3 9 8 1 2 1 2 1 1 4 1 4 5 3 4 1 8 0 10 1 3 9 2 6 3 7 3 4 9 3 2 1 1 1 3 1 1 1 5 0 10 7 3 8 1 4 1 8 6 7 8 1 2 3 1 2 1 9 0 11 4 3 1 9 6 7 9 5 4 3 9 1 1 2 3 3 2 1 3 2 5 2 1 4 4 4 6 4 4 5 3 7 1 9 0 9 8 1 8 5 2 4 1 4 6 2 3 1 1 1 1 1 2 3 1 6 0 10 9 5 4 1 4 7 2 6 5 4 3 2 1 3 1 1 1 9 0 9 1 4 2 9 6 4 9 6 4 3 3 3 2 1 1 1 2 1 1 2 1 5 4 3 4 3 5 1 9 0 11 1 5 9 6 5 9 2 9 1 1 1 3 3 2 1 1 1 1 3 1 1 7 0 11 3 1 2 5 1 1 2 3 3 9 6 2 3 1 3 3 1 2 1 6 0 7 8 1 8 3 1 6 5 1 2 2 3 1 2 3 1 5 5 2 3 4 1 8 0 6 3 1 5 1 3 1 1 3 1 1 1 1 3 2 1 7 0 9 1 4 2 6 9 9 9 9 1 2 2 1 1 2 1 2 1 6 0 9 9 4 1 7 1 1 9 8 5 3 1 1 3 1 1 3 4 3 4 3 4 1 6 0 10 7 2 8 2 1 7 4 2 7 6 3 3 3 1 3 1 1 7 0 11 2 9 4 1 5 2 6 6 6 6 6 3 1 1 1 2 2 1 1 9 0 8 8 1 1 9 3 5 3 4 1 3 2 2 1 3 2 3 1 1 3 3 1 6 1 1 4 6 4 5 3 7 1 7 0 11 6 4 5 1 8 7 2 4 3 3 7 1 3 1 3 2 1 1 1 6 0 10 5 5 9 8 8 5 2 7 1 4 3 1 2 3 3 1 1 6 0 7 3 3 6 1 2 9 3 3 3 2 1 3 1 3 4 3 4 3 2 1 3 4 1 9 0 6 6 9 6 1 9 9 1 1 1 3 2 2 3 3 3 1 6 0 7 8 1 1 9 5 7 3 2 1 3 1 2 1 1 8 0 9 3 2 4 4 5 8 7 9 1 2 3 1 3 2 3 3 1 4 4 1 3 3 6 1 8 0 7 2 8 5 8 9 3 1 1 1 1 2 2 1 1 3 1 9 0 6 1 9 9 5 1 4 1 3 2 1 1 1 2 2 1 1 6 0 7 7 2 1 1 2 1 2 1 1 1 2 1 1 1 5 2 2 3 5 3 5 1 9 0 10 8 6 9 6 5 5 6 1 8 3 1 1 3 2 3 1 2 3 1 1 9 0 8 4 1 7 3 7 7 6 1 1 1 3 2 1 2 1 2 3 1 5 0 10 3 9 6 8 1 1 8 3 7 7 1 1 3 2 2 4 3 5 1 1 2 1 2 2 2 5 5 3 5 1 7 0 10 6 1 3 2 8 1 4 7 4 2 1 1 3 2 2 2 2 1 9 0 10 8 1 5 6 3 1 1 4 6 5 2 3 1 3 2 1 3 3 2 1 6 0 9 1 3 6 9 3 6 2 7 5 2 2 1 2 2 3 5 4 1 2 5 3 4 1 6 0 9 3 6 5 1 9 8 3 1 1 2 2 1 1 1 1 1 7 0 11 4 8 2 6 1 7 4 9 7 8 2 1 1 2 1 2 1 1 1 8 0 10 1 4 6 3 6 8 6 9 1 7 1 3 3 3 1 1 1 1 5 5 2 5 3 6 1 8 0 6 3 7 5 1 6 8 3 1 1 1 1 1 1 2 1 5 0 10 5 1 9 1 5 1 7 4 4 4 3 2 1 1 1 1 7 0 7 6 2 7 1 2 5 4 3 2 3 1 3 1 1 2 1 1 1 2 4 3 6 1 7 0 10 8 5 1 2 2 4 2 5 7 6 1 2 3 2 1 3 3 1 7 0 10 8 9 3 9 6 1 2 1 2 2 1 1 3 1 1 2 1 1 7 0 7 1 4 9 7 5 9 1 3 1 2 1 3 3 3 1 2 2 4 4 3 3 6 1 9 0 11 8 7 4 4 1 3 6 5 6 9 6 2 2 2 1 1 2 3 1 1 1 7 0 6 6 2 1 6 1 9 1 1 1 1 2 1 1 1 9 0 7 3 9 1 3 6 5 1 2 1 2 2 2 2 1 1 3 2 2 2 4 5 3 5 2 5 2 1 5 3 3 7 1 9 0 9 3 5 1 4 6 9 3 1 3 3 3 3 1 2 2 1 2 2 1 8 0 10 7 7 4 1 1 8 2 1 2 9 3 3 3 2 3 1 1 1 1 7 0 11 8 5 9 3 8 7 6 3 1 2 9 3 1 2 1 1 1 1 2 4 5 2 1 3 1 3 6 1 5 0 6 6 7 1 8 2 2 1 3 1 2 1 1 9 0 7 6 1 7 7 1 7 1 1 1 1 3 2 3 1 2 2 1 9 0 9 4 3 1 2 9 6 1 4 3 3 2 1 1 3 2 2 1 2 1 4 2 2 3 1 3 5 1 9 0 10 8 1 4 1 1 1 1 1 5 1 1 3 3 2 2 2 3 3 1 1 8 0 8 9 1 8 4 8 7 1 8 3 2 1 1 3 1 3 2 1 9 0 7 5 9 2 6 7 1 5 2 3 3 1 3 3 3 1 3 3 4 1 1 1 3 4 1 7 0 9 7 8 9 1 2 8 6 3 5 2 1 1 1 1 2 2 1 8 0 8 5 2 2 1 9 2 5 1 3 2 1 3 2 3 1 2 1 6 0 7 3 2 1 9 3 3 1 1 3 2 1 3 1 1 3 1 1 3 5 1 7 0 7 1 9 9 3 5 8 5 2 1 1 3 1 2 3 1 7 0 10 1 7 7 8 2 2 7 7 1 5 2 3 3 1 1 3 3 1 9 0 7 2 7 2 4 8 1 4 1 2 1 3 2 1 1 1 2 4 4 2 2 4 2 3 7 3 7 7 3 4 4 3 4 1 7 0 8 6 9 1 2 9 5 9 7 2 3 1 2 2 1 2 1 9 0 11 3 8 1 3 4 9 1 1 4 9 4 3 1 3 3 1 1 3 3 2 1 5 0 7 7 1 4 6 3 7 3 2 3 3 1 1 4 4 3 1 3 5 1 8 0 11 3 4 6 9 5 1 1 7 6 1 4 1 3 3 1 3 3 1 3 1 7 0 10 3 1 8 5 2 6 1 8 7 6 1 2 3 3 1 2 3 1 7 0 8 5 2 1 1 5 8 9 5 1 1 2 3 2 1 1 4 3 4 1 1 3 4 1 8 0 11 3 1 3 1 1 7 2 8 1 7 6 1 2 1 2 1 1 3 3 1 6 0 11 9 3 6 5 3 1 6 2 2 9 4 3 1 3 2 3 2 1 6 0 6 5 1 7 2 7 1 3 3 2 1 2 2 2 2 4 1 3 5 1 7 0 8 9 9 1 1 6 5 6 7 3 1 2 1 3 1 1 1 8 0 6 1 7 3 7 2 6 1 3 3 3 2 1 1 3 1 6 0 9 9 7 1 9 2 6 3 1 5 1 3 1 1 1 1 1 3 1 2 4 3 1 5 2 4 5 3 6 1 5 0 11 8 8 4 3 1 7 7 6 1 7 5 1 2 1 1 3 1 6 0 10 1 1 2 6 1 1 8 6 3 7 3 3 3 1 1 1 1 9 0 9 6 2 6 7 5 1 8 6 3 1 1 3 3 3 1 2 1 2 3 2 3 5 5 4 3 5 1 9 0 11 3 7 3 1 6 3 3 1 5 6 8 1 2 2 2 1 2 3 3 1 1 5 0 6 9 7 6 2 1 5 1 3 1 3 2 1 6 0 10 1 9 8 5 4 1 7 2 7 7 3 3 3 1 1 3 5 3 3 2 3 3 6 1 9 0 8 2 4 7 3 6 1 5 2 3 1 2 1 1 2 3 3 3 1 8 0 10 9 2 9 8 9 4 1 4 1 1 1 3 3 3 2 2 1 1 1 9 0 7 4 2 2 9 1 4 7 2 2 1 2 3 2 3 1 2 5 2 1 1 5 4 3 4 1 7 0 7 1 9 3 2 8 1 5 3 1 1 2 1 2 1 1 9 0 11 3 9 1 5 4 9 3 8 1 5 4 3 1 3 1 3 2 3 2 3 1 9 0 6 1 9 7 8 1 1 3 2 1 1 3 2 2 2 1 3 4 4 2 5 3 6 5 2 5 4 3 5 1 9 0 10 1 5 4 5 2 6 9 6 7 4 3 1 3 3 1 2 1 1 1 1 7 0 6 3 8 6 1 3 3 3 3 3 1 1 3 3 1 5 0 10 7 2 5 2 5 1 9 3 8 9 1 1 1 3 2 4 5 3 2 3 3 6 1 7 0 10 4 1 1 6 5 9 5 5 4 8 2 2 2 3 1 1 1 1 6 0 8 5 6 1 3 4 1 2 3 1 2 3 1 1 1 1 8 0 11 9 7 2 8 1 4 2 6 2 4 1 1 1 3 1 2 2 1 2 2 3 1 3 4 5 3 5 1 9 0 10 4 1 9 5 2 5 3 9 5 6 3 3 1 2 2 1 1 2 3 1 7 0 10 5 7 5 9 4 1 2 8 1 8 3 2 3 2 3 3 1 1 8 0 9 5 2 1 3 1 1 4 1 2 1 2 2 2 1 1 2 1 1 1 4 2 1 3 4 1 9 0 11 9 6 1 9 3 2 4 8 1 4 3 3 2 2 1 2 1 2 1 1 1 7 0 9 3 3 5 4 1 5 2 6 6 2 2 1 1 1 1 3 1 9 0 8 8 2 8 1 1 9 9 2 2 3 3 2 1 2 1 3 2 1 3 2 2 3 7 1 5 0 11 2 1 9 8 3 9 5 1 7 4 1 1 1 2 2 1 1 6 0 6 5 6 6 5 1 1 1 3 3 1 2 1 1 6 0 10 4 4 7 7 3 1 9 8 8 6 1 2 1 3 1 1 1 4 2 1 2 1 3 2 6 4 5 5 3 3 4 1 6 0 9 8 2 1 1 2 3 4 6 6 3 2 3 3 1 1 1 5 0 6 5 1 2 5 5 8 2 1 2 2 2 1 5 0 10 9 8 3 6 1 1 3 1 3 5 3 3 1 1 3 1 2 3 4 3 6 1 9 0 7 8 6 1 1 5 9 2 3 1 1 2 2 1 2 1 3 1 8 0 11 2 7 8 1 7 5 5 5 6 4 3 3 1 3 3 1 2 3 2 1 7 0 9 1 9 3 1 5 4 3 3 8 2 2 1 3 2 2 1 4 3 5 2 4 1 3 5 1 9 0 11 7 4 2 1 5 5 6 3 1 9 1 1 1 2 1 2 3 1 1 2 1 9 0 11 1 5 4 3 9 9 8 1 7 1 9 1 2 1 3 2 2 3 2 2 1 8 0 8 1 4 5 2 3 5 9 1 2 3 2 1 1 2 1 2 4 3 3 2 2 3 7 1 8 0 11 4 4 1 5 8 7 5 8 4 1 2 1 1 3 1 2 3 1 1 1 6 0 8 9 6 7 2 4 8 1 1 1 1 1 3 3 3 1 6 0 10 1 5 7 5 3 1 6 9 7 4 3 1 1 3 1 3 3 3 2 4 5 5 4 3 7 1 9 0 10 6 9 4 8 5 3 4 5 1 2 2 3 1 1 1 2 2 1 1 1 7 0 9 4 1 1 1 2 3 8 7 4 3 2 2 1 2 3 1 1 6 0 10 7 7 9 3 8 8 7 7 1 1 3 2 3 3 1 1 3 3 5 1 3 4 5 1 4 6 4 4 3 7 1 8 0 8 2 7 3 1 1 1 8 1 3 3 1 2 1 1 2 1 1 9 0 11 9 2 6 1 6 7 1 4 9 5 5 2 1 3 1 1 3 1 2 1 1 7 0 6 8 9 9 2 1 2 3 2 2 1 2 1 2 5 2 5 5 5 1 2 3 5 1 5 0 11 4 9 6 3 7 1 7 5 7 2 9 1 3 2 3 3 1 6 0 9 8 8 1 2 9 6 5 8 3 1 1 2 3 1 1 1 8 0 10 5 2 4 7 8 7 3 6 9 1 1 3 2 2 1 2 3 3 3 4 2 4 1 3 6 1 5 0 10 5 8 1 3 9 5 6 1 1 7 2 2 1 1 2 1 5 0 8 1 9 8 2 3 5 6 1 1 2 1 2 3 1 6 0 10 1 7 1 3 1 7 1 2 7 3 2 1 1 3 1 1 2 3 3 1 1 3 3 4 1 7 0 10 3 1 1 5 2 6 4 7 4 5 3 3 2 1 1 2 3 1 9 0 10 1 1 7 9 8 1 6 1 4 1 1 1 3 1 3 1 2 3 1 1 8 0 10 2 3 4 1 6 7 3 6 6 5 2 1 2 3 1 1 1 2 5 3 3 3 1 3 2 1 5 3 3 5 1 5 0 11 4 1 9 2 7 1 6 8 3 9 1 2 1 1 2 3 1 9 0 6 4 1 8 4 6 4 1 2 1 1 3 1 3 2 2 1 5 0 8 9 2 1 9 1 8 7 4 1 1 3 3 1 2 5 1 2 2 3 5 1 6 0 9 3 6 2 3 5 8 5 1 3 2 1 3 2 1 1 1 7 0 8 1 1 5 3 3 4 2 5 2 1 2 2 1 1 3 1 6 0 9 1 1 8 1 7 5 1 4 9 2 1 2 2 1 1 2 1 2 4 1 3 5 1 8 0 10 8 1 5 7 4 5 7 1 1 1 2 3 1 2 2 3 1 3 1 8 0 6 1 2 8 8 1 5 1 1 1 3 2 2 1 2 1 5 0 8 6 9 9 6 5 4 3 1 1 1 3 1 3 3 3 1 5 3 3 7 1 6 0 8 7 6 7 1 3 9 7 8 2 1 1 3 3 3 1 8 0 10 3 8 9 8 5 7 2 3 1 2 2 1 2 2 1 1 2 1 1 9 0 7 4 3 4 1 9 2 8 1 3 1 2 2 2 1 2 3 4 1 5 1 3 4 1 3 4 1 5 0 6 2 5 6 1 7 1 3 2 1 3 1 1 5 0 6 3 9 6 7 7 1 2 2 1 2 3 1 5 0 9 4 3 2 1 7 6 1 3 7 3 3 2 1 2 2 4 4 1 4 3 4 5 5 3 7 1 8 0 9 1 5 4 4 5 1 7 1 4 3 3 2 1 2 1 2 3 1 6 0 8 9 2 6 4 1 1 6 4 2 1 1 1 1 1 1 6 0 9 9 7 2 6 3 3 7 1 4 2 1 1 3 1 3 2 3 1 3 2 4 5 3 6 1 6 0 8 5 1 7 1 7 5 1 1 2 2 3 3 2 1 1 6 0 9 4 9 6 2 1 9 4 1 6 1 3 2 2 3 3 1 6 0 11 5 9 9 9 2 8 6 1 9 4 1 1 1 1 3 3 1 1 4 3 2 4 5 3 6 1 5 0 9 2 1 4 2 8 8 6 5 3 3 1 3 3 3 1 5 0 8 8 7 8 2 1 6 9 1 1 1 1 3 3 1 5 0 6 3 1 3 9 9 1 1 1 3 1 2 4 3 5 2 3 2 3 7 1 8 0 7 9 6 4 5 1 1 8 3 1 2 1 2 2 2 3 1 9 0 8 1 8 4 8 5 4 1 1 3 3 2 2 1 2 2 1 1 1 9 0 8 1 1 2 6 3 1 8 9 1 3 2 1 2 1 3 1 1 4 4 2 2 3 1 2 3 5 1 8 0 8 4 4 3 4 8 8 1 7 1 3 1 1 1 3 3 1 1 5 0 9 5 4 1 5 1 3 5 7 1 3 2 1 3 3 1 6 0 8 4 3 5 8 5 1 2 3 1 1 2 2 2 1 2 5 1 3 1 5 4 7 5 6 1 6 4 7 3 5 3 3 7 1 7 0 6 9 4 7 1 2 2 3 1 3 2 1 3 1 1 6 0 8 2 5 7 1 8 5 1 2 1 2 2 1 1 2 1 8 0 10 4 7 1 1 8 1 1 4 7 5 1 2 2 2 1 3 3 1 1 3 2 3 3 4 1 3 7 1 9 0 9 1 1 8 9 2 9 5 7 3 1 1 2 3 2 1 1 3 1 1 7 0 11 1 9 7 5 8 5 1 1 8 8 3 2 3 3 1 1 2 1 1 6 0 6 9 8 3 1 8 1 1 2 3 1 1 3 3 1 4 3 5 4 4 3 5 1 7 0 7 6 5 1 9 6 5 8 2 3 2 3 1 2 3 1 8 0 6 1 3 8 7 8 6 1 1 3 2 2 1 2 2 1 6 0 11 4 3 9 1 6 1 2 9 8 9 9 2 1 3 3 1 1 5 3 1 3 5 3 6 1 9 0 9 6 1 9 4 1 9 5 8 9 2 1 1 3 1 3 1 1 3 1 9 0 11 6 9 4 8 7 2 1 8 4 1 6 1 2 1 1 3 2 3 3 3 1 7 0 8 1 3 4 6 1 4 3 3 2 1 1 3 1 3 1 3 4 5 3 3 5 3 5 1 6 0 7 1 4 5 9 3 1 3 2 3 1 1 3 2 1 5 0 11 8 1 9 4 1 1 1 4 2 4 5 3 1 2 3 2 1 5 0 6 6 1 8 6 2 8 2 1 3 2 3 3 2 4 3 5 1 4 5 5 5 3 6 1 5 0 10 6 6 8 4 5 1 6 6 1 9 2 2 3 2 1 1 5 0 8 5 1 9 9 3 1 1 5 2 1 1 2 1 1 9 0 9 9 1 9 1 8 4 1 1 4 3 1 1 1 1 1 3 1 2 5 5 1 2 3 3 3 4 1 9 0 8 1 4 8 4 9 3 1 1 2 2 3 2 1 3 1 3 2 1 5 0 9 1 6 3 6 9 7 1 2 6 2 1 2 1 2 1 8 0 8 6 1 2 2 5 4 3 5 1 1 3 2 1 1 2 3 3 4 2 3 3 5 1 6 0 6 9 5 9 6 5 1 1 3 2 2 3 2 1 7 0 9 3 3 5 6 2 1 2 7 7 1 1 1 1 2 3 3 1 9 0 9 8 1 7 7 5 1 3 2 9 3 3 2 2 1 2 1 1 3 3 4 1 3 1 3 5 1 6 0 10 1 1 5 2 2 9 8 7 6 2 3 2 2 2 1 1 1 6 0 6 7 5 6 2 5 1 1 1 2 1 2 3 1 6 0 9 5 4 3 7 7 9 2 9 1 1 1 1 1 3 1 2 1 3 4 1 3 5 1 5 0 7 7 5 5 2 8 1 3 1 1 3 1 2 1 5 0 8 4 3 9 1 7 3 6 9 3 1 3 1 1 1 5 0 9 2 4 7 6 1 6 6 4 5 1 3 3 1 1 5 3 3 2 1 2 2 2 3 7 5 5 3 4 1 7 0 11 1 3 1 1 6 9 9 3 3 3 1 3 1 1 1 3 2 3 1 9 0 11 2 1 7 7 6 2 1 3 8 4 7 1 1 3 1 2 2 2 2 1 1 5 0 7 1 5 1 5 1 5 3 2 2 3 1 1 5 4 3 3 3 7 1 6 0 10 1 7 9 6 8 2 2 6 1 8 1 1 3 2 2 2 1 8 0 9 7 8 1 5 4 8 7 1 3 3 1 3 3 3 2 1 1 1 5 0 9 6 1 9 8 5 7 1 8 1 2 1 2 3 1 3 1 3 5 5 2 1 3 6 1 5 0 11 3 9 9 5 4 7 1 9 7 8 1 1 1 3 2 1 1 9 0 6 3 5 6 1 5 3 2 2 3 1 1 2 3 2 1 1 8 0 6 3 4 3 3 5 1 2 2 1 2 1 2 1 3 3 1 3 4 3 1 3 7 1 6 0 6 4 2 1 2 1 2 3 3 1 3 3 1 1 5 0 7 1 9 1 2 4 2 3 1 2 3 1 3 1 5 0 7 2 9 1 3 2 9 3 1 2 1 2 1 1 2 2 5 5 3 3 3 4 1 8 0 9 8 9 6 1 9 1 6 3 5 3 3 2 1 3 1 2 2 1 6 0 9 1 9 9 7 3 7 8 8 5 2 1 1 3 2 3 1 8 0 7 2 4 1 1 3 8 6 3 2 3 2 1 2 1 2 4 2 1 3 2 7 4 3 1 5 3 3 6 1 7 0 10 2 1 4 6 1 4 8 5 5 6 1 2 3 2 3 1 1 1 8 0 9 8 7 5 1 5 1 6 1 3 2 1 1 1 1 3 1 1 1 7 0 11 7 2 7 2 9 3 7 8 1 7 5 3 2 2 2 1 3 1 1 2 2 3 1 3 3 7 1 7 0 11 1 6 1 8 5 8 7 5 5 5 8 3 2 1 3 1 1 2 1 5 0 7 9 1 7 1 1 9 8 2 2 3 1 1 1 5 0 11 8 1 9 5 8 5 5 3 4 2 1 1 3 1 3 3 4 1 3 3 1 3 1 3 6 1 8 0 8 3 4 7 3 6 1 5 6 1 1 1 3 1 2 1 3 1 7 0 7 1 8 1 7 1 6 7 1 2 1 1 2 1 1 1 7 0 8 7 7 9 5 1 2 5 1 3 2 3 2 1 3 1 1 3 3 5 3 2 3 7 1 8 0 6 2 1 6 7 6 1 1 1 1 2 2 1 2 2 1 7 0 6 1 5 4 9 6 2 2 2 3 1 2 3 1 1 7 0 9 4 8 5 1 6 3 7 6 2 3 2 1 1 1 1 2 2 4 3 2 5 2 4 3 7 1 5 0 6 8 1 6 4 3 6 3 3 2 2 1 1 9 0 8 3 1 3 2 6 8 9 1 3 3 1 2 1 2 1 2 1 1 8 0 10 4 4 5 4 8 1 1 7 3 9 2 1 3 3 3 3 3 3 3 4 2 1 4 3 5 4 2 5 5 4 3 6 1 5 0 7 3 1 9 7 2 1 4 1 2 1 2 2 1 8 0 6 1 1 9 6 5 4 2 2 3 3 1 3 3 1 1 8 0 6 1 1 2 3 9 6 2 2 1 2 3 2 1 1 2 2 3 1 2 5 3 6 1 7 0 11 8 4 3 8 9 9 1 6 1 4 3 2 2 2 2 1 1 2 1 9 0 7 1 4 8 5 1 7 4 3 1 1 3 3 2 1 1 2 1 8 0 8 2 8 7 2 9 6 1 6 1 3 1 2 3 1 1 1 1 5 4 2 5 1 3 4 1 5 0 9 5 3 7 2 1 8 1 7 4 1 1 1 1 1 1 7 0 8 6 2 1 1 2 6 5 1 1 3 2 3 1 3 2 1 6 0 8 8 1 7 9 5 9 9 3 1 3 1 1 2 1 5 1 3 4 3 5 1 8 0 8 8 4 5 6 7 2 1 2 1 1 2 3 1 3 1 1 1 8 0 8 8 1 5 3 4 9 7 2 2 2 2 3 2 2 2 1 1 6 0 11 5 1 5 6 1 2 3 3 4 2 9 2 1 1 2 2 2 5 2 4 2 3 3 6 1 9 0 6 3 4 2 2 1 7 3 1 3 2 2 1 1 3 3 1 6 0 10 6 1 9 3 1 2 5 2 4 7 2 2 3 1 3 3 1 9 0 10 5 9 1 5 7 6 8 1 7 7 3 1 2 2 1 1 3 2 2 3 2 4 1 2 1 2 5 3 5 5 3 3 7 1 6 0 11 3 5 4 7 2 5 1 5 7 2 9 2 3 1 1 2 1 1 7 0 10 3 2 7 1 5 6 4 1 1 6 2 3 3 1 1 1 2 1 8 0 9 1 1 9 2 9 5 1 5 2 2 3 3 2 1 3 2 1 3 5 4 1 2 1 2 3 5 1 6 0 7 5 1 6 3 8 4 4 2 3 1 1 2 2 1 9 0 6 8 1 9 3 3 5 2 1 1 2 3 3 1 3 1 1 8 0 10 1 7 9 4 1 8 6 2 9 3 3 1 1 2 2 2 1 3 2 4 3 3 1 3 6 1 8 0 11 8 5 9 4 2 7 5 2 1 9 5 3 3 1 1 2 3 3 2 1 7 0 10 2 1 4 2 3 2 5 1 1 2 3 1 2 2 2 1 3 1 8 0 11 5 6 4 7 1 9 1 5 4 2 8 3 2 1 3 1 1 2 3 5 1 3 3 3 4 3 7 1 7 0 8 9 5 3 9 2 6 5 1 3 3 1 3 3 2 2 1 8 0 11 1 2 8 1 2 2 3 2 5 3 8 1 1 1 2 3 2 3 1 1 8 0 7 3 3 2 1 2 6 1 3 2 1 3 1 3 1 1 3 1 3 3 2 3 5 3 6 1 9 0 9 5 5 1 6 5 5 9 2 3 1 2 3 3 2 2 1 2 2 1 6 0 9 1 1 9 1 3 9 1 3 9 2 2 3 1 1 1 1 5 0 11 1 3 2 4 1 1 6 7 5 2 5 2 1 1 3 1 2 4 3 3 3 1 7 1 4 5 3 3 5 1 6 0 11 1 7 5 6 5 6 4 3 1 1 1 1 1 1 1 2 1 1 6 0 11 6 7 5 9 1 5 9 4 1 4 5 2 1 3 1 2 1 1 7 0 11 5 4 7 1 2 1 9 7 4 7 3 2 1 1 3 2 3 1 1 5 1 5 3 3 6 1 7 0 11 9 5 7 9 1 3 8 8 6 6 1 3 1 2 2 1 2 1 1 7 0 7 2 6 1 5 8 5 4 2 1 2 2 1 3 2 1 9 0 10 6 3 5 8 1 8 1 7 1 9 2 1 3 3 1 1 3 2 2 1 4 2 2 5 2 3 6 1 9 0 6 6 8 1 8 3 8 2 1 2 3 1 2 1 3 1 1 5 0 10 1 6 5 1 1 1 2 5 8 2 3 2 1 1 1 1 6 0 8 3 6 8 6 3 5 1 4 3 1 1 1 3 2 1 5 5 3 3 2 3 4 1 9 0 6 5 4 3 6 1 4 1 2 1 1 2 2 2 1 3 1 6 0 8 5 1 5 2 5 6 5 2 1 2 2 1 2 2 1 6 0 11 9 8 3 9 9 1 6 1 1 1 5 2 1 3 2 1 1 3 4 5 3 3 6 1 9 0 8 1 2 4 6 9 1 6 7 1 3 1 3 3 1 1 1 3 1 5 0 10 5 5 7 7 1 7 8 5 2 6 1 2 1 3 2 1 5 0 10 2 4 8 1 9 9 7 1 4 1 1 1 3 2 2 1 5 1 3 4 1 2 2 5 1 6 4 6 2 5 5 3 4 1 9 0 7 6 7 1 1 2 9 9 2 1 2 2 1 3 1 2 1 1 6 0 11 9 2 1 5 1 5 6 8 8 7 9 2 1 2 2 1 3 1 8 0 11 4 1 4 6 2 6 6 1 7 3 6 2 3 2 1 2 3 1 1 4 3 5 2 3 4 1 5 0 11 7 1 1 8 4 9 7 3 8 4 2 2 3 3 1 1 1 8 0 9 1 8 5 8 1 9 3 9 1 1 3 2 1 1 3 1 1 1 8 0 11 6 1 1 5 6 7 5 5 7 1 6 3 1 2 3 2 1 2 1 3 3 4 4 3 4 1 9 0 7 6 3 3 1 4 7 1 3 3 2 1 2 2 2 1 1 1 7 0 6 1 3 7 1 7 8 3 2 3 2 1 1 3 1 6 0 10 1 4 4 1 5 4 5 1 9 9 2 1 2 3 3 2 3 3 4 1 3 6 1 9 0 9 8 7 7 9 1 1 1 5 9 3 2 1 2 1 2 3 1 2 1 8 0 6 6 5 6 1 7 9 2 3 2 1 1 1 3 1 1 5 0 7 9 4 9 7 9 1 3 1 2 1 2 1 1 2 4 5 3 2 3 5 1 8 0 8 1 2 6 1 6 2 8 3 1 3 2 2 3 2 1 3 1 7 0 7 1 7 8 1 4 4 2 1 3 1 3 1 3 1 1 8 0 7 1 5 8 1 4 6 4 1 3 1 1 2 1 3 1 4 1 4 5 3 2 5 2 1 3 5 3 3 5 1 6 0 10 1 8 5 2 4 7 1 4 7 3 1 2 3 3 2 3 1 7 0 6 1 2 1 8 4 2 2 1 1 3 2 2 3 1 9 0 11 1 6 8 3 7 6 9 6 9 1 4 2 3 2 2 3 1 1 1 2 3 5 5 2 4 3 4 1 9 0 6 9 2 1 5 2 7 1 1 2 2 2 2 3 3 1 1 5 0 6 4 5 4 6 1 3 1 3 1 2 1 1 6 0 7 8 8 9 1 2 7 8 3 2 3 1 3 3 2 4 1 5 3 7 1 8 0 7 9 1 9 3 2 1 9 1 1 1 2 2 3 2 2 1 9 0 6 8 4 1 9 2 4 3 3 2 1 2 2 1 3 3 1 6 0 8 1 6 3 7 1 7 7 2 2 2 1 2 1 1 4 3 4 1 3 1 3 3 6 1 5 0 11 1 7 2 8 1 2 1 9 3 6 2 1 1 2 1 2 1 6 0 10 1 1 4 5 3 7 8 3 9 4 1 3 2 1 1 2 1 5 0 7 1 7 6 5 8 1 4 2 3 1 1 2 2 2 4 2 1 4 3 6 1 9 0 7 2 3 8 1 7 1 7 1 1 3 2 3 2 1 3 1 1 8 0 9 8 8 6 2 1 6 5 7 6 2 2 1 2 2 1 1 1 1 9 0 11 4 7 5 1 4 4 4 8 2 5 2 2 3 1 1 2 1 3 2 3 2 2 3 3 1 1 3 1 5 4 5 3 4 1 8 0 8 1 3 9 7 5 7 1 7 1 1 1 1 2 2 2 1 1 8 0 11 8 4 5 4 3 6 9 7 3 1 5 2 1 3 2 3 1 1 1 1 6 0 8 9 1 4 1 5 9 1 8 1 3 3 3 3 1 5 2 4 3 3 4 1 5 0 7 7 2 3 8 5 1 5 1 1 2 3 1 1 7 0 9 1 3 3 9 3 1 8 1 5 1 2 1 2 1 3 2 1 7 0 9 7 4 3 3 9 1 4 8 7 3 1 3 3 2 3 1 2 3 5 2 3 7 1 7 0 6 1 1 5 4 1 5 1 2 2 1 1 3 1 1 7 0 7 9 4 7 9 7 2 1 3 1 1 2 2 2 1 1 9 0 8 5 1 5 2 7 9 8 3 3 3 3 3 1 1 1 2 3 1 5 3 5 3 3 2 3 7 1 8 0 6 8 1 4 1 6 8 1 3 1 3 3 3 1 1 1 9 0 11 6 5 3 3 4 1 4 2 9 8 8 3 2 1 2 2 3 1 1 1 1 5 0 11 8 1 3 9 6 6 6 6 6 5 1 3 1 1 3 1 3 1 1 5 5 3 3 1 2 6 5 2 5 3 3 5 1 9 0 9 2 6 5 8 8 9 2 1 3 2 2 1 3 2 3 3 1 1 1 5 0 10 6 5 8 1 5 2 6 3 9 9 2 3 1 3 3 1 7 0 10 8 4 7 2 3 1 6 2 2 9 1 3 3 3 2 2 1 2 1 1 2 3 3 7 1 9 0 7 2 1 2 2 9 6 7 2 1 2 1 3 2 3 2 2 1 5 0 10 6 2 9 7 5 8 1 9 2 3 2 1 3 3 2 1 8 0 10 7 5 9 6 8 9 7 1 9 9 1 2 2 2 1 3 2 3 3 3 5 2 2 4 5 3 7 1 7 0 11 9 1 4 5 2 2 6 2 1 8 4 2 2 3 1 1 1 3 1 5 0 6 3 8 1 7 6 4 1 1 1 2 2 1 7 0 6 1 5 6 4 8 2 1 1 3 1 3 3 2 4 5 1 1 5 4 3 3 4 1 9 0 10 7 3 2 3 9 9 2 1 4 1 1 2 1 1 1 2 3 3 1 1 9 0 9 2 1 2 4 9 4 1 1 7 1 2 2 3 2 1 1 3 1 1 5 0 7 3 6 5 5 1 4 9 2 1 2 1 3 4 1 3 1 3 7 1 7 0 6 7 9 4 1 1 6 1 3 3 2 1 3 1 1 7 0 8 9 5 4 9 6 6 1 4 1 3 2 1 3 2 3 1 6 0 7 8 1 1 9 3 8 7 2 1 1 1 1 2 3 3 4 3 3 5 2 2 5 1 5 3 3 7 1 9 0 7 9 1 6 3 9 1 3 1 3 2 1 3 2 3 2 1 1 9 0 9 8 1 9 2 7 5 6 6 5 2 2 3 3 1 1 2 1 1 1 9 0 6 4 7 4 1 1 6 2 3 2 2 1 1 1 2 3 1 4 2 4 1 1 4 3 7 1 7 0 11 6 8 7 1 6 5 4 1 2 6 7 2 3 1 3 1 1 1 1 8 0 8 5 4 4 1 9 1 5 6 3 3 3 2 3 1 2 2 1 8 0 8 1 6 5 4 1 1 4 5 1 1 2 3 3 2 2 1 5 3 4 5 3 3 5 3 6 1 7 0 6 1 1 8 2 3 4 1 3 3 3 2 1 1 1 8 0 10 9 7 2 9 4 1 9 9 8 9 3 1 3 2 2 1 3 2 1 9 0 8 9 4 1 8 8 8 8 6 1 1 1 2 1 2 3 2 2 5 5 2 2 1 1 3 5 1 9 0 6 1 8 9 6 5 4 3 3 1 3 1 1 1 1 2 1 7 0 9 6 8 9 1 5 8 4 7 6 3 3 1 1 3 2 1 1 6 0 11 9 8 9 3 8 2 3 6 5 1 2 3 2 1 3 3 1 5 5 1 3 5 3 7 1 6 0 11 8 5 7 8 6 1 3 4 3 1 6 2 1 2 3 1 1 1 8 0 7 3 3 6 1 2 1 6 3 1 2 2 2 1 2 2 1 9 0 11 8 1 1 7 1 4 4 9 7 8 6 2 1 3 2 1 1 1 1 1 5 1 4 1 5 2 1 2 7 1 4 3 3 6 1 5 0 7 6 9 4 3 9 9 1 1 1 2 2 3 1 8 0 6 1 1 1 8 7 1 2 1 1 1 3 1 3 2 1 6 0 7 1 4 3 8 2 6 9 1 1 1 2 2 1 5 5 1 5 2 2 3 6 1 8 0 11 5 2 7 1 1 8 9 5 3 2 7 3 2 1 2 2 1 1 3 1 9 0 10 3 5 4 7 9 3 8 6 1 8 2 1 2 3 1 1 2 2 3 1 9 0 8 9 7 8 6 1 5 1 1 2 2 1 1 2 3 1 1 3 3 4 2 3 1 1 3 6 1 9 0 11 8 6 5 3 2 7 1 1 7 3 4 3 1 2 1 1 2 2 1 3 1 6 0 7 1 5 1 1 8 6 6 1 3 1 1 2 3 1 8 0 7 1 6 3 8 4 9 1 2 2 3 2 1 1 3 1 1 4 4 3 3 5 3 4 1 5 0 8 9 3 7 4 3 9 1 8 1 3 1 3 3 1 8 0 6 1 8 9 1 4 3 1 1 1 1 1 2 1 2 1 8 0 6 1 3 7 6 4 7 2 1 1 1 3 1 2 3 5 1 1 2 3 6 4 2 3 6 3 4 3 3 6 1 7 0 7 7 6 7 1 1 9 1 3 3 1 1 1 1 3 1 6 0 11 9 4 4 5 2 2 1 8 8 3 1 1 2 2 2 1 1 1 8 0 11 8 7 1 1 7 4 2 1 1 2 2 2 1 1 3 3 2 2 3 1 2 3 5 4 5 3 5 1 6 0 11 8 8 6 9 1 3 9 8 8 3 2 2 2 3 3 1 1 1 6 0 10 6 2 6 8 1 1 7 2 4 9 1 1 2 1 2 1 1 6 0 10 1 3 7 6 1 2 3 9 2 1 1 2 2 3 2 1 4 1 3 1 2 3 5 1 9 0 7 1 5 7 5 8 1 4 3 1 1 3 1 2 2 2 2 1 6 0 9 1 8 8 4 4 6 5 4 2 3 3 2 2 1 1 1 5 0 6 1 5 3 4 1 6 1 3 1 1 2 3 3 1 3 2 3 7 1 5 0 6 1 5 8 4 4 1 3 2 1 3 1 1 6 0 6 9 8 1 6 1 2 2 3 2 3 2 1 1 6 0 7 9 4 8 1 8 7 1 1 3 1 1 3 2 5 4 2 2 1 3 3 3 5 4 4 5 3 4 1 8 0 9 6 9 1 1 2 4 6 8 2 2 3 1 1 2 1 3 3 1 7 0 9 1 3 4 8 8 6 1 5 9 3 2 3 1 2 1 1 1 5 0 8 6 4 9 1 2 2 1 5 3 1 1 3 1 3 3 5 3 3 4 1 5 0 10 4 5 3 9 2 2 7 1 4 7 2 1 1 2 2 1 6 0 10 4 3 1 7 5 1 1 6 8 1 1 2 2 1 2 1 1 7 0 8 7 3 2 8 3 1 6 2 1 1 1 1 3 1 1 3 1 4 5 3 5 1 9 0 9 3 1 9 1 5 7 5 3 4 3 1 2 1 1 1 1 3 1 1 8 0 10 1 1 8 2 3 6 9 5 6 8 2 3 3 2 2 3 1 1 1 6 0 11 1 8 5 4 5 3 3 6 6 6 2 2 1 1 2 1 1 4 5 1 3 3 3 6 1 8 0 9 7 6 5 9 3 1 4 2 2 2 1 1 2 2 3 2 1 1 6 0 6 8 9 6 9 1 2 1 1 2 3 2 1 1 8 0 8 4 1 7 4 3 7 7 8 2 1 1 2 1 1 2 3 3 5 2 2 5 1 2 1 4 3 3 5 5 3 7 1 5 0 10 1 6 9 2 7 7 5 1 6 3 3 3 3 1 1 1 7 0 9 3 5 5 7 2 8 9 5 1 2 3 3 2 1 1 1 1 5 0 11 3 2 6 1 5 1 9 9 9 1 7 2 2 2 1 1 2 5 1 2 1 3 1 3 7 1 5 0 8 1 7 2 2 1 5 8 5 1 2 1 2 1 1 9 0 7 5 5 1 3 9 9 3 1 3 1 2 3 1 3 3 2 1 6 0 8 7 1 5 9 1 1 9 9 2 3 1 2 3 2 1 2 3 4 2 5 4 3 6 1 7 0 9 2 7 9 1 3 6 5 6 4 1 2 1 1 1 3 2 1 6 0 9 6 2 5 5 5 6 1 2 7 2 1 2 1 2 1 1 7 0 9 3 6 3 4 1 5 5 6 4 3 3 2 1 1 1 1 2 3 1 4 1 5 3 5 1 8 0 9 1 3 8 7 8 6 2 1 5 2 1 1 1 1 2 3 2 1 8 0 11 8 6 3 1 1 7 4 4 3 4 1 2 3 1 1 1 2 2 1 1 6 0 8 1 2 3 5 6 8 8 5 3 2 3 1 2 1 3 5 1 3 5 3 6 1 8 0 8 8 1 2 1 9 4 2 4 3 2 2 2 1 2 1 3 1 5 0 6 7 6 5 1 9 6 3 1 1 1 3 1 7 0 6 1 2 5 5 2 1 1 1 1 3 3 1 1 1 1 1 3 2 5 4 1 3 7 5 4 3 3 7 1 8 0 9 6 3 7 7 4 6 6 1 4 2 3 3 1 1 2 3 2 1 8 0 6 1 6 1 3 7 7 1 3 1 2 1 3 1 1 1 6 0 6 3 9 5 1 9 3 3 2 2 2 1 3 1 3 1 1 4 2 2 3 4 1 5 0 6 7 8 4 8 6 1 3 2 1 2 1 1 5 0 6 6 7 8 1 1 6 1 2 1 2 3 1 6 0 7 6 7 6 4 2 3 1 2 1 1 3 2 1 4 3 1 4 3 6 1 7 0 7 5 8 1 7 5 7 1 1 3 1 3 2 2 3 1 7 0 9 1 4 2 9 5 9 6 7 6 1 2 2 1 1 2 1 1 9 0 10 1 7 7 4 5 8 8 5 3 1 1 2 2 1 3 2 2 1 1 2 4 4 2 1 4 3 5 1 9 0 9 3 4 1 9 1 1 1 6 6 3 2 3 3 1 1 1 1 1 1 9 0 6 6 9 4 8 5 1 1 1 3 3 1 3 2 1 2 1 9 0 10 9 1 4 9 5 7 2 3 7 2 3 3 3 1 1 3 1 1 1 1 2 5 5 1 5 2 3 5 5 3 6 1 7 0 6 6 2 2 2 7 1 1 2 2 3 1 3 1 1 7 0 8 1 3 1 7 4 6 6 6 3 1 1 1 2 2 1 1 5 0 7 1 8 1 9 2 5 4 1 1 2 1 1 1 3 4 4 1 1 3 7 1 9 0 9 8 4 5 4 3 7 6 1 4 2 3 3 1 3 2 3 3 2 1 7 0 10 1 6 3 4 1 5 3 7 6 2 1 1 2 3 1 1 3 1 7 0 8 1 5 3 1 4 2 5 4 1 1 3 1 3 1 3 1 2 5 1 1 1 5 3 4 1 9 0 11 1 6 7 1 1 6 5 4 5 4 2 2 3 1 3 2 3 2 3 1 1 5 0 9 1 9 2 4 7 9 3 2 3 1 2 2 2 1 1 6 0 11 1 1 5 3 5 5 1 9 7 2 6 1 2 1 1 1 1 3 1 2 5 3 5 1 7 0 7 1 1 7 9 2 7 2 1 2 3 3 1 3 2 1 9 0 7 3 8 1 7 8 3 4 3 3 3 2 1 2 2 2 2 1 9 0 11 9 9 1 2 8 1 1 6 3 8 4 1 3 1 2 1 3 2 1 3 5 3 3 4 2 3 6 1 8 0 10 1 8 6 7 4 9 4 2 7 8 3 3 3 2 2 3 2 1 1 6 0 11 1 1 6 7 8 7 7 2 3 6 6 1 1 3 1 3 1 1 8 0 10 2 1 9 4 1 3 8 3 2 4 1 1 3 2 3 1 2 3 2 1 2 5 1 3 5 1 5 2 7 4 4 3 5 1 6 0 9 1 2 9 8 2 1 9 3 3 3 1 2 1 2 1 1 7 0 7 2 1 1 2 5 7 5 2 2 3 1 1 3 3 1 7 0 6 6 7 5 2 1 4 1 2 1 1 3 3 2 5 3 1 1 5 3 6 1 9 0 9 2 1 9 8 9 5 5 9 7 2 2 1 2 2 1 1 2 2 1 8 0 10 9 5 4 1 6 6 9 4 7 1 2 3 3 2 2 1 2 1 1 9 0 6 7 5 3 1 4 7 1 2 3 3 1 1 1 2 1 1 4 1 1 1 1 3 7 1 7 0 8 3 4 3 5 8 1 7 7 3 1 3 2 1 1 1 1 8 0 6 8 8 1 4 6 4 1 3 2 3 3 3 1 3 1 9 0 11 6 8 8 2 3 1 7 4 3 1 8 1 2 2 3 1 2 2 2 3 2 1 2 5 4 1 2 3 5 1 5 0 11 2 1 4 5 7 7 5 9 3 1 3 1 3 1 3 1 1 8 0 9 9 5 9 7 5 7 5 6 1 1 2 1 3 1 1 3 2 1 8 0 9 1 5 9 1 6 9 7 5 6 2 2 1 1 3 1 1 3 2 1 3 1 2 3 2 4 3 4 3 6 6 2 5 3 3 6 1 7 0 8 9 8 8 9 1 6 8 7 3 1 1 3 2 3 1 1 6 0 6 8 8 3 6 4 1 1 2 1 2 3 3 1 6 0 10 1 9 6 2 5 9 7 4 3 9 1 1 1 1 1 3 3 4 3 1 3 2 3 7 1 6 0 7 6 2 1 3 4 8 4 3 3 1 2 3 3 1 9 0 6 1 3 9 1 1 9 2 1 1 3 2 2 2 2 2 1 7 0 10 4 4 5 5 9 1 9 9 4 9 2 1 3 1 1 2 3 1 1 3 1 2 5 2 3 4 1 7 0 7 2 5 1 1 9 9 3 1 3 1 3 1 3 1 1 8 0 8 1 4 3 8 7 3 1 1 3 1 1 3 1 1 1 1 1 7 0 8 2 3 4 2 1 5 1 8 1 3 1 3 1 1 2 2 2 5 2 3 6 1 7 0 7 6 7 8 1 6 7 3 2 1 2 1 2 2 3 1 6 0 11 7 8 4 5 3 6 1 2 5 1 3 1 2 1 2 1 1 1 8 0 8 7 8 9 6 3 8 1 1 1 2 3 1 1 3 3 2 3 4 3 2 5 3 3 5 1 8 0 9 1 1 6 8 6 1 9 2 8 1 2 2 2 1 2 2 1 1 6 0 8 4 9 1 9 2 4 2 3 2 3 1 2 1 1 1 6 0 9 3 6 2 1 2 3 4 9 1 2 1 3 3 3 2 4 1 2 2 5 4 6 1 4 3 3 7 1 7 0 11 2 5 1 3 4 6 4 6 7 5 1 1 1 1 3 2 3 1 1 8 0 9 2 1 5 4 8 7 9 8 7 3 3 2 1 3 1 1 1 1 8 0 8 8 1 2 2 1 7 2 5 2 2 3 1 3 1 2 1 2 5 1 3 3 1 3 3 6 1 6 0 9 8 8 9 2 3 1 1 1 1 1 1 3 3 1 2 1 5 0 8 1 7 1 2 5 3 5 1 3 3 1 1 2 1 9 0 10 2 9 1 3 2 3 8 5 7 1 3 1 3 1 1 1 3 2 3 1 5 4 1 1 1 3 4 1 6 0 9 4 9 6 6 1 5 1 1 4 2 1 3 2 1 2 1 5 0 8 7 1 6 8 8 8 4 6 1 1 1 3 1 1 5 0 11 7 4 7 1 4 6 3 1 4 4 2 3 2 1 1 2 1 3 1 3 3 4 1 6 0 6 3 9 1 3 9 7 1 1 1 2 1 2 1 8 0 9 9 5 7 1 7 1 1 9 9 1 3 1 3 1 1 1 2 1 6 0 10 1 2 4 1 2 2 5 6 4 9 1 3 2 1 2 1 2 2 3 5 2 1 3 4 4 3 5 1 6 0 10 1 1 2 9 8 2 3 4 3 1 1 3 2 1 3 3 1 8 0 6 3 5 7 6 1 1 2 1 3 2 3 2 3 2 1 6 0 10 5 6 1 2 4 1 8 6 1 8 1 1 2 1 3 3 5 1 1 2 4 3 4 1 9 0 9 3 8 3 8 1 5 9 8 1 1 2 2 1 1 3 2 3 1 1 8 0 8 9 5 7 1 2 1 4 3 2 3 1 1 1 1 3 3 1 8 0 9 5 2 3 2 7 7 1 5 8 3 3 2 1 3 3 1 1 1 3 1 3 3 4 1 5 0 8 5 1 2 9 6 8 8 9 3 1 1 1 3 1 9 0 10 3 2 3 8 4 9 6 2 1 1 3 3 3 3 2 1 1 2 3 1 6 0 7 6 2 2 5 6 8 1 3 1 3 2 3 3 1 4 5 1 3 5 1 7 0 6 1 3 6 9 1 1 1 2 3 2 2 3 1 1 9 0 10 4 6 1 1 8 1 5 3 6 3 2 2 3 1 2 2 1 1 1 1 9 0 7 6 9 6 9 2 1 1 2 2 1 1 1 1 3 3 1 1 3 3 4 4 1 3 4 4 4 3 3 7 1 8 0 9 6 1 7 3 2 9 8 2 5 3 2 1 1 3 3 3 2 1 8 0 10 8 4 1 8 3 3 8 7 3 7 3 1 3 1 3 1 1 2 1 9 0 11 5 1 2 1 3 4 6 7 2 3 2 1 1 3 3 1 2 3 3 3 3 1 4 3 4 3 2 3 6 1 9 0 11 6 4 4 1 2 9 3 1 4 5 1 2 1 3 1 1 1 3 2 2 1 7 0 11 7 8 7 3 1 3 9 9 7 3 3 2 2 1 2 1 1 3 1 9 0 8 4 1 1 1 3 8 2 2 2 1 1 2 1 2 1 1 1 4 3 4 4 2 1 3 4 1 8 0 7 9 2 2 1 1 6 8 1 1 2 1 1 1 1 2 1 7 0 6 9 3 8 3 1 7 2 1 3 3 2 1 2 1 9 0 11 6 9 2 1 9 8 8 3 8 8 4 2 3 1 3 3 3 1 2 1 3 2 4 2 3 4 1 8 0 9 1 1 8 7 1 7 9 5 9 1 3 2 3 2 1 1 3 1 6 0 8 5 6 5 3 6 9 1 9 1 1 1 2 2 3 1 5 0 9 2 4 5 1 4 7 6 1 2 2 3 1 2 2 2 2 2 2 4 1 3 5 5 3 5 1 5 0 10 5 1 5 5 8 9 3 1 2 8 1 2 1 2 1 1 5 0 10 3 1 8 6 3 3 5 3 7 7 2 1 3 1 2 1 8 0 6 2 1 1 8 4 6 3 2 1 3 2 3 1 1 2 1 4 5 2 3 6 1 6 0 11 8 5 3 3 1 8 2 4 8 9 1 3 1 1 2 1 1 1 7 0 8 1 7 5 1 5 6 9 8 3 1 2 1 3 1 3 1 5 0 11 6 9 8 3 1 8 8 9 1 4 1 1 1 2 1 1 4 2 2 1 1 1 3 4 1 7 0 8 3 3 1 7 7 4 5 7 2 1 1 2 1 1 2 1 8 0 9 4 9 1 9 5 1 1 3 8 1 2 3 1 2 3 3 2 1 9 0 6 9 2 2 1 9 7 1 2 3 1 1 3 1 1 3 2 2 1 1 3 6 1 9 0 10 3 6 8 3 8 1 4 5 6 4 3 1 3 2 3 2 3 2 2 1 9 0 11 2 1 9 1 6 1 2 6 7 8 4 3 1 1 1 1 2 1 3 1 1 6 0 11 1 4 6 3 7 9 5 1 1 2 9 3 2 3 1 2 1 1 3 2 3 5 4 3 5 1 7 0 9 1 8 8 3 7 2 8 8 1 1 1 2 1 3 1 1 1 8 0 7 9 1 9 8 4 4 8 2 3 1 3 3 2 3 1 1 8 0 10 5 8 2 4 1 9 5 3 7 3 2 2 2 1 1 2 2 1 2 3 1 2 4 1 4 6 3 5 5 3 3 5 1 9 0 6 9 3 5 4 1 6 2 1 1 3 1 3 1 2 1 1 7 0 9 9 7 8 1 4 2 3 6 4 2 1 2 2 1 1 1 1 5 0 9 1 1 7 2 1 1 2 5 2 1 2 1 3 1 2 3 3 4 1 3 7 1 8 0 10 5 6 6 7 6 7 1 2 9 3 1 1 1 1 2 2 2 2 1 6 0 10 7 7 1 6 7 3 1 9 8 8 1 2 1 2 3 3 1 8 0 8 5 1 9 7 4 2 5 4 1 1 3 1 2 1 2 2 4 1 1 1 5 4 3 3 7 1 6 0 11 6 9 1 3 6 6 5 5 1 5 9 1 2 2 2 2 2 1 6 0 8 1 3 7 1 4 8 8 9 1 1 1 1 2 2 1 7 0 11 4 5 8 4 7 5 8 5 4 1 1 2 1 3 3 1 3 1 1 5 2 4 1 5 3 3 5 1 5 0 8 4 4 8 3 4 5 1 7 1 3 3 1 1 1 6 0 10 6 3 9 6 8 5 5 8 9 1 2 3 1 2 3 3 1 7 0 11 3 1 4 6 1 3 1 6 4 4 6 1 2 1 3 3 2 2 4 5 1 2 1 3 6 1 5 0 11 8 1 6 7 6 1 1 6 1 1 1 2 1 2 1 1 1 8 0 10 8 1 5 8 8 5 2 5 9 2 1 2 2 1 2 1 2 3 1 8 0 9 1 6 5 2 4 3 5 9 4 1 2 2 2 1 1 2 1 1 2 3 3 4 5 4 1 3 6 1 1 4 1 7 10 4 10 6 3 7 2"

  // Ugly iterating solution
  def part1(s: String) = {
    val a = s.split(" ").map(_.toInt)
    def f(o: Int): (Int, Int) = {
      if (a.size < o) (o, 0) else {
        var i = o + 2
        var s = 0
        (0 until a(o)).foreach { c => val r = f(i); i = r._1; s = s + r._2 }
        (0 until a(o + 1)).foreach { m => s = s + a(i); i = i + 1 }
        (i, s)
      }
    }
    f(0)._2
  }

  "part1" should "satisfy the examples given" in {
    assertResult(138)(part1("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"))
  }

  "part1" should "succeed" in { info(part1(input).toString) }

  // Ugly iterating solution
  def part2(s: String) = {
    val a = s.split(" ").map(_.toInt)
    def f(o: Int): (Int, Int) = {
      if (a.size < o) (o, 0) else {
        var i = o + 2
        if (a(o) == 0) (i + a(o + 1), a.slice(i, i + a(o + 1)).sum) else {
          val rs = (0 until a(o)).map { c => val r = f(i); i = r._1; ((c + 1) -> r._2) }.toMap
          (i + a(o + 1), (0 until a(o + 1)).map { m => rs.getOrElse(a(i + m), 0) }.sum)
        }
      }
    }
    f(0)._2
  }

  "part2" should "satisfy the examples given" in {
    assertResult(66)(part2("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"))
  }

  "part2" should "succeed" in { info(part2(input).toString) }
}
