package en.am.android.erratik

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by aminenami on 1/25/18.
 */

class Erratik : Random() {

//    private var items: Iterable<T> = emptyList()
//
//    fun from(items: Iterable<T>): Erratik<T> = also { this.items = items }
//
//    fun choose(count: Int) = also { this.items = Erratik.tap(count, items) }
//
//    fun choose(count: Int) = also { this.items = Erratik.choose(count, items) }
//
//    fun remove(count: Int) = { this.items = Erratik.remove(count, items) }
//
//    fun group(groupCount: Int, groupSize: Int) = also { items = Erratik.group(groupCount, groupSize, items).asIterable() }
//
//    fun done(): Iterable<T> {
//        return items
//    }


    fun <T> chooseOne(vararg items: T): T {
        return chooseOne(items.asIterable())
    }

    fun <T> chooseOne(items: Iterable<T>): T {
        return items.elementAt(nextInt(items.count()))
    }

    fun <T> tap(count: Int, vararg items: T): Iterable<T> {
        return tap(count, items.asIterable())
    }

    fun <T> tap(count: Int, items: Iterable<T>): Iterable<T> {
        if (count <= 0) return emptyList()
        return (0 until count).map { chooseOne(items) }
    }

    fun <T> choose(count: Int, vararg items: T): Iterable<T> {
        return choose(count, items.asIterable())
    }

    fun <T> choose(count: Int, items: Iterable<T>): Iterable<T> {
        if (count <= 0) return emptyList()
        val list = items.toMutableList()
        val resList = ArrayList<T>()
        var index = 0
        while (index < count) {
            val i = nextInt(list.size)
            resList.add(list.removeAt(i))
            index++
        }

        return resList.toList()
    }

    fun <T> asyncChoose(callback: AsyncCallback<Iterable<T>>, count: Int, items: Iterable<T>) {
        Thread {
            val result = choose(count, items)
            callback.onErraticResult(result)
        }.start()
    }

    fun <T> remove(count: Int, vararg items: T): Iterable<T> {
        return remove(count, items.asIterable())
    }

    fun <T> remove(count: Int, items: Iterable<T>): Iterable<T> {
        if (count <= 0) return emptyList()
        val list = items.toMutableList()
        (0 until count).forEach { list.removeAt(nextInt(list.size)) }
        return list
    }

    fun <T> asyncRemove(callback: AsyncCallback<Iterable<T>>, count: Int, vararg items: T) {
        asyncRemove(callback, count, items.asIterable())
    }

    fun <T> asyncRemove(callback: AsyncCallback<Iterable<T>>, count: Int, items: Iterable<T>) {
        Thread {
            val list = remove(count, items)
            callback.onErraticResult(list)
        }.start()
    }

    fun <T> group(groupCount: Int, groupSize: Int, vararg items: T): Iterable<Iterable<T>> {
        return group(groupCount, groupSize, items.asIterable())
    }

    fun <T> group(groupCount: Int, groupSize: Int, items: Iterable<T>): Iterable<Iterable<T>> {
        if (groupCount <= 0 || groupSize <= 0) return emptyList()
        val resList = MutableList(groupCount) { ArrayList<T>() }
        val itemList = items.toMutableList()

        while (itemList.isNotEmpty()) {
            val item = itemList.removeAt(nextInt(itemList.size))
            val list = resList.firstOrNull { it.size < groupSize } ?: break
            list.add(item)
        }

        return resList
    }

    fun <T> asyncGroup(callback: AsyncCallback<Iterable<Iterable<T>>>, groupCount: Int, groupSize: Int, vararg items: T) {
        asyncGroup(callback, groupCount, groupSize, items.asIterable())
    }

    fun <T> asyncGroup(callback: AsyncCallback<Iterable<Iterable<T>>>, groupCount: Int, groupSize: Int, items: Iterable<T>) {
        Thread {
            val result = group(groupCount, groupSize, items)
            callback.onErraticResult(result)
        }.start()
    }

    fun <T> groupUnfair(groupCount: Int, maxGroupSize: Int, vararg items: T): Iterable<Iterable<T>> {
        return groupUnfair(groupCount, maxGroupSize, items.asIterable())
    }

    fun <T> groupUnfair(groupCount: Int, maxGroupSize: Int, items: Iterable<T>): Iterable<Iterable<T>> {
        if (groupCount <= 0 || maxGroupSize <= 0) return emptyList()
        val incompleteLists = MutableList(groupCount) { ArrayList<T>() }
        val resList = ArrayList<List<T>>()
        val itemList = items.toMutableList()

        while (itemList.isNotEmpty() && incompleteLists.isNotEmpty()) {
            val listIndex = nextInt(incompleteLists.size)
            if (incompleteLists[listIndex].size == maxGroupSize) {
                resList.add(incompleteLists.removeAt(listIndex))
                continue
            }

            incompleteLists[listIndex].add(itemList.removeAt(0))
        }

        incompleteLists.forEach { resList.add(it) }

        return resList
    }

//    fun <T> groupUnfairNotEmpty(groupCount: Int, maxGroupSize: Int, minGroupSize: Int, items: Iterable<T>): Iterable<Iterable<T>> {
//
//    }

    fun <T> tapFromEach(countFromEach: Int, vararg items: Iterable<T>): Iterable<Iterable<T>> {
        return tapFromEach(countFromEach, items.asIterable())
    }

    fun <T> tapFromEach(countFromEach: Int, items: Iterable<Iterable<T>>): Iterable<Iterable<T>> {
        if (countFromEach <= 0) return emptyList()
        return List(items.count()) { tap(countFromEach, items.elementAt(it)) }
    }

    fun <T> chooseFromEach(countFromEach: Int, vararg items: Iterable<T>): Iterable<Iterable<T>> {
        return chooseFromEach(countFromEach, items.asIterable())
    }

    fun <T> chooseFromEach(countFromEach: Int, items: Iterable<Iterable<T>>): Iterable<Iterable<T>> {
        if (countFromEach <= 0) return emptyList()
        return List(items.count()) { choose(countFromEach, items.elementAt(it)) }
    }



    interface Stream<T> {
        fun next(): T
    }

    interface AsyncCallback<T> {
        fun onErraticResult(result: T)
    }

}