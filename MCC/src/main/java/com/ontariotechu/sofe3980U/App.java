package com.ontariotechu.sofe3980U;


import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
	public static int argmax( float [] arr)
	{
		int min=0;
		for (int i=1;i<arr.length;i++)
		{
			if(arr[i]>arr[min]){
				min=i;
			}
		}
		return min;
	}
    public static void main( String[] args )
    {
		String filePath="model.csv";
		FileReader filereader;
		List<String[]> allData;
		try{
			filereader = new FileReader(filePath); 
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			allData = csvReader.readAll();
		}
		catch(Exception e){
			System.out.println( "Error reading the CSV file" );
			return;
		}
		float CE=0;
		int count=0;
		float[] y_predicted=new float[5];
		int [] [] CM = new int[5][5];
		for (String[] row : allData) { 
			int y_true=Integer.parseInt(row[0]);
			for(int i=0;i<5;i++){
				y_predicted[i]=Float.parseFloat(row[i+1]);
			}
			CE-=Math.log(y_predicted[y_true-1]);
			CM[argmax(y_predicted)][y_true-1]+=1;
			count++;
		} 
		CE/=count;
		System.out.println("CE ="+CE);
		System.out.println("Confusion matrix"); 			
		System.out.println("\t\ty=1\t y=2\t y=3\t y=4\t y=5");
		for(int j=0;j<5;j++){
			System.out.print("\ty^="+(j+1));
			for(int k=0;k<5;k++){
				System.out.print("\t"+CM[j][k]);	
			}
			System.out.println();
		}
	}
	
}
