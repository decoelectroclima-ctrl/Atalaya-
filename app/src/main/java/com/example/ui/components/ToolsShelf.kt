package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

data class ToolItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

@Composable
fun ToolsShelf(
    allTools: List<ToolItem>,
    pinnedIds: Set<String>,
    expanded: Boolean,
    onToggleExpanded: (Boolean) -> Unit,
    onTogglePinned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pinned = allTools.filter { pinnedIds.contains(it.id) }
    val rest = allTools.filter { !pinnedIds.contains(it.id) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Mis Herramientas",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Fijadas por el usuario, siempre visibles
        if (pinned.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height((((pinned.size + 2) / 3) * 84).dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pinned) { tool -> ToolTile(tool, isPinned = true, onTogglePinned = onTogglePinned) }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // El resto, plegado por defecto
        TextButton(onClick = { onToggleExpanded(!expanded) }) {
            Text(if (expanded) "Ver menos" else "Ver más herramientas (${rest.size})")
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height((((rest.size + 2) / 3) * 84).dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rest) { tool -> ToolTile(tool, isPinned = false, onTogglePinned = onTogglePinned) }
            }
        }
    }
}

@Composable
private fun ToolTile(tool: ToolItem, isPinned: Boolean, onTogglePinned: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .clickable { tool.onClick() },
        shape = RoundedCornerShape(14.dp),
        color = SoltarSurface,
        border = BorderStroke(1.dp, SoltarBorder)
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(tool.icon, contentDescription = tool.label, tint = SoltarAmber, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(tool.label, style = MaterialTheme.typography.labelSmall, color = TextPrimary, maxLines = 2, textAlign = TextAlign.Center)
            }
            IconButton(
                onClick = { onTogglePinned(tool.id) },
                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
            ) {
                Icon(
                    if (isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin,
                    contentDescription = if (isPinned) "Quitar de fijados" else "Fijar",
                    tint = if (isPinned) SoltarAmber else TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
