package com.ayerdi.examen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayerdi.examen.ui.theme.ExamenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



class Operando(
    val id: Int,
    val name: String,
    val operacion: Int
)

fun Suma(a: Int, b: Int): Int{
    return a + b
}

fun Resta(a: Int, b: Int): Int{
    return a - b
}

class OperandoDb(a: Int, b: Int) {
    private val operandos: List<Operando> = listOf(
        Operando(1, "Suma", operacion = Suma(a = a, b = b)),
        Operando(2, "Resta", Resta(a, b))
    )
    fun getAllOperandos(): List<Operando> {
        return operandos
    }

    fun getOperandosById(id: Int): Operando {
        return operandos.first { it.id == id }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    var pantallaActual by remember { mutableStateOf("pantalla1") }
    var operacionSeleccionadaId by remember { mutableStateOf<Int?>(null) }

    when (pantallaActual) {
        "pantalla1" -> {
            Pantalla1(
                onOperandoClick = { operacionId ->
                    operacionSeleccionadaId = operacionId
                    pantallaActual = "pantalla2"
                },
                modifier = modifier
            )
        }
        "pantalla2" -> {
            operacionSeleccionadaId?.let { id ->
                Pantalla2(
                    a = 0,
                    b = 0,
                    id = id,
                    onBack = { pantallaActual = "pantalla1" },
                    modifier = modifier
                )
            }
        }
    }
}


@Composable
fun Pantalla1(
    onOperandoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val db = remember { OperandoDb(0, 0) }
    val operandos = remember { db.getAllOperandos() }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(operandos) { op ->
                    Button(
                        onClick = { onOperandoClick(op.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RectangleShape
                    ) {
                        Text(op.name, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sofia Lopez - 231929")
            Text(text = "Pablo Toledo - 24355")
        }
    }
}

@Composable
fun Pantalla2(
    a: Int,
    b: Int,
    id: Int,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var num1State by remember { mutableStateOf(a.toString()) }
    var num2State by remember { mutableStateOf(b.toString()) }
    var resultado by remember { mutableStateOf<Int?>(null) }

    val num1 = num1State.toIntOrNull() ?: 0
    val num2 = num2State.toIntOrNull() ?: 0

    val db = remember(num1, num2) { OperandoDb(num1, num2) }
    val operando = remember(num1, num2, id) { db.getOperandosById(id) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = operando.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text(text = "Num 1", fontSize = 24.sp)
                TextField(
                    value = num1State,
                    onValueChange = { num1State = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ingresa el primer número") },
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                Text(text = "Num 2", fontSize = 24.sp)
                TextField(
                    value = num2State,
                    onValueChange = { num2State = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ingresa el segundo número") },
                    singleLine = true
                )}
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ){
                Button(
                    onClick = {
                        resultado = operando.operacion
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Operar")
                }

                Spacer(Modifier.height(16.dp))

                if (resultado != null) {
                    Text(
                        text = "$resultado",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Regresar")
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Pantalla1Preview() {
    ExamenTheme {
        Pantalla1(onOperandoClick = {})
    }
}
@Preview(showBackground = true)
@Composable
fun Pantalla2Preview() {
    ExamenTheme {
        Pantalla2(a = 0, b = 0, id = 2)
    }
}