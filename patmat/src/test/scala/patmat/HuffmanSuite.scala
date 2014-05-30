package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {

  trait TestTrees {
    val t1 = Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5)
    val t2 = Fork(Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5), Leaf('d', 4), List('a', 'b', 'd'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("make code three") {
    new TestTrees {
      assert(makeCodeTree(
        makeCodeTree(Leaf('x', 1), Leaf('e', 1)),
        Leaf('t', 2)).weight === 4)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a', 'b', 'd'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }


  test("times(List('a', 'b', 'a')) ... should return List(('a', 2), ('b', 1))") {
    val x = times(string2Chars("aba"))
    assert(x.contains(('a', 2)))
    assert(x.contains(('b', 1)))
    assert(x.size === 2)
  }


  test("times(List('a', 'b', 'a', 'b', 'c', 'd')) ... should return List(('a', 2), ('b', 2), ('c', 1)), ('d', 1)") {
    val x = times(string2Chars("ababcd"))
    assert(x.contains(('a', 2)))
    assert(x.contains(('b', 2)))
    assert(x.contains(('c', 1)))
    assert(x.contains(('d', 1)))
    assert(x.size === 4)
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4)))
  }

  test("until function") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    val excpectedTree: Fork = makeCodeTree(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4))
    assert(until(singleton, combine)(leaflist).head === excpectedTree)
  }

  test("createCodeTree ('c',1)('d',1)('a',2)('b',2)") {
    val textoPrueba = "ababcd"
    val expectedFork = Fork(Leaf('a', 2), Fork(Fork(Leaf('d', 1), Leaf('c', 1), List('d', 'c'), 2), Leaf('b', 2), List('d', 'c', 'b'), 4), List('a', 'd', 'c', 'b'), 6)
    assert(createCodeTree(string2Chars(textoPrueba)) === expectedFork)

  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }
}
