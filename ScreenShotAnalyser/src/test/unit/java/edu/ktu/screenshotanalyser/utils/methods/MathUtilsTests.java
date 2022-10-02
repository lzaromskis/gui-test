package edu.ktu.screenshotanalyser.utils.methods;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    @ParameterizedTest
    @MethodSource("circularDifferenceArgs")
    void circularDifference_OK(float lhs, float rhs, float scaleMin, float scaleMax, float expected) {
        // Arrange & Act
        var result = MathUtils.circularDifference(lhs, rhs, scaleMin, scaleMax);

        // Assert
        assertEquals(expected, result, 0.00001f);
    }

    public static Stream<Arguments> circularDifferenceArgs() {
        return Stream.of(
            arguments(2f, 4f, 0f, 10f, 2f),
            arguments(2f, 8f, 0f, 10f, 4f),
            arguments(1f, 6f, 0f, 10f, 5f),
            arguments(0f, 10f, 0f, 10f, 0f)
        );
    }

    @Test
    void circularDifference_IllegalArgumentException() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> MathUtils.circularDifference(1f, 2f, 2f, 1f));
    }
}
