package com.sciome.bmdexpress2.mvp.model.category;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sciome.bmdexpress2.mvp.model.BMDExpressAnalysisRow;
import com.sciome.bmdexpress2.mvp.model.category.identifier.CategoryIdentifier;
import com.sciome.bmdexpress2.mvp.model.category.identifier.GOCategoryIdentifier;
import com.sciome.bmdexpress2.mvp.model.stat.ProbeStatResult;
import com.sciome.bmdexpress2.mvp.model.stat.StatResult;
import com.sciome.bmdexpress2.util.NumberManager;
import com.sciome.charts.annotation.ChartableDataPoint;
import com.sciome.charts.annotation.ChartableDataPointLabel;
import com.sciome.filter.annotation.Filterable;

public abstract class CategoryAnalysisResult extends BMDExpressAnalysisRow implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3651047352571831492L;

	protected CategoryIdentifier categoryIdentifier;
	private List<ReferenceGeneProbeStatResult> referenceGeneProbeStatResults;
	private Integer geneAllCountFromExperiment;
	private Integer geneAllCount;
	private Integer geneCountSignificantANOVA;
	private Double percentage;

	// filter count vars
	private Integer genesWithBMDLessEqualHighDose;
	private Integer genesWithBMDpValueGreaterEqualValue;
	private Integer genesWithBMDBMDLRatioBelowValue;
	private Integer genesWithBMDUBMDLRatioBelowValue;
	private Integer genesWithBMDUBMDRatioBelowValue;
	private Integer genesWithNFoldBelowLowPostiveDoseValue;
	private Integer genesThatPassedAllFilters;

	private Double fishersExactLeftPValue;
	private Double fishersExactRightPValue;
	private Double fishersExactTwoTailPValue;

	private String genesWithConflictingProbeSets = null;

	// bmd/bmdl/bmdu stats
	private Double bmdMean;
	private Double bmdMedian;
	private Double bmdMinimum;
	private Double bmdSD;
	private Double bmdWMean;
	private Double bmdWSD;

	private Double bmdlMean;
	private Double bmdlMedian;
	private Double bmdlMinimum;
	private Double bmdlSD;
	private Double bmdlWMean;
	private Double bmdlWSD;

	private Double bmduMean;
	private Double bmduMedian;
	private Double bmduMinimum;
	private Double bmduSD;
	private Double bmduWMean;
	private Double bmduWSD;

	// percentile fields
	private Double fifthPercentileIndex;
	private Double bmdFifthPercentileTotalGenes;

	private Double tenthPercentileIndex;
	private Double bmdTenthPercentileTotalGenes;

	private Double genesUpBMDMean;
	private Double genesUpBMDMedian;
	private Double genesUpBMDSD;

	private Double genesUpBMDLMean;
	private Double genesUpBMDLMedian;
	private Double genesUpBMDLSD;

	private Double genesUpBMDUMean;
	private Double genesUpBMDUMedian;
	private Double genesUpBMDUSD;

	private Double genesDownBMDMean;
	private Double genesDownBMDMedian;
	private Double genesDownBMDSD;

	private Double genesDownBMDLMean;
	private Double genesDownBMDLMedian;
	private Double genesDownBMDLSD;

	private Double genesDownBMDUMean;
	private Double genesDownBMDUMedian;
	private Double genesDownBMDUSD;

	// statResult counts
	// could be computed from probestatresult
	private Map<StatResult, Integer> statResultCounts;

	// row data for the table view.
	protected transient List<Object> row;

	private transient String genes;
	private transient String geneSymbols;

	// converting the object data to row data will require lots of string buffers.
	// let them all use the same object to reduce instantiation
	private transient StringBuffer stringBuffer = new StringBuffer();

	public List<ReferenceGeneProbeStatResult> getReferenceGeneProbeStatResults()
	{
		return referenceGeneProbeStatResults;
	}

	public void setReferenceGeneProbeStatResults(
			List<ReferenceGeneProbeStatResult> referenceGeneProbeStatResults)
	{
		this.referenceGeneProbeStatResults = referenceGeneProbeStatResults;
	}

	@Filterable(key = CategoryAnalysisResults.CATEGORY_ID)
	public String getCategoryID()
	{
		return categoryIdentifier.getId();
	}

	@Filterable(key = CategoryAnalysisResults.CATEGORY_DESCRIPTION)
	public String getCategoryDescription()
	{
		return categoryIdentifier.getTitle();
	}

	@Filterable(key = CategoryAnalysisResults.GO_TERM_LEVEL)
	public Integer getGotermLevel()
	{
		if (categoryIdentifier instanceof GOCategoryIdentifier)
		{
			GOCategoryIdentifier goCatID = (GOCategoryIdentifier) categoryIdentifier;
			try
			{
				return Integer.valueOf(goCatID.getGoLevel());
			}
			catch (Exception e) // this should always be an integer but in case it happens not to be just
								// return null;
			{

			}
		}

		return null;
	}

	@Filterable(key = "Entrez Gene IDs")
	public String getGenes()
	{
		if (genes == null)
			getGenesIds();
		return genes;
	}

	@Filterable(key = "Gene Symbols")
	public String getGeneSymbols()
	{
		if (geneSymbols == null)
			getGeneSymbolsPrivate();
		return geneSymbols;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.FISHERS_TWO_TAIL_NEG_LOG)
	public Double getNegLogOfFishers2Tail()
	{
		if (fishersExactTwoTailPValue == null)
			return null;
		return NumberManager.negLog10(fishersExactTwoTailPValue);
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.NUM_SIGNFICANT_GENES)
	@Filterable(key = CategoryAnalysisResults.NUM_SIGNFICANT_GENES)
	public Integer getGeneCountSignificantANOVA()
	{
		return geneCountSignificantANOVA;
	}

	public void setGeneCountSignificantANOVA(Integer geneCountSignificantANOVA)
	{
		this.geneCountSignificantANOVA = geneCountSignificantANOVA;
	}

	@Filterable(key = "Genes With BMD BMDL Ratio Below Value")
	public Integer getGenesWithBMDBMDLRatioBelowValue()
	{
		return genesWithBMDBMDLRatioBelowValue;
	}

	public void setGenesWithBMDBMDLRatioBelowValue(Integer genesWithBMDBMDLRatioBelowValue)
	{
		this.genesWithBMDBMDLRatioBelowValue = genesWithBMDBMDLRatioBelowValue;
	}

	public void setGenesWithBMDUBMDLRatioBelowValue(Integer value)
	{
		this.genesWithBMDUBMDLRatioBelowValue = value;
	}

	@Filterable(key = "Genes With BMDU BMDL Ratio Below Value")
	public Integer getGenesWithBMDUBMDLRatioBelowValue()
	{
		return genesWithBMDUBMDLRatioBelowValue;
	}

	public void setGenesWithBMDUBMDRatioBelowValue(Integer value)
	{
		this.genesWithBMDUBMDRatioBelowValue = value;
	}

	@Filterable(key = "Genes With BMDU BMD Ratio Below Value")
	public Integer getGenesWithBMDUBMDRatioBelowValue()
	{
		return genesWithBMDUBMDRatioBelowValue;
	}

	@Filterable(key = "Genes With N-Fold Below low Postive Dose Value")
	public Integer getGenesWithNFoldBelowLowPostiveDoseValue()
	{
		return genesWithNFoldBelowLowPostiveDoseValue;
	}

	public void setGenesWithNFoldBelowLowPostiveDoseValue(Integer genesWithNFoldBelowLowPostiveDoseValue)
	{
		this.genesWithNFoldBelowLowPostiveDoseValue = genesWithNFoldBelowLowPostiveDoseValue;
	}

	@ChartableDataPoint(key = "Percentage")
	@Filterable(key = "Percentage")
	public Double getPercentage()
	{
		return percentage;
	}

	@Filterable(key = "All Gene Count")
	public Integer getGeneAllCount()
	{
		return geneAllCount;
	}

	public void setGeneAllCount(Integer geneAllCount)
	{
		this.geneAllCount = geneAllCount;
	}

	public Integer getGeneAllCountFromExperiment()
	{
		return geneAllCountFromExperiment;
	}

	public void setGeneAllCountFromExperiment(Integer geneAllCountFromExperiment)
	{
		this.geneAllCountFromExperiment = geneAllCountFromExperiment;
	}

	public void setPercentage(Double percentage)
	{
		this.percentage = percentage;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMD_MEAN)
	public Double getBmdMean()
	{
		return bmdMean;
	}

	public void setBmdMean(Double bmdMean)
	{
		this.bmdMean = bmdMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMD_MEDIAN)
	public Double getBmdMedian()
	{
		return bmdMedian;
	}

	public void setBmdMedian(Double bmdMedian)
	{
		this.bmdMedian = bmdMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_MINIUMUM)
	@Filterable(key = CategoryAnalysisResults.BMD_MINIUMUM)
	public Double getBmdMinimum()
	{
		return bmdMinimum;
	}

	public void setBmdMinimum(Double bmdMinimum)
	{
		this.bmdMinimum = bmdMinimum;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_MAXIMUM)
	public Double getBMDMaximum()
	{
		Double currentMaxBMD = null;
		if (referenceGeneProbeStatResults == null)
			return null;
		for (ReferenceGeneProbeStatResult result : referenceGeneProbeStatResults)
		{
			for (ProbeStatResult probeStatResult : result.getProbeStatResults())
			{
				if (currentMaxBMD == null || (probeStatResult.getBestBMD() != null
						&& probeStatResult.getBestBMD() > currentMaxBMD))
				{
					currentMaxBMD = probeStatResult.getBestBMD();
				}
			}
		}
		return currentMaxBMD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDL_MAXIMUM)
	public Double getBMDLMaximum()
	{
		Double currentMaxBMDL = null;
		if (referenceGeneProbeStatResults == null)
			return null;
		for (ReferenceGeneProbeStatResult result : referenceGeneProbeStatResults)
		{
			for (ProbeStatResult probeStatResult : result.getProbeStatResults())
			{
				if (currentMaxBMDL == null || (probeStatResult.getBestBMDL() != null
						&& probeStatResult.getBestBMDL() > currentMaxBMDL))
				{
					currentMaxBMDL = probeStatResult.getBestBMDL();
				}
			}
		}
		return currentMaxBMDL;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_SD)
	@Filterable(key = CategoryAnalysisResults.BMD_SD)
	public Double getBmdSD()
	{
		if (bmdSD != null && bmdSD.isNaN())
			return null;
		return bmdSD;
	}

	public void setBmdSD(Double bmdSD)
	{
		if (bmdSD != null && bmdSD.isNaN())
			this.bmdSD = null;
		else
			this.bmdSD = bmdSD;
	}

	public Double getBmdWMean()
	{
		return bmdWMean;
	}

	public void setBmdWMean(Double bmdWMean)
	{
		this.bmdWMean = bmdWMean;
	}

	public Double getBmdWSD()
	{
		if (bmdWSD != null && bmdWSD.isNaN())
		{
			return null;
		}
		return bmdWSD;
	}

	public void setBmdWSD(Double bmdWSD)
	{
		if (bmdWSD != null && bmdWSD.isNaN())
			this.bmdWSD = null;
		else
			this.bmdWSD = bmdWSD;
	}

	@ChartableDataPointLabel
	public String getChartableDataLabel()
	{
		return this.getCategoryIdentifier().getId();
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDU_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDU_MEAN)
	public Double getBmduMean()
	{
		return bmduMean;
	}

	public void setBmduMean(Double bmduMean)
	{
		this.bmduMean = bmduMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDU_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDU_MEDIAN)
	public Double getBmduMedian()
	{
		return bmduMedian;
	}

	public void setBmduMedian(Double bmduMedian)
	{
		this.bmduMedian = bmduMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDU_MINIUMUM)
	@Filterable(key = CategoryAnalysisResults.BMDU_MINIUMUM)
	public Double getBmduMinimum()
	{
		return bmduMinimum;
	}

	public void setBmduMinimum(Double bmduMinimum)
	{
		this.bmduMinimum = bmduMinimum;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDU_SD)
	@Filterable(key = CategoryAnalysisResults.BMDU_SD)
	public Double getBmduSD()
	{
		if (bmduSD != null && bmduSD.isNaN())
		{
			return null;
		}
		return bmduSD;
	}

	public void setBmduSD(Double bmduSD)
	{
		if (bmduSD != null && bmduSD.isNaN())
			this.bmduSD = null;
		else
			this.bmduSD = bmduSD;
	}

	public Double getBmduWMean()
	{
		return bmduWMean;
	}

	public void setBmduWMean(Double bmduWMean)
	{
		this.bmduWMean = bmduWMean;
	}

	public Double getBmduWSD()
	{
		if (bmduWSD != null && bmduWSD.isNaN())
			return null;
		return bmduWSD;
	}

	public void setBmduWSD(Double bmduWSD)
	{
		if (bmduWSD != null && bmduWSD.isNaN())
			this.bmduWSD = null;
		else
			this.bmduWSD = bmduWSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDL_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDL_MEAN)
	public Double getBmdlMean()
	{
		return bmdlMean;
	}

	public void setBmdlMean(Double bmdlMean)
	{
		this.bmdlMean = bmdlMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDL_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDL_MEDIAN)
	public Double getBmdlMedian()
	{
		return bmdlMedian;
	}

	public void setBmdlMedian(Double bmdlMedian)
	{
		this.bmdlMedian = bmdlMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDL_MINIUMUM)
	@Filterable(key = CategoryAnalysisResults.BMDL_MINIUMUM)
	public Double getBmdlMinimum()
	{
		return bmdlMinimum;
	}

	public void setBmdlMinimum(Double bmdlMinimum)
	{
		this.bmdlMinimum = bmdlMinimum;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDL_SD)
	@Filterable(key = CategoryAnalysisResults.BMDL_SD)
	public Double getBmdlSD()
	{
		if (bmdlSD != null && bmdlSD.isNaN())
		{
			return null;
		}
		return bmdlSD;
	}

	public void setBmdlSD(Double bmdlSD)
	{
		if (bmdlSD != null && bmdlSD.isNaN())
			this.bmdlSD = null;
		else
			this.bmdlSD = bmdlSD;
	}

	public Double getBmdlWMean()
	{
		return bmdlWMean;
	}

	public void setBmdlWMean(Double bmdlWMean)
	{
		this.bmdlWMean = bmdlWMean;
	}

	public Double getBmdlWSD()
	{
		if (bmdlWSD != null && bmdlWSD.isNaN())
			return null;
		return bmdlWSD;
	}

	public void setBmdlWSD(Double bmdlWSD)
	{
		if (bmdlWSD != null && bmdlWSD.isNaN())
			this.bmdlWSD = null;
		else
			this.bmdlWSD = bmdlWSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_FIFTH_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMD_FIFTH_MEAN)
	public Double getFifthPercentileIndex()
	{
		return fifthPercentileIndex;
	}

	public void setFifthPercentileIndex(Double fifthPercentileIndex)
	{
		this.fifthPercentileIndex = fifthPercentileIndex;
	}

	public Double getBmdFifthPercentileTotalGenes()
	{
		return bmdFifthPercentileTotalGenes;
	}

	public void setBmdFifthPercentileTotalGenes(Double bmdFifthPercentileTotalGenes)
	{
		this.bmdFifthPercentileTotalGenes = bmdFifthPercentileTotalGenes;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMD_TENTH_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMD_TENTH_MEAN)
	public Double getTenthPercentileIndex()
	{
		return tenthPercentileIndex;
	}

	public void setTenthPercentileIndex(Double tenthPercentileIndex)
	{
		this.tenthPercentileIndex = tenthPercentileIndex;
	}

	public Double getBmdTenthPercentileTotalGenes()
	{
		return bmdTenthPercentileTotalGenes;
	}

	public void setBmdTenthPercentileTotalGenes(Double bmdTenthPercentileTotalGenes)
	{
		this.bmdTenthPercentileTotalGenes = bmdTenthPercentileTotalGenes;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUP_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDUP_MEAN)
	public Double getGenesUpBMDMean()
	{
		return genesUpBMDMean;
	}

	public void setGenesUpBMDMean(Double genesUpBMDMean)
	{
		this.genesUpBMDMean = genesUpBMDMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUP_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDUP_MEDIAN)
	public Double getGenesUpBMDMedian()
	{
		return genesUpBMDMedian;
	}

	public void setGenesUpBMDMedian(Double genesUpBMDMedian)
	{
		this.genesUpBMDMedian = genesUpBMDMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUP_SD)
	@Filterable(key = CategoryAnalysisResults.BMDUP_SD)
	public Double getGenesUpBMDSD()
	{
		if (genesUpBMDSD != null && genesUpBMDSD.isNaN())
			return null;
		return genesUpBMDSD;
	}

	public void setGenesUpBMDSD(Double genesUpBMDSD)
	{
		this.genesUpBMDSD = genesUpBMDSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDLUP_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDLUP_MEAN)
	public Double getGenesUpBMDLMean()
	{
		return genesUpBMDLMean;
	}

	public void setGenesUpBMDLMean(Double genesUpBMDLMean)
	{
		this.genesUpBMDLMean = genesUpBMDLMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDLUP_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDLUP_MEDIAN)
	public Double getGenesUpBMDLMedian()
	{
		return genesUpBMDLMedian;
	}

	public void setGenesUpBMDLMedian(Double genesUpBMDLMedian)
	{
		this.genesUpBMDLMedian = genesUpBMDLMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDLUP_SD)
	@Filterable(key = CategoryAnalysisResults.BMDLUP_SD)
	public Double getGenesUpBMDLSD()
	{
		if (genesUpBMDLSD != null && genesUpBMDLSD.isNaN())
			return null;
		return genesUpBMDLSD;
	}

	public void setGenesUpBMDLSD(Double genesUpBMDLSD)
	{
		this.genesUpBMDLSD = genesUpBMDLSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUUP_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDUUP_MEAN)
	public Double getGenesUpBMDUMean()
	{
		return genesUpBMDUMean;
	}

	public void setGenesUpBMDUMean(Double genesUpBMDUMean)
	{
		this.genesUpBMDUMean = genesUpBMDUMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUUP_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDUUP_MEDIAN)
	public Double getGenesUpBMDUMedian()
	{
		return genesUpBMDUMedian;
	}

	public void setGenesUpBMDUMedian(Double genesUpBMDUMedian)
	{
		this.genesUpBMDUMedian = genesUpBMDUMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUUP_SD)
	@Filterable(key = CategoryAnalysisResults.BMDUUP_SD)
	public Double getGenesUpBMDUSD()
	{
		if (genesUpBMDUSD != null && genesUpBMDUSD.isNaN())
			return null;
		return genesUpBMDUSD;
	}

	public void setGenesUpBMDUSD(Double genesUpBMDUSD)
	{
		this.genesUpBMDUSD = genesUpBMDUSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDDOWN_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDDOWN_MEAN)
	public Double getGenesDownBMDMean()
	{
		return genesDownBMDMean;
	}

	public void setGenesDownBMDMean(Double genesDownBMDMean)
	{
		this.genesDownBMDMean = genesDownBMDMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDDOWN_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDDOWN_MEDIAN)
	public Double getGenesDownBMDMedian()
	{
		return genesDownBMDMedian;
	}

	public void setGenesDownBMDMedian(Double genesDownBMDMedian)
	{
		this.genesDownBMDMedian = genesDownBMDMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDDOWN_SD)
	@Filterable(key = CategoryAnalysisResults.BMDDOWN_SD)
	public Double getGenesDownBMDSD()
	{
		if (genesDownBMDSD != null && genesDownBMDSD.isNaN())
			return null;
		return genesDownBMDSD;
	}

	public void setGenesDownBMDSD(Double genesDownBMDSD)
	{
		this.genesDownBMDSD = genesDownBMDSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDLDOWN_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDLDOWN_MEAN)
	public Double getGenesDownBMDLMean()
	{
		return genesDownBMDLMean;
	}

	public void setGenesDownBMDLMean(Double genesDownBMDLMean)
	{
		this.genesDownBMDLMean = genesDownBMDLMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDLDOWN_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDLDOWN_MEDIAN)
	public Double getGenesDownBMDLMedian()
	{
		return genesDownBMDLMedian;
	}

	public void setGenesDownBMDLMedian(Double genesDownBMDLMedian)
	{
		this.genesDownBMDLMedian = genesDownBMDLMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDLDOWN_SD)
	@Filterable(key = CategoryAnalysisResults.BMDLDOWN_SD)
	public Double getGenesDownBMDLSD()
	{
		if (genesDownBMDLSD != null && genesDownBMDLSD.isNaN())
			return null;
		return genesDownBMDLSD;
	}

	public void setGenesDownBMDLSD(Double genesDownBMDLSD)
	{
		this.genesDownBMDLSD = genesDownBMDLSD;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUDOWN_MEAN)
	@Filterable(key = CategoryAnalysisResults.BMDUDOWN_MEAN)
	public Double getGenesDownBMDUMean()
	{
		return genesDownBMDUMean;
	}

	public void setGenesDownBMDUMean(Double genesDownBMDUMean)
	{
		this.genesDownBMDUMean = genesDownBMDUMean;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUDOWN_MEDIAN)
	@Filterable(key = CategoryAnalysisResults.BMDUDOWN_MEDIAN)
	public Double getGenesDownBMDUMedian()
	{
		return genesDownBMDUMedian;
	}

	public void setGenesDownBMDUMedian(Double genesDownBMDUMedian)
	{
		this.genesDownBMDUMedian = genesDownBMDUMedian;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.BMDUDOWN_SD)
	@Filterable(key = CategoryAnalysisResults.BMDUDOWN_SD)
	public Double getGenesDownBMDUSD()
	{
		if (genesDownBMDUSD != null && genesDownBMDUSD.isNaN())
			return null;
		return genesDownBMDUSD;
	}

	public void setGenesDownBMDUSD(Double genesDownBMDUSD)
	{
		this.genesDownBMDUSD = genesDownBMDUSD;
	}

	public Map<StatResult, Integer> getStatResultCounts()
	{
		return statResultCounts;
	}

	public void setStatResultCounts(Map<StatResult, Integer> statResultCounts)
	{
		this.statResultCounts = statResultCounts;
	}

	public String getGenesWithConflictingProbeSets()
	{
		return genesWithConflictingProbeSets;
	}

	public void setGenesWithConflictingProbeSets(String genesWithConflictingProbeSets)
	{
		this.genesWithConflictingProbeSets = genesWithConflictingProbeSets;
	}

	@Filterable(key = "Genes With BMD <= Highest Dose")
	public Integer getGenesWithBMDLessEqualHighDose()
	{
		return genesWithBMDLessEqualHighDose;
	}

	public void setGenesWithBMDLessEqualHighDose(Integer genesWithBMDLessEqualHighDose)
	{
		this.genesWithBMDLessEqualHighDose = genesWithBMDLessEqualHighDose;
	}

	@Filterable(key = "Genes With BMD pValue >= N")
	public Integer getGenesWithBMDpValueGreaterEqualValue()
	{
		return genesWithBMDpValueGreaterEqualValue;
	}

	public void setGenesWithBMDpValueGreaterEqualValue(Integer genesWithBMDpValueGreaterEqualValue)
	{
		this.genesWithBMDpValueGreaterEqualValue = genesWithBMDpValueGreaterEqualValue;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.FISHERS_LEFT)
	@Filterable(key = CategoryAnalysisResults.FISHERS_LEFT)
	public Double getFishersExactLeftPValue()
	{
		return fishersExactLeftPValue;
	}

	public void setFishersExactLeftPValue(Double fishersExactLeftPValue)
	{
		this.fishersExactLeftPValue = fishersExactLeftPValue;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.FISHERS_RIGHT)
	@Filterable(key = CategoryAnalysisResults.FISHERS_RIGHT)
	public Double getFishersExactRightPValue()
	{
		return fishersExactRightPValue;
	}

	public void setFishersExactRightPValue(Double fishersExactRightPValue)
	{
		this.fishersExactRightPValue = fishersExactRightPValue;
	}

	@ChartableDataPoint(key = CategoryAnalysisResults.FISHERS_TWO_TAIL)
	@Filterable(key = CategoryAnalysisResults.FISHERS_TWO_TAIL)
	public Double getFishersExactTwoTailPValue()
	{
		return fishersExactTwoTailPValue;
	}

	public void setFishersExactTwoTailPValue(Double fishersExactTwoTailPValue)
	{
		this.fishersExactTwoTailPValue = fishersExactTwoTailPValue;
	}

	// add in the ratios filters
	@Filterable(key = CategoryAnalysisResults.BMDU_BMDL_MEDIAN_RATIO)
	public Double getBMDUdivBMDLMEDIAN()
	{
		if (bmduMedian.equals(0.0))
			return null;
		return this.bmduMedian / this.bmdlMedian;
	}

	@Filterable(key = CategoryAnalysisResults.BMD_BMDL_MEDIAN_RATIO)
	public Double getBMDdivBMDLMEDIAN()
	{
		if (bmdMedian.equals(0.0))
			return null;
		return this.bmdMedian / this.bmdlMedian;
	}

	@Filterable(key = CategoryAnalysisResults.BMDU_BMD_MEDIAN_RATIO)
	public Double getBMDUdivBMDMEDIAN()
	{
		if (bmduMedian.equals(0.0))
			return null;
		return this.bmduMedian / this.bmdMedian;
	}

	@Filterable(key = CategoryAnalysisResults.BMDU_BMDL_MEAN_RATIO)
	public Double getBMDUdivBMDLMEAN()
	{
		if (bmduMean.equals(0.0))
			return null;
		return this.bmduMean / this.bmdlMean;
	}

	@Filterable(key = CategoryAnalysisResults.BMD_BMDL_MEAN_RATIO)
	public Double getBMDdivBMDLMEAN()
	{
		if (bmdMean.equals(0.0))
			return null;
		return this.bmdMean / this.bmdlMean;
	}

	@Filterable(key = CategoryAnalysisResults.BMDU_BMD_MEAN_RATIO)
	public Double getBMDUdivBMDMEAN()
	{
		if (bmduMean.equals(0.0))
			return null;
		return this.bmduMean / this.bmdMean;
	}

	@Override
	public List<Object> getRow()
	{
		if (row == null || row.size() == 0)
			createRowData();
		return row;
	}

	public CategoryIdentifier getCategoryIdentifier()
	{
		return categoryIdentifier;
	}

	public void setCategoryIdentifier(CategoryIdentifier categoryIdentifier)
	{
		this.categoryIdentifier = categoryIdentifier;
	}

	public List<String> generateColumnHeader()
	{
		List<String> headers = new ArrayList<>();

		headers.add("All Genes (Expression Data)");
		headers.add("All Genes (Platform)");
		headers.add("Input Genes");
		if (genesWithBMDLessEqualHighDose != null)
		{
			headers.add("Genes with BMD <= Highest Dose");
		}
		if (genesWithBMDpValueGreaterEqualValue != null)
		{
			headers.add("Genes with BMD p-Value >= ");
		}

		if (genesWithBMDBMDLRatioBelowValue != null)
		{
			headers.add("Genes with BMD/BMDL <= ");
		}

		if (genesWithBMDUBMDRatioBelowValue != null)
		{
			headers.add("Genes with BMDU/BMD <= ");
		}

		if (genesWithBMDUBMDLRatioBelowValue != null)
		{
			headers.add("Genes with BMDU/BMDL <= ");
		}

		if (genesWithNFoldBelowLowPostiveDoseValue != null)
		{
			headers.add("Genes with BMD <= N-Fold Lowest Positive Dose ");
		}
		headers.add("Genes That Passed All Filters");

		headers.add("Fisher's Exact Left P-Value");
		headers.add("Fisher's Exact Right P-Value");
		headers.add("Fisher's Exact Two-Tailed P-Value");

		headers.add("Percentage");
		headers.add("Entrez Gene IDs");
		headers.add("Gene Symbols");
		headers.add("Probe IDs");

		if (genesWithConflictingProbeSets != null)
		{
			headers.add("Genes with Conflicting Probesets");
		}

		headers.add("BMD Mean");
		headers.add("BMD Median");
		headers.add("BMD Minimum");
		headers.add("BMD SD");
		headers.add("BMD wMean");
		headers.add("BMD wSD");

		headers.add("BMDL Mean");
		headers.add("BMDL Median");
		headers.add("BMDL Minimum");
		headers.add("BMDL SD");
		headers.add("BMDL wMean");
		headers.add("BMDL wSD");

		headers.add("BMDU Mean");
		headers.add("BMDU Median");
		headers.add("BMDU Minimum");
		headers.add("BMDU SD");
		headers.add("BMDU wMean");
		headers.add("BMDU wSD");

		headers.add("5th Percentile Index");
		headers.add("BMD at 5th Percentile of Total Genes");
		headers.add("10th Percentile Index");
		headers.add("BMD at 10th Percentile of Total Genes");

		headers.add("BMD List");
		headers.add("BMDL List");
		headers.add("BMDU List");

		headers.add("Probes with Adverse Direction Up");
		headers.add("Probes with Adverse Direction Down");

		headers.add("Genes with Adverse Direction Up");
		headers.add("Genes Up List");
		headers.add("Genes Up Probes List");
		headers.add("Genes Up BMD Mean");
		headers.add("Genes Up BMD Median");
		headers.add("Genes Up SD");
		headers.add("Genes Up BMDL Mean");
		headers.add("Genes Up BMDL Median");
		headers.add("Genes Up BMDL SD");
		headers.add("Genes Up BMDU Mean");
		headers.add("Genes Up BMDU Median");
		headers.add("Genes Up BMDU SD");
		headers.add("BMD list (up)");
		headers.add("BMDL List (up)");
		headers.add("BMDU List (up)");

		headers.add("Genes with Adverse Direction Down");
		headers.add("Genes Down List");
		headers.add("Genes Down Probes List");
		headers.add("Genes Down BMD Mean");
		headers.add("Genes Down BMD Median");
		headers.add("Genes Down SD");
		headers.add("Genes Down BMDL Mean");
		headers.add("Genes Down BMDL Median");
		headers.add("Genes Down BMDL SD");
		headers.add("Genes Down BMDU Mean");
		headers.add("Genes Down BMDU Median");
		headers.add("Genes Down BMDU SD");
		headers.add("BMD list (down)");
		headers.add("BMDL List (down)");
		headers.add("BMDU List (down)");

		headers.add("Genes with Adverse Conflict Count");
		headers.add("Genes Conflict List");
		headers.add("Genes Conflict Probes List");
		headers.add("BMD list (Conflict)");
		headers.add("BMDL list (Conflict)");
		headers.add("BMDU list (Conflict)");
		headers.add("Model Counts");

		return headers;
	}

	protected void createRowData()
	{
		stringBuffer = new StringBuffer();
		if (row != null)
			return;
		row = new ArrayList<>();
		row.add(this.geneAllCountFromExperiment);
		row.add(this.geneAllCount);
		row.add(this.geneCountSignificantANOVA);
		if (genesWithBMDLessEqualHighDose != null)
			row.add(this.genesWithBMDLessEqualHighDose);
		if (genesWithBMDpValueGreaterEqualValue != null)
			row.add(this.genesWithBMDpValueGreaterEqualValue);
		if (genesWithBMDBMDLRatioBelowValue != null)
			row.add(this.genesWithBMDBMDLRatioBelowValue);
		if (genesWithBMDUBMDRatioBelowValue != null)
			row.add(this.genesWithBMDUBMDRatioBelowValue);
		if (genesWithBMDUBMDLRatioBelowValue != null)
			row.add(this.genesWithBMDUBMDLRatioBelowValue);

		if (genesWithNFoldBelowLowPostiveDoseValue != null)
			row.add(this.genesWithNFoldBelowLowPostiveDoseValue);
		row.add(getAllGenesPassedAllFilters());
		row.add(this.fishersExactLeftPValue);
		row.add(this.fishersExactRightPValue);
		row.add(this.fishersExactTwoTailPValue);

		row.add(this.percentage);
		row.add(getGenesIds());
		row.add(getGeneSymbolsPrivate());
		row.add(getProbeIds());

		if (genesWithConflictingProbeSets != null)
			row.add(grabGenesWithConflictingProbeSets());

		row.add(this.bmdMean);
		row.add(this.bmdMedian);
		row.add(this.bmdMinimum);
		row.add(this.getBmdSD());
		row.add(this.bmdWMean);
		row.add(this.getBmdWSD());

		row.add(this.bmdlMean);
		row.add(this.bmdlMedian);
		row.add(this.bmdlMinimum);
		row.add(this.getBmdlSD());
		row.add(this.bmdlWMean);
		row.add(this.getBmdlWSD());

		row.add(this.bmduMean);
		row.add(this.bmduMedian);
		row.add(this.bmduMinimum);
		row.add(this.getBmduSD());
		row.add(this.bmduWMean);
		row.add(this.getBmduWSD());

		row.add(this.fifthPercentileIndex);
		row.add(this.bmdFifthPercentileTotalGenes);
		row.add(this.tenthPercentileIndex);
		row.add(this.bmdTenthPercentileTotalGenes);

		row.add(getBMDList());
		row.add(getBMDLList());
		row.add(getBMDUList());

		row.add(getProbesAdversUpCount());
		row.add(getProbesAdverseDownCount());

		row.add(getGenesAdverseUpCount());
		row.add(getGenesUp());
		row.add(getProbesUp());
		row.add(this.genesUpBMDMean);
		row.add(this.genesUpBMDMedian);
		row.add(this.getGenesUpBMDSD());
		row.add(this.genesUpBMDLMean);
		row.add(this.genesUpBMDLMedian);
		row.add(this.getGenesUpBMDLSD());
		row.add(this.genesUpBMDUMean);
		row.add(this.genesUpBMDUMedian);
		row.add(this.getGenesUpBMDUSD());
		row.add(getBMDUp());
		row.add(getBMDLUp());
		row.add(getBMDUUp());

		row.add(getGenesAdverseDownCount());
		row.add(getGenesDown());
		row.add(getProbesDown());
		row.add((this.genesDownBMDMean));
		row.add((this.genesDownBMDMedian));
		row.add((this.getGenesDownBMDSD()));
		row.add((this.genesDownBMDLMean));
		row.add((this.genesDownBMDLMedian));
		row.add((this.getGenesDownBMDLSD()));
		row.add((this.genesDownBMDUMean));
		row.add((this.genesDownBMDUMedian));
		row.add((this.getGenesDownBMDUSD()));
		row.add(getBMDDown());
		row.add(getBMDLDown());
		row.add(getBMDUDown());

		row.add((getAdverseConflictCount()));
		row.add(getGenesConflictList());
		row.add(getProbesConflictList());
		row.add(getBMDConflictList());
		row.add(getBMDLConflictList());
		row.add(getBMDUConflictList());
		row.add(calculateStatResultCounts());

	}

	public void setAllGenesPassedAllFilters(Integer number)
	{
		genesThatPassedAllFilters = number;
	}

	@Filterable(key = "Genes That Passed All Filters")
	public Integer getAllGenesPassedAllFilters()
	{

		if (genesThatPassedAllFilters != null)
			return genesThatPassedAllFilters;
		// starting with the top field, check to see the first one that
		// is not null to be able to figure out which value
		// represents the number of genes that passed all filters.
		if (genesWithNFoldBelowLowPostiveDoseValue != null)
		{
			return genesWithNFoldBelowLowPostiveDoseValue;
		}
		else if (genesWithBMDUBMDLRatioBelowValue != null)
		{
			return genesWithBMDUBMDLRatioBelowValue;
		}
		else if (genesWithBMDUBMDRatioBelowValue != null)
		{
			return genesWithBMDUBMDRatioBelowValue;
		}
		else if (genesWithBMDBMDLRatioBelowValue != null)
		{
			return genesWithBMDBMDLRatioBelowValue;
		}
		else if (genesWithBMDpValueGreaterEqualValue != null)
		{
			return genesWithBMDpValueGreaterEqualValue;
		}
		else if (genesWithBMDLessEqualHighDose != null)
		{
			return genesWithBMDLessEqualHighDose;
		}

		return geneCountSignificantANOVA;
	}

	private String grabGenesWithConflictingProbeSets()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}

		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getConflictMinCorrelation() == null)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			stringBuffer.append(ref.getReferenceGene().getId());
			stringBuffer.append("(");
			stringBuffer.append(ref.getConflictMinCorrelation());
			stringBuffer.append(")");

		}
		return stringBuffer.toString();
	}

	private String getBMDLConflictList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.CONFLICT)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{

				if (i > 0)
					stringBuffer.append(",");
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
				{
					stringBuffer.append(ref.getProbeStatResults().get(i).getBestStatResult().getBMDL());
				}
				else
					stringBuffer.append("none");

			}

		}
		return stringBuffer.toString();
	}

	private String getBMDUConflictList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.CONFLICT)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{

				if (i > 0)
					stringBuffer.append(",");
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
				{
					stringBuffer.append(ref.getProbeStatResults().get(i).getBestStatResult().getBMDU());
				}
				else
					stringBuffer.append("none");

			}

		}
		return stringBuffer.toString();
	}

	private String getBMDConflictList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{

			if (ref.getAdverseDirection() != AdverseDirectionEnum.CONFLICT)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{

				if (i > 0)
					stringBuffer.append(",");
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
				{
					stringBuffer.append(ref.getProbeStatResults().get(i).getBestStatResult().getBMD());
				}
				else
					stringBuffer.append("none");

			}

		}
		return stringBuffer.toString();
	}

	private String getProbesConflictList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.CONFLICT)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (i > 0)
					stringBuffer.append(",");
				stringBuffer.append(ref.getProbeStatResults().get(i).getProbeResponse().getProbe().getId());

			}

		}
		return stringBuffer.toString();
	}

	private String getGenesConflictList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.CONFLICT)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			stringBuffer.append(ref.getReferenceGene().getId());

		}
		return stringBuffer.toString();
	}

	private Integer getAdverseConflictCount()
	{
		Integer count = 0;
		if (referenceGeneProbeStatResults == null)
		{
			return 0;
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() == AdverseDirectionEnum.CONFLICT)
			{
				count++;
			}

		}
		return count;
	}

	private String getBMDLDown()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.DOWN)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			int size = ref.getProbeStatResults().size();
			double total = 0.0;
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMDL();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getBMDUDown()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.DOWN)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			int size = ref.getProbeStatResults().size();
			double total = 0.0;
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMDU();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getBMDDown()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.DOWN)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMD();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getProbesDown()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.DOWN)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (i > 0)
					stringBuffer.append(",");
				stringBuffer.append(ref.getProbeStatResults().get(i).getProbeResponse().getProbe().getId());

			}

		}
		return stringBuffer.toString();
	}

	private String getGenesDown()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.DOWN)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			stringBuffer.append(ref.getReferenceGene().getId());

		}
		return stringBuffer.toString();
	}

	private Integer getGenesAdverseDownCount()
	{
		Integer count = 0;
		if (referenceGeneProbeStatResults == null)
		{
			return 0;
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() == AdverseDirectionEnum.DOWN)
			{
				count++;
			}

		}
		return count;
	}

	private String getBMDLUp()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.UP)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMDL();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getBMDUUp()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.UP)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMDU();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getBMDUp()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.UP)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMD();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getProbesUp()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.UP)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (i > 0)
					stringBuffer.append(",");
				stringBuffer.append(ref.getProbeStatResults().get(i).getProbeResponse().getProbe().getId());

			}

		}
		return stringBuffer.toString();
	}

	private String getGenesUp()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() != AdverseDirectionEnum.UP)
				continue;
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			stringBuffer.append(ref.getReferenceGene().getId());

		}
		return stringBuffer.toString();
	}

	private Integer getGenesAdverseUpCount()
	{
		Integer count = 0;
		if (referenceGeneProbeStatResults == null)
		{
			return 0;
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() == AdverseDirectionEnum.UP)
			{
				count++;
			}

		}
		return count;
	}

	private Integer getProbesAdverseDownCount()
	{
		Integer count = 0;
		if (referenceGeneProbeStatResults == null)
		{
			return 0;
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() == AdverseDirectionEnum.DOWN)
			{
				count += ref.getProbeStatResults().size();
			}

		}
		return count;
	}

	private Integer getProbesAdversUpCount()
	{
		Integer count = 0;
		if (referenceGeneProbeStatResults == null)
		{
			return 0;
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (ref.getAdverseDirection() == AdverseDirectionEnum.UP)
			{
				count += ref.getProbeStatResults().size();
			}

		}
		return count;
	}

	private String getBMDLList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMDL();
				else
					size--;
			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getBMDUList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMDU();
				else
					size--;
			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getBMDList()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			double total = 0.0;
			int size = ref.getProbeStatResults().size();
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (ref.getProbeStatResults().get(i).getBestStatResult() != null)
					total += ref.getProbeStatResults().get(i).getBestStatResult().getBMD();
				else
					size--;

			}
			if (size > 0)
				stringBuffer.append(total / size);
			else
				stringBuffer.append("NA");

		}
		return stringBuffer.toString();
	}

	private String getProbeIds()
	{
		stringBuffer.setLength(0);
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}
		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			for (int i = 0; i < ref.getProbeStatResults().size(); i++)
			{
				if (i > 0)
					stringBuffer.append(",");

				ProbeStatResult prs = ref.getProbeStatResults().get(i);
				if (prs == null || prs.getProbeResponse() == null
						|| prs.getProbeResponse().getProbe() == null)
					System.out.println();
				else
					stringBuffer
							.append(ref.getProbeStatResults().get(i).getProbeResponse().getProbe().getId());

			}

		}
		return stringBuffer.toString();
	}

	private String getGenesIds()
	{
		stringBuffer.setLength(0);
		if (this.genes != null)
			return this.genes;
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}

		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			if (ref != null && ref.getReferenceGene() != null)
				stringBuffer.append(ref.getReferenceGene().getId());

		}
		genes = stringBuffer.toString();
		return stringBuffer.toString();
	}

	private String getGeneSymbolsPrivate()
	{
		stringBuffer.setLength(0);
		if (this.geneSymbols != null)
			return this.geneSymbols;
		if (referenceGeneProbeStatResults == null)
		{
			return "";
		}

		for (ReferenceGeneProbeStatResult ref : this.referenceGeneProbeStatResults)
		{
			if (stringBuffer.length() > 0)
				stringBuffer.append(";");
			if (ref != null && ref.getReferenceGene() != null)
				stringBuffer.append(ref.getReferenceGene().getGeneSymbol());

		}
		geneSymbols = stringBuffer.toString();
		return stringBuffer.toString();
	}

	/*
	 * calculate statresult counts
	 */

	private String calculateStatResultCounts()
	{

		Map<String, List<StatResult>> statResultCounts = new HashMap<>();
		int total = 0;
		if (referenceGeneProbeStatResults == null)
			return "";
		for (ReferenceGeneProbeStatResult refGeneProbeStat : referenceGeneProbeStatResults)
		{
			for (ProbeStatResult probeStatResult : refGeneProbeStat.getProbeStatResults())
			{
				StatResult statResult = probeStatResult.getBestStatResult();

				if (statResult != null)
				{
					if (!statResultCounts.containsKey(statResult.toString()))
					{
						statResultCounts.put(statResult.toString(), new ArrayList<>());
					}
					statResultCounts.get(statResult.toString()).add(statResult);
					total++;
				}
			}
		}

		// Generate the String
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		StringBuilder sb = new StringBuilder();
		List<String> sortedList = new ArrayList<>(statResultCounts.keySet());
		Collections.sort(sortedList);
		for (String key : sortedList)
		{
			if (sb.length() > 0)
				sb.append(";");

			sb.append(key);
			sb.append(",");
			sb.append(statResultCounts.get(key).size());
			sb.append("(");
			sb.append(df.format(statResultCounts.get(key).size() / (double) total));
			sb.append(")");
		}

		return sb.toString();
	}

	private String double2String(Double d)
	{
		if (d == null)
		{
			return "";
		}
		else if (d.isNaN())
		{
			return "";
		}

		return String.valueOf(d);

	}

	private String int2String(Integer i)
	{
		if (i == null)
		{
			return "";
		}

		return String.valueOf(i);

	}

}
