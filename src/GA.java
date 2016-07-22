

import java.util.HashMap;


public class GA {
	int g_size = 32; //遺伝子の数
	public static int population = 30; //集団の数
	public static int ex_population = 10;
	public static Gene genes[] = new Gene[population];
	public static Gene g_exc[] = new Gene[ex_population];
	private HashMap<String, Integer> moving = new HashMap<String, Integer>();
	private Maze maze;
	
	//遺伝子の初期化
	void initGene(){
		for(int i=0; i<genes.length;i++){
			Gene g;
			g = new Gene();
			g.generation = 0;
			g.score = 0;
			for(int j=0; j<g_size; j++){
				g.genetic[j] = (int)(Math.random()*2);
			}
			genes[i] = g;
		}
		for(int i=0; i<g_exc.length;i++){
			Gene g;
			g = new Gene();
			g.generation = 0;
			g.score = 0;
			for(int j=0; j<g_size; j++){
				g.genetic[j] = (int)(Math.random()*2);
			}
			g_exc[i] = g;
		}
	}
	
	//交叉
	void crossing(Gene g1, Gene g2, int index){
		//TODO 複雑な交叉の実装をすること
		int cross_point = (int)(Math.random()*(g_size/2))*2;
		cross_point = (int)(Math.random()*g_size);
		int gene_tmp[] = new int[g_size];
		
		for(int i=0; i< g_size; i++){
			if(i < cross_point){
				gene_tmp[i] = g1.genetic[i];
				//System.out.println("cross"+i);
			}else{
				gene_tmp[i] = g2.genetic[i];
			}
		}
			
		//TODO ジェネレーション数を反映させること
		genes[index].generation = g_exc[0].generation;
		genes[index].genetic = gene_tmp;
		genes[index].score = 0;
		
	}
	
	//突然変異
	void mutation(){
		//System.out.println(maze.generation);
		if(Math.random()*100 <= 5.0  || (maze.generation % 100 == 0 && maze.generation >=100)){
			int gene_num1 = (int)(Math.random()*g_size);
			int gene_num2 = (int)(Math.random()*g_size);
			int g_target = (int)(Math.random()*(population-ex_population));
			int gene_1 = genes[g_target].genetic[gene_num1];
			int gene_2 = genes[g_target].genetic[gene_num2];
			genes[g_target+ex_population].genetic[gene_num1] = gene_2;
			genes[g_target+ex_population].genetic[gene_num2] = gene_1;
			System.out.println("突然変異しました");
		}
		
	}
	
	//新世代の作成
	void createNewgene(){
		for(int i=0; i<g_exc.length; i++){
			genes[i] = g_exc[i];
			//TODO ジェネレーション数を反映させること
			//genes[i].generation = g_exc[i].generation+1;
		}
		
		int index = g_exc.length;
		
		while(index <= population-ex_population){
			for(int i=0; i<ex_population-1; i++){
				for(int j=i+1; j<ex_population; j++){
					if(index <= population-ex_population){
						crossing(g_exc[i], g_exc[j], index);
						//System.out.println(index);
						index++;
					}else{
						
						break;
						
					}
				}
			}
		}
		mutation();
	}
	
	void sortGene(){
		for(int i = 0; i < genes.length - 1; i++){
		    for(int j = i + 1; j < genes.length; j++){
		        if(genes[i].score < genes[j].score){
		            Gene tmp = genes[i];
		            genes[i] = genes[j];
		            genes[j] = tmp;
		        }
		    }
		}
		for(int i=0; i< g_exc.length; i++){
			g_exc[i] = genes[i];
			System.out.println(g_exc[i].score);
		}
	}
	
	//個体クラス
	/****************************/
	/***世代、遺伝子、スコアを記録する***/
	/****************************/
	public class Gene{
		int generation;
		int genetic[] = new int[g_size];
		int score;
	}
	
	public GA(){
	
	}
}
