package com.example.macrotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.macrotrack.model.NutritionData
import com.example.macrotrack.ui.theme.MacroTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MacroTrackTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    var showAddDataDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("") }
    var nutritionDataList by remember { mutableStateOf(List(7) { NutritionData() }) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(daysOfWeek.size) { index ->
            val day = daysOfWeek[index]
            DayCard(
                day = day,
                nutritionData = nutritionDataList[index],
                onAddDataClick = {
                    selectedDay = day
                    showAddDataDialog = true
                }
            )
        }
    }

    if (showAddDataDialog) {
        AddDataDialog(
            onDismiss = { showAddDataDialog = false },
            onSave = { calories, protein, sugar, sodium ->
                val dayIndex = daysOfWeek.indexOf(selectedDay)
                nutritionDataList = nutritionDataList.toMutableList().apply {
                    this[dayIndex] = NutritionData(calories, protein, sugar, sodium)
                }
                showAddDataDialog = false
            }
        )
    }
}

@Composable
fun DayCard(day: String, nutritionData: NutritionData, onAddDataClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
//        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = day, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Calories: ${nutritionData.calories}")
                Text("Protein: ${nutritionData.protein}g")
                Text("Sugar: ${nutritionData.sugar}g")
                Text("Sodium: ${nutritionData.sodium}mg")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddDataClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Data")
            }
        }
    }
}

@Composable
fun AddDataDialog(onDismiss: () -> Unit, onSave: (Int, Int, Int, Int) -> Unit) {
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    var sodium by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Nutritional Data") },
        text = {
            Column {
                TextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = sugar,
                    onValueChange = { sugar = it },
                    label = { Text("Sugar (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = sodium,
                    onValueChange = { sodium = it },
                    label = { Text("Sodium (mg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    calories.toIntOrNull() ?: 0,
                    protein.toIntOrNull() ?: 0,
                    sugar.toIntOrNull() ?: 0,
                    sodium.toIntOrNull() ?: 0
                )
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
val foodImages = listOf(R.drawable.img_1, R.drawable.img_2)
