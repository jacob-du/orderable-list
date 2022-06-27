package cn.soldat.orderable

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState

/**
 * @Author: Jacob Du
 * @Date: 2022/6/27 09:26
 * @Desc:
 * @Website: https://www.soldat.cn
 */

/**
 * 获取可见Item的位置
 */
fun LazyListState.getVisibleItemInfoFor(absolute: Int): LazyListItemInfo? {
    return this.layoutInfo.visibleItemsInfo.getOrNull(absolute - this.layoutInfo.visibleItemsInfo.first().index)
}

/**
 * Item的在屏幕上的位置
 */
val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

/**
 * 调整列表中元素的位置
 */
fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return
    val element = this.removeAt(from) ?: return
    this.add(to, element)
}
