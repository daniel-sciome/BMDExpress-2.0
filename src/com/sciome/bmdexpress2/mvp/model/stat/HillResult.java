package com.sciome.bmdexpress2.mvp.model.stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HillResult extends StatResult
{

	private static final long serialVersionUID = -527776055122273597L;
	/**
	 * GeneId
	 */

	private short kFlag;

	public HillResult()
	{
		super();
	}

	public short getkFlag()
	{
		return kFlag;
	}

	public void setkFlag(short kFlag)
	{
		this.kFlag = kFlag;
	}

	@Override
	public List<String> getColumnNames()
	{

		return new ArrayList<String>(Arrays.asList("Hill BMD", "Hill BMDL", "Hill BMDU", "Hill fitPValue",
				"Hill fitLogLikelihood", "Hill AIC", "Hill adverseDirection", "Hill BMD/BMDL", "Flagged Hill",
				"Hill Parameter Intercept", "Hill Parameter v", "Hill Parameter n", "Hill Parameter k"));

	}

	@Override
	public List<Object> getRow()
	{
		Double param1 = null;
		Double param2= null;
		Double param3 = null;
		Double param4 = null;
		if(curveParameters !=null)
		{
			param1 = curveParameters[0];
			param2 = curveParameters[1];
			param3 = curveParameters[2];
			param4 = curveParameters[3];
		}
		
		return new ArrayList<Object>(
				Arrays.asList((this.getBMD()), (this.getBMDL()), (this.getBMDU()), (this.getFitPValue()),
						(this.getFitLogLikelihood()), (this.getAIC()), (this.getAdverseDirection()),
						(this.getBMD() / this.getBMDL()), (this.getkFlag()), param1,
						param2, param3, param4));
	
	}

	@Override
	public String toString()
	{
		return "Hill";
	}

	@Override
	public List<String> getParametersNames()
	{
		return new ArrayList<String>(Arrays.asList("intercept", "v-parameter", "n-parameter", "k-parameter"));
	}

}
