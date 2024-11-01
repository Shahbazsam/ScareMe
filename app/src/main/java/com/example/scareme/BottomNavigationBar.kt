package com.example.scareme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class BottomNavigationItem(
    val selectedIcon: Int,
    val unselectedIcon: Int,
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(
    currentSelectedItemIndex: Int,
    onItemClick: (Int) -> Unit
) {
    val items = listOf(
        BottomNavigationItem(
            selectedIcon = R.drawable.selected_tinder,
            unselectedIcon = R.drawable.unselected_tinder
        ),
        BottomNavigationItem(
            selectedIcon = R.drawable.selected_chat,
            unselectedIcon = R.drawable.unselected_chat
        ),
        BottomNavigationItem(
            selectedIcon = R.drawable.selected_profile,
            unselectedIcon = R.drawable.unselected_profile
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(currentSelectedItemIndex)
    }

    NavigationBar(
        containerColor = Color.Black
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItemIndex == index

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .clickable {
                            Log.d("BottomBar", "Before update: $selectedItemIndex")
                            selectedItemIndex = index
                            Log.d("BottomBar", "After update: $selectedItemIndex")
                            onItemClick(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val icon = if (isSelected) item.selectedIcon else item.unselectedIcon
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(if (isSelected) 64.dp else 40.dp)
                    )
                }

            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarTheme() {
    BottomNavigationBar(onItemClick = {})
}*/
