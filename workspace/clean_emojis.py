import os

ui_dir = "app/src/main/java/com/example/ui"

replacements = {
    "SupportHubComponent.kt": [
        ("o apoya con un ❤️.", "o apoya con afecto."),
        ('Text("❤️ ${post.likes}"', 'Text("${post.likes}"'),
    ],
    "OnboardingScreen.kt": [
        ('Text("💡 ${rec.primaryBenefit}"', 'Text("${rec.primaryBenefit}"'),
    ],
    "ProfileScreen.kt": [
        ('viewModel.showNotification("📦 Datos exportados y cifrados con éxito")', 'viewModel.showNotification("Datos exportados y cifrados con éxito")'),
        ('viewModel.showNotification("❌ Error al exportar datos")', 'viewModel.showNotification("Error al exportar datos")'),
        ('viewModel.showNotification("❌ Error: ${e.localizedMessage}")', 'viewModel.showNotification("Error: ${e.localizedMessage}")'),
        ('viewModel.showNotification("📥 Datos importados y restaurados con éxito")', 'viewModel.showNotification("Datos importados y restaurados con éxito")'),
        ('viewModel.showNotification("❌ PIN incorrecto o archivo de respaldo inválido")', 'viewModel.showNotification("PIN incorrecto o archivo de respaldo inválido")'),
        ('viewModel.showNotification("❌ Error al importar: ${e.localizedMessage}")', 'viewModel.showNotification("Error al importar: ${e.localizedMessage}")'),
        ('"🌅 Mañana"', '"Mañana"'),
        ('"🌤️ Tarde"', '"Tarde"'),
        ('"🌙 Noche (Recomendado)"', '"Noche (Recomendado)"'),
        ('"🌌 Madrugada"', '"Madrugada"'),
        ('"🌅 Mañana"', '"Mañana"'),
        ('"🌤️ Tarde"', '"Tarde"'),
        ('"🌙 Noche"', '"Noche"'),
        ('Text("🔔 Diario"', 'Text("Diario"'),
        ('Text("🌿 Empatía (3d)"', 'Text("Empatía (3d)"'),
        ('Text("🎉 Hito (7d)"', 'Text("Hito (7d)"'),
        ('text = "💡 Cómo añadirlo:', 'text = "Cómo añadirlo:'),
        ('viewModel.showNotification("🔄 Widget sincronizado con éxito")', 'viewModel.showNotification("Widget sincronizado con éxito")'),
    ],
    "RelationshipAuditDialog.kt": [
        ('✨ Ayúdame a identificar', 'Ayúdame a identificar'),
    ],
    "AiCompanionSheet.kt": [
        ('⚖️ Aviso ético y clínico', 'Aviso ético y clínico'),
    ],
    "PaywallDialog.kt": [
        ('🔒 Separación estricta', 'Separación estricta'),
    ],
    "MandatoryJournalPendingScreen.kt": [
        ('⚠️ Escribe al menos unas palabras para desbloquear la app.', 'Escribe al menos unas palabras para desbloquear la app.'),
    ],
    "EmotionalCheckinDialog.kt": [
        ('"🌟 Muy bien"', '"Muy bien"'),
        ('"🙂 Bien"', '"Bien"'),
        ('"😐 Neutral"', '"Neutral"'),
        ('"🌧️ Mal"', '"Mal"'),
        ('"⛈️ Muy mal"', '"Muy mal"'),
        ('"📈 Mejor"', '"Mejor"'),
        ('"➡️ Igual"', '"Igual"'),
        ('"📉 Peor"', '"Peor"'),
    ],
    "TimeCapsuleDialog.kt": [
        ('text = "📜 CARTA ORIGINAL ESCRITA HACE', 'text = "CARTA ORIGINAL ESCRITA HACE'),
        ('text = if (isReady) "✨ Lista para desbloquear', 'text = if (isReady) "Lista para desbloquear'),
        ('else "🔒 Desbloqueo en', 'else "Desbloqueo en'),
    ],
    "TimeCapsuleComparisonDialog.kt": [
        ('text = "✉️ «', 'text = "«'),
    ],
    "BeginnerLetterDialog.kt": [
        ('text = "✍️ Escribe una carta', 'text = "Escribe una carta'),
    ],
    "TemporalMirrorDialog.kt": [
        ('Text(text = "📝"', 'Text(text = ""'),
    ],
    "EncounterSimulatorDialog.kt": [
        ('text = "🎭 $exName', 'text = "$exName'),
    ],
    "PersonalJournalDialog.kt": [
        ('"🌿 Calma"', '"Calma"'),
        ('"🥀 Nostalgia"', '"Nostalgia"'),
        ('"⚡ Ansiedad"', '"Ansiedad"'),
        ('"💡 Claridad"', '"Claridad"'),
        ('"🌧️ Duelo"', '"Duelo"'),
        ('"✨ Gratitud"', '"Gratitud"'),
        ('"🛡️ Valentía"', '"Valentía"'),
        ('"🌪️ Confusión"', '"Confusión"'),
        ('viewModel.showNotification("📋 Copiado al portapapeles")', 'viewModel.showNotification("Copiado al portapapeles")'),
        ('viewModel.showNotification("⚠️ Permiso de micrófono requerido")', 'viewModel.showNotification("Permiso de micrófono requerido")'),
        ('text = "✨ Espejo Temporal"', 'text = "Espejo Temporal"'),
        ('"🏛️ Estoicismo"', '"Estoicismo"'),
        ('"🧠 Psicología"', '"Psicología"'),
        ('"🕊️ Trascendente"', '"Trascendente"'),
        ('"🏛️ Estoica"', '"Estoica"'),
    ],
    "WisdomAndRitualDialogs.kt": [
        ('text = if (isMilestoneReached) "✨ Hito Alcanzado: Banco Personal de Sabiduría" else "🔒 Banco Personal (Se desbloquea al alcanzar 30 días o 5 check-ins)"', 'text = if (isMilestoneReached) "Hito Alcanzado: Banco Personal de Sabiduría" else "Banco Personal (Se desbloquea al alcanzar 30 días o 5 check-ins)"'),
        ('successMsg = "✨ ¡Frase guardada en tu banco personal!"', 'successMsg = "¡Frase guardada en tu banco personal!"'),
        ('title = { Text(if (isUnlocked) "Ritual de Cierre Personalizado (IA On-Device)" else "🔒 Ritual Bloqueado") }', 'title = { Text(if (isUnlocked) "Ritual de Cierre Personalizado (IA On-Device)" else "Ritual Bloqueado") }'),
    ]
}

for root, dirs, files in os.walk(ui_dir):
    for file in files:
        if file in replacements:
            path = os.path.join(root, file)
            with open(path, "r", encoding="utf-8") as f:
                content = f.read()
            for old, new in replacements[file]:
                content = content.replace(old, new)
            with open(path, "w", encoding="utf-8") as f:
                f.write(content)
            print(f"Updated {file}")
print("Done cleaning UI emojis.")
