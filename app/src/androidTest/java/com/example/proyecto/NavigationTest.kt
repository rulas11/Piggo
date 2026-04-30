package com.example.proyecto

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test


class NavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigationToConfigAndThemeToggle() {
        // 1. Busca y selecciona la etiqueta "Configuración"
        composeTestRule.onNodeWithContentDescription("Configuración").performClick()

        // 2. Buscar el nodo que se comporta como un Switch
        val switchNode = composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))

        // 3. Clic en el Switch
        switchNode.performClick()

        // 4. Verificar el Switch encendido
        switchNode.assertIsOn()

        // 5. Volver
        composeTestRule.onNodeWithContentDescription("Atrás").performClick()

        // 6. Verificar regreso
        composeTestRule.onNodeWithText("Metas:").assertIsDisplayed()
    }
}

