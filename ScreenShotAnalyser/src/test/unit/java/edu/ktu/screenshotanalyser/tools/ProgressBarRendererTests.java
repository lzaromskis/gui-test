package edu.ktu.screenshotanalyser.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ProgressBarRendererTests {

    @ParameterizedTest
    @MethodSource("progressAndExpectedBarProvider")
    public void render_ProgressBarReflectsProgress(float progress, String expectedBar)
    {
        //Arrange
        var renderer = new ProgressBarRenderer(10, "[", "]", "=", new String[] {"-", "\\", "|", "/"}, " ");

        //Act
        var result = renderer.render(progress);

        // Assert
        assertEquals(expectedBar, result);
    }

    private static Stream<Arguments> progressAndExpectedBarProvider() {
        return Stream.of(
            arguments(-1f,   "[-         ]"),
            arguments(0f,    "[-         ]"),
            arguments(0.1f,  "[-         ]"),
            arguments(0.11f, "[=-        ]"),
            arguments(0.45f, "[====-     ]"),
            arguments(1f,    "[==========]"),
            arguments(2f,    "[==========]")
        );
    }

    @Test
    public void render_RepeatingCallsChangePartialCell()
    {
        //Arrange
        var renderer = new ProgressBarRenderer(10, "[", "]", "=", new String[] {"-", "\\", "|", "/"}, " ");

        //Act
        var result1 = renderer.render(0f);
        var result2 = renderer.render(0f);
        var result3 = renderer.render(0.02f);
        var result4 = renderer.render(0.02f);
        var result5 = renderer.render(0.04f);

        // Assert
        assertEquals("[-         ]", result1);
        assertEquals("[\\         ]", result2);
        assertEquals("[|         ]", result3);
        assertEquals("[/         ]", result4);
        assertEquals("[-         ]", result5);
    }

    @Test
    public void render_AddedFullCellResetsPartialCell()
    {
        //Arrange
        var renderer = new ProgressBarRenderer(10, "[", "]", "=", new String[] {"-", "\\", "|", "/"}, " ");

        //Act
        var result1 = renderer.render(0f);
        var result2 = renderer.render(0.05f);
        var result3 = renderer.render(0.14f);
        var result4 = renderer.render(0.18f);
        var result5 = renderer.render(0.24f);

        // Assert
        assertEquals("[-         ]", result1);
        assertEquals("[\\         ]", result2);
        assertEquals("[=-        ]", result3);
        assertEquals("[=\\        ]", result4);
        assertEquals("[==-       ]", result5);
    }
}
