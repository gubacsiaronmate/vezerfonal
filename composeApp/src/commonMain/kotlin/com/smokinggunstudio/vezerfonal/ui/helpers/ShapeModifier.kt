package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class ShapeModifier(
    val topLeft: Int,
    val topRight: Int,
    val bottomLeft: Int,
    val bottomRight: Int,
) {
    /**
     * Constructor for uniform corner rounding on all corners
     */
    constructor(radius: Int) : this(radius, radius, radius, radius)
    
    /**
     * Creates a modified version with only the top corners rounded
     */
    fun topRounded(radius: Int = 12): ShapeModifier =
        copy(topLeft = radius, topRight = radius, bottomLeft = 0, bottomRight = 0)
    
    /**
     * Creates a modified version with only the bottom corners rounded
     */
    fun bottomRounded(radius: Int = 12): ShapeModifier =
        copy(topLeft = 0, topRight = 0, bottomLeft = radius, bottomRight = radius)
    
    /**
     * Creates a modified version with only the left corners rounded
     */
    fun leftRounded(radius: Int = 12): ShapeModifier =
        copy(topLeft = radius, topRight = 0, bottomLeft = radius, bottomRight = 0)
    
    /**
     * Creates a modified version with only the right corners rounded
     */
    fun rightRounded(radius: Int = 12): ShapeModifier =
        copy(topLeft = 0, topRight = radius, bottomLeft = 0, bottomRight = radius)
    
    fun toShape(): Shape = RoundedCornerShape(
        topStart = topLeft.dp,
        topEnd = topRight.dp,
        bottomEnd = bottomRight.dp,
        bottomStart = bottomLeft.dp,
    )
    
    companion object {
        /**
         * Predefined shape with no rounded corners
         */
        val RECTANGLE = ShapeModifier(0)
        
        /**
         * Predefined shape with standard rounded corners (matches AppShapes.medium)
         */
        val ROUNDED = ShapeModifier(12)

        /**
         * Predefined shape with large rounded corners (matches AppShapes.extraLarge)
         */
        val EXTRA_LARGE = ShapeModifier(28)

        /**
         * Predefined pill/full-circle shape
         */
        val FULL = ShapeModifier(50)
        
        /**
         * Predefined shape with only top corners rounded
         */
        val TOP_ROUNDED = ShapeModifier(12, 12, 0, 0)
        
        /**
         * Predefined shape with only bottom corners rounded
         */
        val BOTTOM_ROUNDED = ShapeModifier(0, 0, 12, 12)
        
        /**
         * Predefined shape with only left corners rounded
         */
        val LEFT_ROUNDED = ShapeModifier(12, 12, 0, 0)
        
        /**
         * Predefined shape with only right corners rounded
         */
        val RIGHT_ROUNDED = ShapeModifier(0, 0, 12, 12)
        
        /**
         * Constructor for setting top and bottom corners separately
         * @param top Radius for top corners
         * @param bottom Radius for bottom corners
         */
        fun vertical(top: Int, bottom: Int): ShapeModifier = ShapeModifier(
            topLeft = top,
            topRight = top,
            bottomLeft = bottom,
            bottomRight = bottom
        )
        
        /**
         * Constructor for setting horizontal and vertical corners separately
         * @param left Radius for left corners
         * @param right Radius for right corners
         */
        fun horizontal(left: Int, right: Int): ShapeModifier = ShapeModifier(
            topLeft = left,
            topRight = right,
            bottomLeft = left,
            bottomRight = right
        )
    }
}
