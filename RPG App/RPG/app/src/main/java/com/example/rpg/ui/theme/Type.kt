package com.example.rpg.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.rpg.R

val klopFontFamily = FontFamily(
    Font(R.font.klop, FontWeight.Normal)
)
val dynaPuffFontFamily = FontFamily(
    Font(R.font.dynapuff, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    //this changes default font for text items
    bodyLarge = TextStyle(
        fontFamily = klopFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // This changes default font for button labels
    labelLarge = TextStyle(
        fontFamily = klopFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    //This changes default font for nav bar labels
    labelMedium = TextStyle(
        fontFamily = klopFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    */
)