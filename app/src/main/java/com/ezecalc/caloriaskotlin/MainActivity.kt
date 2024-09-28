package com.ezecalc.caloriaskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ezecalc.caloriaskotlin.ui.theme.CaloriasKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloriasKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowCalories(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
val alimentos = mapOf(
    "Pizza" to 800,
    "Ensalada" to 200,
    "Hamburguesa" to 700,
    "Manzana" to 100,
    "Helado" to 300,
    "Pasta" to 600,
    "Yogurt" to 150
)

@Preview(showBackground = true)
@Composable
fun ShowCalories(modifier: Modifier = Modifier) {
    var lista_alimentos = remember { mutableStateOf(alimentos) }
    val calorias_text = remember { mutableStateOf(calculateCalories(alimentos, "todos los productos")) }

    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Text(text = "Lista de Ailmentos", modifier = modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 15.dp),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = "Por Ezequiel Calcanat", modifier = modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 1.dp),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF14369A)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            Button(
                onClick = {
                    lista_alimentos.value = filterByHighCalories()
                    calorias_text.value = calculateCalories(lista_alimentos.value, "productos altos en calorías")
                }
            ) {
                Text(text = "Alto en Calorías", color = Color.White)
            }
            Button(
                onClick = {
                    lista_alimentos.value = filterByLowCalories()
                    calorias_text.value = calculateCalories(lista_alimentos.value, "productos bajos en calorías")
                }
            ) {
                Text(text = "Bajo en Calorías", color = Color.White)
            }
            Button(
                onClick = {
                    lista_alimentos.value = alimentos
                    calorias_text.value = calculateCalories(alimentos, "todos los productos")
                }
            ) {
                Text(text = "Todos", color = Color.White)
            }
        }
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            lista_alimentos.value.forEach { (alimento, calorias) ->
                Text(text = "$alimento: $calorias calorías")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = calorias_text.value)
        }
    }
}


fun calculateCalories(filteredAlimentos: Map<String, Int>, description: String): String {
    val total_calorias = filteredAlimentos.values.sum()
    return "Total de calorías en $description: $total_calorias calorías."
}

fun filterByHighCalories(): Map<String, Int> {
    return alimentos.filter { it.value > 500 }
}

fun filterByLowCalories(): Map<String, Int> {
    return alimentos.filter { it.value <= 500 }
}