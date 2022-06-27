package cn.soldat.dragdroplist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.soldat.dragdroplist.ui.theme.DragDropListTheme
import cn.soldat.orderable.OrderableColumnList
import cn.soldat.orderable.move

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            DragDropListTheme {
                // A surface container using the 'background' color from the theme
                val list = remember {
                    mutableStateListOf(
                        "Item 1",
                        "Item 2",
                        "Item 3",
                        "Item 4",
                        "Item 5",
                        "Item 6",
                        "Item 7",
                        "Item 8",
                        "Item 9",
                        "Item 10",
                        "Item 11",
                        "Item 12",
                        "Item 13",
                        "Item 14",
                    )
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // TopBar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .background(MaterialTheme.colors.primary),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Drag & Drop List",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    // 可拖动列表
                    OrderableColumnList(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        items = list, onMove = { fromIndex, toIndex ->
                            list.move(fromIndex, toIndex)
                        }) { _, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    Color.Green.copy(0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    Toast
                                        .makeText(context, item, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                .padding(20.dp)
                        ) {
                            Text(text = item, fontSize = 16.sp, fontFamily = FontFamily.Serif)
                        }
                    }
                }

            }
        }
    }
}
