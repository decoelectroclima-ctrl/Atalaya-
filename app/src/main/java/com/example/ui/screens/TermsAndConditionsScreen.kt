package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TermsAndConditionsScreen(
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("terms_screen"),
        color = SoltarBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar
            Surface(
                color = SoltarSurface,
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, SoltarBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("terms_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = SoltarAmber
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(22.dp))
                        Text(
                            text = "Términos y Condiciones",
                            style = MaterialTheme.typography.titleMedium,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "TÉRMINOS Y CONDICIONES DE USO DE ADRIANA",
                    style = MaterialTheme.typography.titleSmall,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Última actualización: 14 de junio de 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("1. Titularidad y Objeto", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Titular: Javier Jiménez Fernández (adriana.app.soltar@gmail.com). ADRIANA es una aplicación móvil diseñada como herramienta de autorregulación reflexiva y acompañamiento en procesos de duelo afectivo, separación y ruptura sentimental.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.5.dp, SoltarAmber.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("2. Aviso Médico y Psicológico Importante", style = MaterialTheme.typography.titleSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                        Text(
                            text = "ADRIANA es un asistente de inteligencia artificial y herramienta de apoyo emocional. NO es una persona real, ni un psicólogo colegiado, psiquiatra ni profesional sanitario. No emite diagnósticos médicos ni sustituye la psicoterapia clínica profesional. En caso de crisis severa, ideación autolítica o emergencia, el usuario debe contactar inmediatamente con servicios especializados de emergencia (024 en España, 112 de emergencias o 988 de prevención del suicidio).",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("3. Transparencia de IA (Reglamento UE Art. 50)", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = "De conformidad con la normativa europea sobre Inteligencia Artificial, se advierte expresamente al usuario que las respuestas del coach, los análisis de diario, las simulaciones de encuentro y las recomendaciones son generadas total o parcialmente por algoritmos de inteligencia artificial. El usuario interactúa con un sistema artificial y no con un ser humano.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("4. Conducta y Uso Responsable", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = "El usuario se compromete a hacer un uso lícito y adecuado de la aplicación, absteniéndose de utilizar las herramientas de simulación para hostigar, vulnerar órdenes de alejamiento o alimentar conductas de dependencia patológica nocivas.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
