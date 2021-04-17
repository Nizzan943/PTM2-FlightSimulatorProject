package Model;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector
{
	private ArrayList<CorrelatedFeatures> arrCor = new ArrayList<CorrelatedFeatures>();
	

	@Override
	public void learnNormal(TimeSeries ts)
	{
		String[] str = new String[ts.getCols().length];

		for (int i = 0; i < ts.getCols().length; i++)
			str[i] = ts.getCols()[i].getName();

		float maxPearson = -2;
		
		float f1;
		
		int index = 0;
		
		for (int i = 0; i < str.length; i++)
		{
			ArrayList<Float> temp = ts.getCols()[i].getFloats();
			
			float[] tempArr = ts.ArrListToArr(temp);
			
			for (int j = i + 1; j < str.length; j++) 
			{
				
				if (str[i] != str[j])
				{
					ArrayList<Float> temp2 = ts.getCols()[j].getFloats();
				
					float[] tempArr2 = ts.ArrListToArr(temp2);
				
					f1 = Math.abs(StatLib.pearson(tempArr, tempArr2));
				
					if (maxPearson < f1 && f1 >= ts.correlationTresh)
					{
						maxPearson = f1;
					
						index = j;
					}
				}
				
			}
			
			if (maxPearson > 0)
			{
			
				Point[] arrp = ts.ArrToPoint(tempArr, ts.ArrListToArr(ts.getCols()[index].getFloats()));
			
				Line l = StatLib.linear_reg(arrp);
			
				float maxThreshold = 0;
			
				for (int j = 0; j < arrp.length; j++)
					if (maxThreshold < StatLib.dev(arrp[j], l))
						maxThreshold = StatLib.dev(arrp[j], l);
			
				arrCor.add(new CorrelatedFeatures(str[i], str[index], maxPearson, l, maxThreshold + (float) 0.0389));
			}
			
			maxPearson = -2;
			
			index = 0;
			
		}
	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts)
	{
			
		List<AnomalyReport> list = new ArrayList<AnomalyReport>();
		
		
		for (int i = 0; i < arrCor.size(); i++)
		{
			String string1 =  arrCor.get(i).feature1;
			
			String string2 =  arrCor.get(i).feature2;
			
			ArrayList<Float> temp1 = new ArrayList<>();
			
			ArrayList<Float> temp2 = new ArrayList<>();
			
			for (int j = 0; j < ts.getCols().length; j++)
			{
				if(ts.getCols()[j].getName().equals(string1))
					temp1 = ts.getCols()[j].getFloats();
				
				if(ts.getCols()[j].getName().equals(string2))
					temp2 = ts.getCols()[j].getFloats();
			}
			
			
			float[] tempArr1 = ts.ArrListToArr(temp1);
			
			float[] tempArr2 = ts.ArrListToArr(temp2);
			
			Point[] pointsArr = ts.ArrToPoint(tempArr1,tempArr2);
			
			for (int j = 0; j < pointsArr.length; j++)
				if (StatLib.dev(pointsArr[j], arrCor.get(i).lin_reg) > arrCor.get(i).threshold)
				{
					AnomalyReport ar = new AnomalyReport(string1 + "-" + string2, j + 1);
					
					list.add(ar);
				}
			
		}
		
		return list;
	}
	
	public List<CorrelatedFeatures> getNormalModel()
	{
		return arrCor;
	}
}
