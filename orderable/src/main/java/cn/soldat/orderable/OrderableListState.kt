package cn.soldat.orderable

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.Job

/**
 * @Author: Jacob Du
 * @Date: 2022/6/27 09:31
 * @Desc:
 * @Website: https://www.soldat.cn
 */

@Composable
fun rememberOrderableListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit
): OrderableListState {
    return remember {
        OrderableListState(lazyListState = lazyListState, onMove = onMove)
    }
}

class OrderableListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit
) {
    var draggedDistance by mutableStateOf(0f) // 拖动距离
    var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null) // 初始拖动元素
    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null) // 当前拖动元素的索引

    /**
     * Kotlin 元组 一个变量可以携带多个值 值的类型取决于 <*, *> 尖括号中的类型
     * Pair: 一个变量可以携带两个值
     * Triple: 一个变量可以携带三个值
     */
    private val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }

    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absolute = it)
        }
            ?.let { item ->
                (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
            }

    private val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absolute = it)
        }

    private var overScrollJob by mutableStateOf<Job?>(null)

    /**
     * 开始拖动
     */
    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            offset.y.toInt() in item.offset..(item.offset + item.size)
        }
            /**
             * kotlin also 和 let
             * 1. also 适用于 let函数的任何场景
             * 2. also函数返回的是这个对象， let函数返回的是代码块的最后一行
             * 3. 都使用 it 代替本身， 其他的apply、with、run都是用 this 代替
             */
            ?.also {
                currentIndexOfDraggedItem = it.index
                initiallyDraggedElement = it
            }
    }

    /**
     * 取消拖动
     */
    fun onDragInterrupted() {
        draggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        overScrollJob?.cancel()
    }

    /**
     * 拖动中
     */
    fun onDrag(offset: Offset) {
        draggedDistance += offset.y
        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo.filterNot { item ->
                    item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index
                }
                    .firstOrNull { item ->
                        val delta = startOffset - hovered.offset
                        when {
                            delta > 0 -> (endOffset > item.offsetEnd)
                            else -> (startOffset < item.offset)
                        }
                    }?.also { item ->
                        currentIndexOfDraggedItem?.let { current ->
                            onMove(current, item.index)
                        }
                        currentIndexOfDraggedItem = item.index
                    }
            }
        }
    }

    fun checkForOverScroll(): Float {
        return initiallyDraggedElement?.let {
            val startOffset = it.offset + draggedDistance
            val endOffset = it.offsetEnd + draggedDistance
            return@let when {
                draggedDistance > 0 ->
                    (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff ->
                        diff > 0
                    }
                draggedDistance < 0 ->
                    (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff ->
                        diff < 0
                    }
                else -> null
            }
        } ?: 0f
    }
}
