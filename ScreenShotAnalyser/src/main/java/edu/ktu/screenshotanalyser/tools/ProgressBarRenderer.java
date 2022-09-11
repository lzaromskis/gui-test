package edu.ktu.screenshotanalyser.tools;

public class ProgressBarRenderer implements IProgressBarRenderer {
    private final int _barLength;
    private final String _leftBorder;
    private final String _rightBorder;
    private final String _fullCell;
    private final String[] _partialCells;
    private final String _emptyCell;

    private int _tick;
    private int _previousActiveCellCount;

    public ProgressBarRenderer(
            int barLength,
            String leftBorder,
            String rightBorder,
            String fullCell,
            String[] partialCells,
            String emptyCell
    ) {
        _barLength = barLength;
        _leftBorder = leftBorder;
        _rightBorder = rightBorder;
        _fullCell = fullCell;
        _partialCells = partialCells;
        _emptyCell = emptyCell;

        _tick = 0;
        _previousActiveCellCount = 0;
    }

    @Override
    public String render(float progress) {
        var clampedProgress = MathUtils.clamp(progress, 0f, 1f);
        var activeCellCount = Math.max((int)Math.ceil(clampedProgress * _barLength), 1);

        // Reset tick on active cell count change
        if (activeCellCount > _previousActiveCellCount) {
            _previousActiveCellCount = activeCellCount;
            _tick = 0;
        }

        var builder = new StringBuilder();
        builder.append(_leftBorder);
        for (int i = 0; i < activeCellCount - 1; i++) {
            builder.append(_fullCell);
        }

        if (MathUtils.equals(clampedProgress, 1f)) {
            builder.append(_fullCell);
        } else {
            builder.append(_partialCells[_tick++ % _partialCells.length]);
        }

        for (int i = 0; i < _barLength - activeCellCount; i++) {
                builder.append(_emptyCell);
        }

        builder.append(_rightBorder);
        return builder.toString();
    }
}
