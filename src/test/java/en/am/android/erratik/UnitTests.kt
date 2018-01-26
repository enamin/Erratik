package en.am.android.erratik

import org.junit.Assert.assertEquals
import org.junit.Test


class UnitTests {
    val ek = Erratik()
    val seed1 = List(100) { it }

    fun <T> log(i: Iterable<T>, tag: String = "Iterable") {
        System.out.println("$tag : ${i.toList()}")
    }

    @Test
    fun choose() {
        assertEquals(0, ek.choose(0, seed1).count())
        assertEquals(0, ek.choose(-1, seed1).count())
        (0..50).forEach {
            val l = ek.choose(9, seed1)
            assertEquals(9, l.count()) // test count
            assertEquals(l.toSet().size, l.count()) // test uniqueness
        }
    }

    @Test
    fun choose_varargs() {
        assertEquals(0, ek.choose(0, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).count())
        assertEquals(0, ek.choose(-1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).count())
        (0..50).forEach {
            val l = ek.choose(5, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
            assertEquals(5, l.count()) // test count
            assertEquals(l.toSet().size, l.count()) // test uniqueness
        }
    }

    @Test
    fun group() {
        assertEquals(0, ek.group(0, 10, seed1).count())
        assertEquals(0, ek.group(-1, 10, seed1).count())
        assertEquals(0, ek.group(10, 0, seed1).count())
        assertEquals(0, ek.group(2, -4, seed1).count())

        val l = ek.group(3, 10, seed1)
        assertEquals(3, l.count())

        l.forEach {
            assertEquals(10, it.count())
        }

        val l2 = ek.group(12, 11, seed1)
        assertEquals(12, l2.count())
        assertEquals(2, l2.filter { it.count() == 0 }.count()) //there must be 2 empty groups
        assertEquals(1, l2.filter { it.count() == 1 }.count()) //there must be 1 group with only one item

        (0..50).forEach {
            val l3 = ek.group(10, 10, seed1)
            assertEquals(seed1.size, l3.flatten().toSet().size) //check for uniqueness
        }
    }

    @Test
    fun groupUnfair() {
        assertEquals(0, ek.groupUnfair(0, 10, seed1).count())
        assertEquals(0, ek.groupUnfair(-1, 10, seed1).count())
        assertEquals(0, ek.groupUnfair(10, 0, seed1).count())
        assertEquals(0, ek.groupUnfair(2, -4, seed1).count())

        (0..50).forEach {
            val l = ek.groupUnfair(seed1.size, 10, seed1)
            assertEquals(seed1.size, l.flatten().size) //no item is left out
            assertEquals(l.filter { it.count() > 1 }.sumBy { it.count() - 1 },
                    l.filter { it.count() == 0 }.count()) //for each empty group there must be a group with more than 1 item

            l.forEach {
                assertEquals(true, it.count() <= 10)
            }

            assertEquals(seed1.size, l.flatten().toSet().size) //check for uniqueness

        }
    }
}