package com.sciome.charts.jfree;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.ZoomHandlerFX;

public class SciomeChartViewer extends ChartViewer {
	public static final double CHART_WIDTH = 500;
	public static final double CHART_HEIGHT = 500;
	
	public SciomeChartViewer(JFreeChart chart) {
		super(chart);
		//Change the background color of the chart to be the same grey color as the program
		Color grey = new Color(244, 244, 244);
		chart.setBackgroundPaint(grey);
		chart.getPlot().setBackgroundPaint(grey);
		chart.getLegend().setBackgroundPaint(grey);
		ChartCanvas canvas = getCanvas();
		
		//Remove the preivous zoom handler and add one that activates only when you hold shift
		ZoomHandlerFX zoom = new ZoomHandlerFX("new", this, false, false, false, true);
		canvas.removeMouseHandler(canvas.getMouseHandler("zoom"));
		canvas.addMouseHandler(zoom);
		canvas.setDomainZoomable(false);
		canvas.setRangeZoomable(false);
		
		//Set height and width of chart
		canvas.setHeight(CHART_HEIGHT);
		canvas.setWidth(CHART_WIDTH);
	}
}
