package com.ezecalc.caloriaskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ezecalc.caloriaskotlin.ui.theme.CaloriasKotlinTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Mapa de alimentos con calorías
val alimentos = mapOf(
    "Pizza" to 800,
    "Ensalada" to 200,
    "Hamburguesa" to 700,
    "Manzana" to 100,
    "Helado" to 300,
    "Pasta" to 600,
    "Yogurt" to 150
)

// ViewModel para manejar el estado con StateFlow
class CaloriesViewModel : ViewModel() {
    private val _alimentosFlow = MutableStateFlow(alimentos)
    val alimentosFlow: StateFlow<Map<String, Int>> = _alimentosFlow.asStateFlow()

    private val _caloriasTextFlow = MutableStateFlow(calculateCalories(alimentos, "todos los productos"))
    val caloriasTextFlow: StateFlow<String> = _caloriasTextFlow.asStateFlow()

    fun filterHighCalories() {
        _alimentosFlow.value = filterByHighCalories()
        _caloriasTextFlow.value = calculateCalories(_alimentosFlow.value, "productos altos en calorías")
    }

    fun filterLowCalories() {
        _alimentosFlow.value = filterByLowCalories()
        _caloriasTextFlow.value = calculateCalories(_alimentosFlow.value, "productos bajos en calorías")
    }

    fun showAll() {
        _alimentosFlow.value = alimentos
        _caloriasTextFlow.value = calculateCalories(alimentos, "todos los productos")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloriasKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Text(
                            text = "Comparación mutableStateOf vs StateFlow",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Comparación de mutableStateOf
                        ShowCaloriesWithMutableStateOf()

                        Spacer(modifier = Modifier.height(32.dp))

                        // Comparación de StateFlow
                        ShowCaloriesWithStateFlow()
                    }
                }
            }
        }
    }
}

// Composable utilizando mutableStateOf
@Composable
fun ShowCaloriesWithMutableStateOf() {
    var listaAlimentos by remember { mutableStateOf(alimentos) }
    var caloriasText by remember { mutableStateOf(calculateCalories(alimentos, "todos los productos")) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Con mutableStateOf", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                listaAlimentos = filterByHighCalories()
                caloriasText = calculateCalories(listaAlimentos, "productos altos en calorías")
            }) {
                Text(text = "Alto en Calorías")
            }
            Button(onClick = {
                listaAlimentos = filterByLowCalories()
                caloriasText = calculateCalories(listaAlimentos, "productos bajos en calorías")
            }) {
                Text(text = "Bajo en Calorías")
            }
            Button(onClick = {
                listaAlimentos = alimentos
                caloriasText = calculateCalories(alimentos, "todos los productos")
            }) {
                Text(text = "Todos")
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            listaAlimentos.forEach { (alimento, calorias) ->
                Text(text = "$alimento: $calorias calorías")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = caloriasText)
        }
    }
}

// Composable utilizando StateFlow
@Composable
fun ShowCaloriesWithStateFlow(viewModel: CaloriesViewModel = viewModel()) {
    val alimentos by viewModel.alimentosFlow.collectAsState()
    val caloriasText by viewModel.caloriasTextFlow.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Con StateFlow", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { viewModel.filterHighCalories() }) {
                Text(text = "Alto en Calorías")
            }
            Button(onClick = { viewModel.filterLowCalories() }) {
                Text(text = "Bajo en Calorías")
            }
            Button(onClick = { viewModel.showAll() }) {
                Text(text = "Todos")
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            alimentos.forEach { (alimento, calorias) ->
                Text(text = "$alimento: $calorias calorías")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = caloriasText)
        }
    }
}

// Funciones de lógica de negocio
fun calculateCalories(filteredAlimentos: Map<String, Int>, description: String): String {
    val totalCalorias = filteredAlimentos.values.sum()
    return "Total de calorías en $description: $totalCalorias calorías."
}

fun filterByHighCalories(): Map<String, Int> {
    return alimentos.filter { it.value > 500 }
}

fun filterByLowCalories(): Map<String, Int> {
    return alimentos.filter { it.value <= 500 }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    CaloriasKotlinTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(it)) {
                Text(
                    text = "Comparación mutableStateOf vs StateFlow",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ShowCaloriesWithMutableStateOf()

                Spacer(modifier = Modifier.height(32.dp))

                ShowCaloriesWithStateFlow()
            }
        }
    }
}
