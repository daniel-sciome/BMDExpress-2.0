package com.sciome.bmdexpress2.mvp.model.stat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sciome.bmdexpress2.mvp.model.BMDExpressAnalysisDataSet;
import com.sciome.bmdexpress2.mvp.model.DoseResponseExperiment;
import com.sciome.bmdexpress2.mvp.model.IStatModelProcessable;
import com.sciome.bmdexpress2.mvp.model.info.AnalysisInfo;
import com.sciome.bmdexpress2.mvp.model.probe.ProbeResponse;
import com.sciome.bmdexpress2.mvp.model.refgene.ReferenceGeneAnnotation;
import com.sciome.charts.annotation.ChartableData;
import com.sciome.charts.annotation.ChartableDataLabel;

public class BMDResult extends BMDExpressAnalysisDataSet implements Serializable, IStatModelProcessable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4821688005886618518L;

	private String name;
	private List<ProbeStatResult> probeStatResults;
	private DoseResponseExperiment doseResponseExperiment;
	private AnalysisInfo analysisInfo;

	private transient List<String> columnHeader;

	/* define chartabble key values */
	public static final String BMD = "Best BMD";
	public static final String BMDL = "Best BMDL";
	public static final String BMDU = "Best BMDU";
	public static final String BMD_BMDL_RATIO = "Best BMD/BMDL";
	public static final String BMDU_BMDL_RATIO = "Best BMDU/BMDL";
	public static final String BMDU_BMD_RATIO = "Best BMDU/BMD";
	public static final String FIT_PVALUE = "Best Fit P-Value";
	public static final String FIT_LOG_LIKELIHOOD = "Best Fit Log-Likelihood";

	@Override
	@ChartableDataLabel(key = "Category Results Name")
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@ChartableData(key = "Probe Stat Results")
	public List<ProbeStatResult> getProbeStatResults()
	{
		return probeStatResults;
	}

	public void setProbeStatResults(List<ProbeStatResult> probeStatResults)
	{
		this.probeStatResults = probeStatResults;
	}

	public DoseResponseExperiment getDoseResponseExperiment()
	{
		return doseResponseExperiment;
	}

	public void setDoseResponseExperiment(DoseResponseExperiment doseResponseExperiment)
	{
		this.doseResponseExperiment = doseResponseExperiment;
	}

	/*
	 * fill the column header for table display or file export purposes.
	 */
	private void fillColumnHeader()
	{
		columnHeader = new ArrayList<>();
		if (probeStatResults == null || probeStatResults.size() == 0)
		{
			return;
		}
		ProbeStatResult probStatResult = probeStatResults.get(0);

		columnHeader = probStatResult.generateColumnHeader();
	}

	@Override
	public List<String> getColumnHeader()
	{
		if (columnHeader == null || columnHeader.size() == 0)
			fillTableData();
		return columnHeader;
	}

	@Override
	public AnalysisInfo getAnalysisInfo()
	{
		return analysisInfo;
	}

	public void setAnalysisInfo(AnalysisInfo analysisInfo)
	{
		this.analysisInfo = analysisInfo;
	}

	// This is called in order to generate data for each probe stat result fo viewing
	// data in a table or exporting it.
	private void fillRowData()
	{

		Map<String, ReferenceGeneAnnotation> probeToGeneMap = new HashMap<>();

		if (this.doseResponseExperiment.getReferenceGeneAnnotations() != null)
		{
			for (ReferenceGeneAnnotation refGeneAnnotation : this.doseResponseExperiment
					.getReferenceGeneAnnotations())
			{
				probeToGeneMap.put(refGeneAnnotation.getProbe().getId(), refGeneAnnotation);
			}
		}
		for (ProbeStatResult probeStatResult : probeStatResults)
		{	
			probeStatResult.createRowData(probeToGeneMap);
		}
	}

	@Override
	public String toString()
	{
		return name;
	}

	public void refreshTableData()
	{
		columnHeader = null;
		for (ProbeStatResult probeStatResult : probeStatResults)
		{
			probeStatResult.refreshRowData();
		}
		fillTableData();

	}

	private void fillTableData()
	{
		if (columnHeader == null)
		{
			fillColumnHeader();
			fillRowData();
		}

	}

	@Override
	public DoseResponseExperiment getProcessableDoseResponseExperiment()
	{
		return doseResponseExperiment;
	}

	@Override
	public List<ProbeResponse> getProcessableProbeResponses()
	{

		List<ProbeResponse> probeResponse = new ArrayList<>();
		for (ProbeStatResult probeStatResult : this.probeStatResults)
		{
			probeResponse.add(probeStatResult.getProbeResponse());
		}

		return probeResponse;
	}

	@Override
	public String getParentDataSetName()
	{
		return doseResponseExperiment.getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getAnalysisRows()
	{
		return probeStatResults;
	}

	@Override
	public List<Object> getColumnHeader2()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
