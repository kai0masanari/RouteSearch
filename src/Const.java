

import java.util.HashMap;

public class Const {
	public static final int search_gene = 50000;
	//public static final int search_gene = 1000;
	//public static final double error_th = 0.001;
	public static final int hidden_neurons = 16;
	//public static final float error_th = (float) 0.05;

	
	public final String types[] = {"input","hidden","output"};
	
	//please edit parameters 
	//leraning_rate and number of hidden neurons, threshold
	public final double learning_rate = 0.3;
	
	public static final double threshold = 0.001;
	
	public static final String forward = "00";
	public static final String right = "01";
	public static final String left = "10";
	public static final String back = "11";
	
	public static final HashMap<String, Integer> moving = new HashMap<String, Integer>();


	
		
	public Const(){
		
	}
}
