package com.example.proyecto

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsOff
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

        // Prueba de navegación entre pantallas
        composeTestRule.onNodeWithText("Analisis").performClick()
        composeTestRule.onNodeWithText("Prueba Analisis").assertIsDisplayed()
        composeTestRule.onNodeWithText("Metas").performClick()
        composeTestRule.onNodeWithText("Prueba Metas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Flujo").performClick()
        composeTestRule.onNodeWithText("Prueba Transacciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tips").performClick()
        composeTestRule.onNodeWithText("Prueba Tips").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inicio").performClick()
        composeTestRule.onNodeWithText("Metas:").assertIsDisplayed()

        //Busca y selecciona la etiqueta "Configuración"
        composeTestRule.onNodeWithContentDescription("Configuración").performClick()

        // Busqueda de switch
        val switchNode = composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))

        //Click de switch y verificacion
        try {
            if (switchNode.equals(switchNode.assertIsOn())) {
                switchNode.performClick()
                switchNode.assertIsOff()
            }
        } catch (e: AssertionError) {
            switchNode.performClick()
            switchNode.assertIsOn()
        }

        //Volver y verificar regreso
        composeTestRule.onNodeWithContentDescription("Atrás").performClick()
        composeTestRule.onNodeWithText("Metas:").assertIsDisplayed()
    }
}


