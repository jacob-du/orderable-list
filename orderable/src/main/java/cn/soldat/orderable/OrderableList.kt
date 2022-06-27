package cn.soldat.orderable

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @Author: Jacob Du
 * @Date: 2022/6/27 10:43
 * @Desc:
 * @Website: https://www.soldat.cn
 */

@Composable
fun <T> OrderableColumnList(
    modifier: Modifier = Modifier,
    items: List<T>,
    onMove: (Int, Int) -> Unit,
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val orderableListState = rememberOrderableListState(onMove = onMove)

    LazyColumn(modifier = modifier
        .pointerInput(Unit) {
            // 监听长按后的拖动手势
            detectDragGesturesAfterLongPress(onDrag = { change, offset ->
                change.consume()
                orderableListState.onDrag(offset = offset)

                if (overScrollJob?.isActive == true) return@detectDragGesturesAfterLongPress
                orderableListState
                    .checkForOverScroll()
                    .takeIf { it != 0f }
                    ?.let {
                        overScrollJob =
                            scope.launch { orderableListState.lazyListState.scrollBy(it) }
                    } ?: kotlin.run { overScrollJob?.cancel() }
            },
                // 拖动开始
                onDragStart = { offset -> orderableListState.onDragStart(offset) },
                // 拖动结束
                onDragEnd = { orderableListState.onDragInterrupted() },
                // 拖动取消
                onDragCancel = { orderableListState.onDragInterrupted() })
        }
        /*.fillMaxSize()
        .padding(top = 10.dp, start = 10.dp, end = 10.dp)*/,
        state = orderableListState.lazyListState
    ) {
        itemsIndexed(items) { index, item ->
            /**
             * kotlin takeIf & takeUnless
             * take if = if(true) xxx
             * take unless = if(false) xxx
             */
            Box(modifier = Modifier
                .graphicsLayer {
                    translationY = orderableListState.elementDisplacement.takeIf {
                        index == orderableListState.currentIndexOfDraggedItem
                    } ?: 0f
                }
            ) {
                itemContent(index, item)
            }
        }
    }
}