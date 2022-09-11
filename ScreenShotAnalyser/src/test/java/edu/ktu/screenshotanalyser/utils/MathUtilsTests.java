package edu.ktu.screenshotanalyser.utils;

import edu.ktu.screenshotanalyser.tools.MathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTests {

    @Test
    void clamp_BetweenMinMax_Value() {
        // Arrange
        var value = 3.5f;
        var min = 1f;
        var max = 5f;

        // Act
        var result = MathUtils.clamp(value, min, max);

        // Assert
        assertEquals(value, result);
    }

    @Test
    void clamp_BelowMin_Min() {
        // Arrange
        var value = 0.5f;
        var min = 1f;
        var max = 5f;

        // Act
        var result = MathUtils.clamp(value, min, max);

        // Assert
        assertEquals(min, result);
    }

    @Test
    void clamp_OverMax_Max() {
        // Arrange
        var value = 7.5f;
        var min = 1f;
        var max = 5f;

        // Act
        var result = MathUtils.clamp(value, min, max);

        // Assert
        assertEquals(max, result);
    }

    @Test
    void equals_WithEpsilon_EqualValues_True() {
        // Arrange
        var value1 = 2.5f;
        var value2 = 2.5f;
        var epsilon = 0.0001f;

        // Act
        var result = MathUtils.equals(value1, value2, epsilon);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_WithEpsilon_UnequalValues_False() {
        // Arrange
        var value1 = 2.5f;
        var value2 = 1.5f;
        var epsilon = 0.0001f;

        // Act
        var result = MathUtils.equals(value1, value2, epsilon);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_WithEpsilon_AlmostEqualValues_True() {
        // Arrange
        var value1 = 2.4999999f;
        var value2 = 2.5000001f;
        var epsilon = 0.0001f;

        // Act
        var result = MathUtils.equals(value1, value2, epsilon);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_WithoutEpsilon_EqualValues_True() {
        // Arrange
        var value1 = 2.5f;
        var value2 = 2.5f;

        // Act
        var result = MathUtils.equals(value1, value2);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_WithoutEpsilon_UnequalValues_False() {
        // Arrange
        var value1 = 2.5f;
        var value2 = 1.5f;

        // Act
        var result = MathUtils.equals(value1, value2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_WithoutEpsilon_AlmostEqualValues_True() {
        // Arrange
        var value1 = 2.4999999f;
        var value2 = 2.5000001f;

        // Act
        var result = MathUtils.equals(value1, value2);

        // Assert
        assertTrue(result);
    }
}
