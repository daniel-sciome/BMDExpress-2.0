package com.sciome.bmdexpress2.mvp.presenter.mainstage.dataview;

import com.sciome.bmdexpress2.mvp.viewinterface.mainstage.dataview.IBMDExpressDataView;
import com.sciome.bmdexpress2.shared.eventbus.BMDExpressEventBus;

public class OneWayANOVADataViewPresenter extends BMDExpressDataViewPresenter<IBMDExpressDataView>
{

	public OneWayANOVADataViewPresenter(IBMDExpressDataView view, BMDExpressEventBus eventBus)
	{
		super(view, eventBus);
		init();
	}

	private void init()
	{
	}

}
