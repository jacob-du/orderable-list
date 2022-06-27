# Orderable List
[![](https://jitpack.io/v/jacob-du/orderable-list.svg)](https://jitpack.io/#jacob-du/orderable-list)
> 一个用Jetpack Compose实现的可拖动排序的 List（Orderable List by Jetpack Compose use kotlin）

##### 依赖

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
  
dependencies {
  implementation 'com.github.jacob-du:orderable-list:0.0.1'
} 
```
##### 目前只支持垂直列表, 支持自定义 Item
```kotlin
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
```
