package com.sciome.charts.javafx;

import java.util.List;

import com.sciome.charts.SciomeChartListener;
import com.sciome.charts.SciomeHeatMap;
import com.sciome.charts.data.ChartConfiguration;
import com.sciome.charts.data.ChartDataPack;

import javafx.scene.chart.Chart;

public class SciomeHeatMapFX extends SciomeHeatMap
{

	public SciomeHeatMapFX(String title, List<ChartDataPack> chartDataPacks,
			SciomeChartListener chartListener)
	{
		super(title, chartDataPacks, chartListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Chart generateChart(String[] keys, ChartConfiguration chartConfig)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isXAxisDefineable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isYAxisDefineable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void redrawChart()
	{
		// TODO Auto-generated method stub

	}

}
