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
		String filePath[]=new String[]{"model_1.csv","model_2.csv","model_3.csv"};
		FileReader filereader;
		float BCE[] =new float[3];
		int[][][] CM =new int[3][2][2];
		float acc[] =new float[3];
		float pre[] =new float[3];
		float recall[] =new float[3];
		float f1[] =new float[3];
		float auc[] =new float[3];
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
			BCE[i]=0;
			int count=0;
			int FP[]= new int[101];
			int TP[]= new int[101];
			int np=0;
			for (String[] row : allData) { 
				int yTrue=Integer.parseInt(row[0]);
				float yPred=Float.parseFloat(row[1]);
				BCE[i]-=Math.log(yTrue+(1-2*yTrue)*yPred);
				CM[i][(yPred>=0.5)? 0:1][1-yTrue]+=1;
				np+=yTrue;
				count++;
				for (int th =0 ;th<=100;th++){
					int pred=((yPred>=(th/100.0)? 1:0));
					TP[th]+=yTrue*pred;
					FP[th]+=(1-yTrue)*pred;
				}
			} 
			float [] x=new float[101];
			float [] y=new float[101];
			for (int th =0 ;th<=100;th++){
				y[th]=TP[th]*1.0f/np;
				x[th]=FP[th]*1.0f/(count-np);
				//System.out.println("("+x[th]+","+y[th]+")");
			}
			auc[i]=0;
			for (int th =1 ;th<=100;th++){
				auc[i]+=(y[th]+y[th-1])*Math.abs(x[th]-x[th-1])/2;
			}
			BCE[i]/=count;
			System.out.println("\tBCE ="+BCE[i]);
			System.out.println("\tConfusion matrix"); 			
			System.out.println("\t\t\ty=1\t y=0");
			for(int j=0;j<2;j++){
				System.out.print("\t\ty^="+(1-j));
				for(int k=0;k<2;k++){
					System.out.print("\t"+CM[i][j][k]);	
				}
				System.out.println();
			}
			acc[i] =(CM[i][0][0]+CM[i][1][1]+0.0f)/count;
			System.out.println("\tAccuracy ="+acc[i]);
			pre[i] =(CM[i][0][0]+0.0f)/(CM[i][0][0]+CM[i][0][1]);
			System.out.println("\tPrecision ="+pre[i]);
			recall[i] =(CM[i][0][0]+0.0f)/(CM[i][0][0]+CM[i][1][0]);
			System.out.println("\tRecall ="+recall[i]);
			f1[i] =2*acc[i]*recall[i]/(acc[i]+recall[i]);
			System.out.println("\tf1 score ="+f1[i]);
			System.out.println("\tauc roc ="+auc[i]);
		}
		System.out.println("According to BCE, The best model is "+filePath[argmin(BCE)]); 
		System.out.println("According to Accuracy, The best model is "+filePath[argmax(acc)]); 
		System.out.println("According to Precision, The best model is "+filePath[argmax(pre)]); 
		System.out.println("According to Recall, The best model is "+filePath[argmax(recall)]); 
		System.out.println("According to F1 score, The best model is "+filePath[argmax(f1)]); 
		System.out.println("According to AUC ROC, The best model is "+filePath[argmax(auc)]); 
    }
}

