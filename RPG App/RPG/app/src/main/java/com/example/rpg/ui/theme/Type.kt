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
    Font(R.font.dynapuff, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    //this changes default font for text items
    bodyLarge = TextStyle(
        fontFamily = dynaPuffFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // This changes default font for button labels
    labelLarge = TextStyle(
        fontFamily = dynaPuffFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    //This changes default font for nav bar labels
    labelMedium = TextStyle(
        fontFamily = dynaPuffFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    //This changes CenterAlignedTopAppBar for tabs in quest screens
    titleLarge = TextStyle(
        fontFamily = dynaPuffFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,  // match your current size
        lineHeight = 40.sp,
        letterSpacing = 0.sp
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