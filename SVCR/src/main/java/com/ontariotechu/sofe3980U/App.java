package com.ontariotechu.sofe3980U;


import java.io.FileReader; 
import java.util.List;
import java.util.ArrayList;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
    public static int argmin( float [] arr)
	{
		int min=0;
		for (int i=1;i<arr.length;i++)
		{
			if(arr[i]<arr[min]){
				min=i;
			}
		}
		return min;
	}
	public static void main( String[] args )
    {
		String filePath[]=new String[]{"model_1.csv","model_2.csv","model_3.csv"};
		FileReader filereader;
		float MSE[] =new float[3];
		float MAE[] =new float[3];
		float MARE[] =new float[3];
		for(int i=0;i<3;i++)
		{
			System.out.println("for "+filePath[i]); 
			List<String[]> allData;
			try{
				filereader = new FileReader(filePath[i]); 
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
				allData = csvReader.readAll();
			}
			catch(Exception e){
				System.out.println( "Error reading the CSV file" );
				return;
			}
			MSE[i]=0;
			MAE[i]=0;
			MARE[i]=0;
			int count=0;
			for (String[] row : allData) { 
				float yTrue=Float.parseFloat(row[0]);
				float yPred=Float.parseFloat(row[1]);
				MSE[i]+=Math.pow(yTrue-yPred,2);
				MAE[i]+=Math.abs(yTrue-yPred);
				MARE[i]+=Math.abs(yTrue-yPred)/(Math.abs(yTrue)+1e-3);
				
				count++;
			} 
			MSE[i]/=count;
			System.out.println("\tMSE ="+MSE[i]); 
			MAE[i]/=count;
			System.out.println("\tMAE ="+MAE[i]); 
			MARE[i]/=count;
			System.out.println("\tMARE ="+MARE[i]); 
		}
		System.out.println("According to MSE, The best model is "+filePath[argmin(MSE)]); 
		System.out.println("According to MAE, The best model is "+filePath[argmin(MAE)]); 
		System.out.println("According to MARE, The best model is "+filePath[argmin(MARE)]); 
    }
}
