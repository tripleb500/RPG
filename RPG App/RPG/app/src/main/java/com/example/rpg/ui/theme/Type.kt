package com.example.rpg.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rpg.R

//val klopFontFamily = FontFamily(
//    Font(R.font.klop, FontWeight.Normal)
//)
val dynaPuffFontFamily = FontFamily(
    Font(R.font.dynapuff_regular),
    Font(R.font.dynapuff_medium, FontWeight.Medium),
    Font(R.font.dynapuff_bold, FontWeight.Bold),
    Font(R.font.dynapuff_semibold, FontWeight.SemiBold)
)

// Set of Material typography styles to start with
val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = dynaPuffFontFamily),
        displayMedium = displayMedium.copy(fontFamily = dynaPuffFontFamily),
        displaySmall = displaySmall.copy(fontFamily = dynaPuffFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = dynaPuffFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = dynaPuffFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = dynaPuffFontFamily),
        titleLarge = titleLarge.copy(fontFamily = dynaPuffFontFamily),
        titleMedium = titleMedium.copy(fontFamily = dynaPuffFontFamily),
        titleSmall = titleSmall.copy(fontFamily = dynaPuffFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = dynaPuffFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = dynaPuffFontFamily),
        bodySmall = bodySmall.copy(fontFamily = dynaPuffFontFamily),
        labelLarge = labelLarge.copy(fontFamily = dynaPuffFontFamily),
        labelMedium = labelMedium.copy(fontFamily = dynaPuffFontFamily),
        labelSmall = labelSmall.copy(fontFamily = dynaPuffFontFamily)
    )
}
