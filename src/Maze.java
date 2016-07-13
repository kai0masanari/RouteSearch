
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Maze {
	static Const _const = new Const();
	static GA _genetic = new GA();
	int[][] blocks = new int[13][13];//迷路配列
	int my_x;//x座標
	int my_y;//y座標
	int my_arrow = 0;//自分が向いている方向
	String[] path = new String[_genetic.population]; // 移動経路
	double around[] = new double[4];//周りの状態
	int score[] = new int[_genetic.population];//スコア
	int distance[] = new int[_genetic.population];//マンハッタン距離
	static myXOR[] xor = new myXOR[_genetic.population];//20個のニューラルネット
	public static int first_learn[][] = new int[_genetic.population][_genetic.g_size];//output.txtを読み込んで格納
	
	

	static int generation = 1;

	private Maze(){
		readFile();//迷路ファイルの読み込み
		//makeMaze();//迷路のランダム生成
		this.my_x = 1;
		this.my_y = 10;
		for(int i = 0;i<_genetic.population;i++){
			xor[i] = new myXOR();
		}

	}

	public void makeMaze(){
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 12; j++){
				if(i == 0 || i == 11 || j == 0 || j == 11){
					blocks[i][j] = 2;
				}else{
					blocks[i][j] = 1;
				}
			}
		}

		// 掘り始める初期位置を決定
		int first_x = (new Random()).nextInt(10) + 1;
		int first_y = (new Random()).nextInt(10) + 1;

		// 掘りきるまで再帰
		recur(first_x, first_y);

		// 再帰処理が終わった段階で，StartとGoalどちらかは壁になっているので，その周りを掘る
		if(blocks[10][1] == 1){
			blocks[10][1] = blocks[9][1] = blocks[10][2] = 0;
		}else if(blocks[1][10] == 1){
			blocks[1][10] = blocks[2][10] = blocks[1][9] = 0;
		}

		// Startを3に，Goalを4に
		blocks[10][1] = 3;
		blocks[1][10] = 4;

		// 生成した迷路をコンソールに表示
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 12; j++){
				//System.out.print(blocks[i][j]);

				if(i == 10 && j == 1){
					System.out.print("S");
				}else if(i == 1 && j == 10){
					System.out.print("G");
				}else if(blocks[i][j] == 2 || blocks[i][j] == 1){
					blocks[i][j] = 1;
					System.out.print("#");
				}else{
					blocks[i][j] = 0;
					System.out.print(".");
				}

			}
			System.out.println("");
		}
	}

	// 再帰的に穴を掘り進む関数
	private void recur(int x, int y){

		// 現在のマスを通路に
		blocks[x][y] = 0;

		// ランダムでチェックする方向を決める．順番はランダムだけど全方向チェックしたいからとりあえず配列で
		// 配列に4方向(0,1,2,3)を格納し，シャッフル
		// 0…前 1…右 2…後 3…左
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		Collections.shuffle(list);

		// 4方向に対して再帰的にチェック
		// 指定した方向の2マス先を確認し，そのマスが掘ることのできる通路だったら再帰
		// 1マス先を通路にし，2マス先で再帰的にこの関数を呼び出す
		for(int i = 0; i < 4; i++){
			int direction = list.get(i);
			switch(direction){
			case 0:
				if(exists(x, y + 2) && blocks[x][y + 2] == 1){
					blocks[x][y + 1] = 0;
					recur(x, y + 2);
				}
				break;
			case 1:
				if(exists(x + 2, y) && blocks[x + 2][y] == 1){
					blocks[x + 1][y] = 0;
					recur(x + 2, y);
				}
				break;
			case 2:
				if(exists(x, y - 2) && blocks[x][y - 2] == 1){
					blocks[x][y - 1] = 0;
					recur(x, y - 2);
				}
				break;
			case 3:
				if(exists(x - 2, y) && blocks[x - 2][y] == 1){
					blocks[x - 1][y] = 0;
					recur(x - 2, y);
				}
				break;
			}
		}
	}

	// 指定した座標が存在するかどうかを確認する関数
	private boolean exists(int x, int y){
		if(x < 0 || x > 11 || y < 0 || y > 11){
			return false;
		}else{
			return true;
		}
	}

	//初期化
	private void Initialize(int i){
		score[i] = 0;
		my_x = 1;
		my_y = 10;
		my_arrow= 0;
		path[i] = "";
		distance[i] = 0;
	}

	//実行
	private void inputArea(int number){
		Initialize(number);
		for(int trial = 1; trial<=100; trial++ ){
			//周りの状態を取得
			if(my_arrow==0){//前
				around[0] = blocks[my_y-1][my_x];
				around[1]=  blocks[my_y][my_x+1];
				around[2] = blocks[my_y+1][my_x];
				around[3] = blocks[my_y][my_x-1];
			}
			else if(my_arrow==1){//右
				around[0] = blocks[my_y][my_x+1];
				around[1]=  blocks[my_y+1][my_x];
				around[2] = blocks[my_y][my_x-1];
				around[3] = blocks[my_y-1][my_x];
			}
			else if(my_arrow==2){//後ろ
				around[0] = blocks[my_y+1][my_x];
				around[1]=  blocks[my_y][my_x-1];
				around[2] = blocks[my_y-1][my_x];
				around[3] = blocks[my_y][my_x+1];
			}
			else if(my_arrow==3){//左
				around[0] = blocks[my_y][my_x-1];
				around[1]=  blocks[my_y-1][my_x];
				around[2] = blocks[my_y][my_x+1];
				around[3] = blocks[my_y+1][my_x];

			}

			xor[number].compute(around);//周りの状態を入力
			score[number]++;
			double out1 = Math.round(xor[number].output[0]);//出力1
			double out2 = Math.round(xor[number].output[1]);//出力2
			arrowDecide(out1,out2);//向く方向の決定

			//ゴールが近くにある場合
			if(blocks[my_y-1][my_x]==4){
				path[number] +="u";
				my_y--;
			}else if(blocks[my_y][my_x+1]==4){
				my_x++;
				path[number]+="r";

			}else{
				Move(number);
			}

			if(my_x==10 & my_y==1){//ゴールにたどり着いたら
				break;
			}
		}
		distance[number] = Math.abs(my_x - 10) + Math.abs(my_y - 1);

	}

	//自分が向いている方向をもとに移動
	private void Move(int i){
		switch(my_arrow){
		case 0://前
			if(blocks[my_y-1][my_x]==0){//移動できる場合
				my_y--;
				path[i] += "u";
			}
			break;
		case 1://右
			if(blocks[my_y][my_x+1]==0){
				my_x++;
				path[i] += "r";
			}
			break;
		case 2://下
			if(blocks[my_y+1][my_x]==0){
				my_y++;
				path[i] += "d";
			}
			break;
		case 3://左
			if(blocks[my_y][my_x-1]==0){
				my_x--;
				path[i] += "l";
			}
			break;
		}
	}

	//迷路ファイルの読み込み
	private void readFile(){
		try{
	    	File file = new File("maze.txt");
	    	BufferedReader br = new BufferedReader(new FileReader(file));
	    	String str= br.readLine();
	    	int index=0;
	    	String b[][] = new String[13][13];
	    	while(str!= null){
	        	b[index] = str.split(",");
	        	str = br.readLine();
	        	index++;
	    	}
			br.close();
	    	for(int i = 0; i < 12; i++){
				for(int j = 0; j < 12; j++){
					blocks[i][j]  =  Integer.parseInt(b[i][j].trim());
				}
			}
			}catch(FileNotFoundException e){
		        System.out.println(e);
			}catch(IOException e){
		        System.out.println(e);
			}

		}

	//迷路を表示
	private void printArea(){
		//System.out.println("ターン数："+ count);
		//System.out.println("向き："+ my_arrow);
		for(int i = 0; i < 12;i++){
			for(int j = 0; j < 12; j++){
				switch(blocks[i][j]){
					case 0:
					System.out.print(".");
					break;
					case 1://障害物
					System.out.print("*");
					break;
					case 5://ゴール
					System.out.print("G");
					break;
					case 10://自分の位置
					System.out.print("@");
					break;
					default:
					break;
				}
			}
			System.out.println();
		}
	}

	//ニューラルネットの出力をもとに移動する方向を決定
	private void arrowDecide(double output1, double output2){
		//前進
		if(output1 == 0 && output2 == 0){
			my_arrow = my_arrow;
		}
		//後ろ
		else if(output1 == 1 && output2 == 1){
			if(my_arrow==0){//前
				my_arrow = 2;
			}else if(my_arrow==1){//右
				my_arrow = 3;
			}else if(my_arrow==2){//後ろ
				my_arrow = 0;
			}else{//左
				my_arrow = 1;
			}
		}
		//右回転
		else if(output1 == 0 && output2 == 1 ){
			if(my_arrow == 0){//前
				my_arrow = 1;
			}else if(my_arrow==1){//右
				my_arrow = 2;
			}else if(my_arrow==2){//後
				my_arrow=3;
			}else{//左
				my_arrow = 0;
			}
		}
		//左回転
		else{
			if(my_arrow == 0){//前
				my_arrow = 3;
			}else if(my_arrow==1){//右
				my_arrow = 0;
			}else if(my_arrow==2){//後ろ
				my_arrow = 1;
			}else{//左
				my_arrow = 2;
			}
		}
	}

	//出力をそれぞれ学習
	private void readOutput(){
		try{
	    	File file = new File("output.txt");
	    	BufferedReader br = new BufferedReader(new FileReader(file));
	    	String str= br.readLine();
	    	String b[][] = new String[_genetic.population][_genetic.g_size];
	    	int ind = 0;
	    	while(str!= null){
	        	b[ind] = str.split(",");
	        	str = br.readLine();
	        	ind++;
	    	}
			br.close();
			
	    	for(int i = 0; i < _genetic.population; i++){
	    		for(int j = 0;j < _genetic.g_size; j++){
	    			first_learn[i][j] = Integer.parseInt(b[i][j].trim());
					//System.out.print(first_learn[i][j]);
	    		}
	    		_genetic.genes[i].genetic = first_learn[i];
	    		//System.out.println();

			}
			}catch(FileNotFoundException e){
		        System.out.println(e);
			}catch(IOException e){
		        System.out.println(e);
			}

			int index1 = 0;
			int index2 = 0;
			for(int i = 0; i < _genetic.population ; i ++){
				//System.out.println(i+"個目のニューラルネットワーク");
				index1 = 0;
				index2 = 0;
				for(int j = 0;j<_genetic.g_size;j++){
					xor[i].res_sample[index2][index1] = first_learn[i][j];
					//System.out.print(xor[i].res_sample[index2][index1]);
					index1++;
					if(index1%2==0){
						//System.out.println();
						index1 = 0;
						index2++;
					}
				}
				//res_sampleにfirst_learn配列を対応させてから学習

				xor[i].learning();
			}
		}

	public int[][] getMazeBlock(){
		return this.blocks;
	}

	//最短距離（スコアが最少)のニューラルネットワークの経路を返す
	public String getPath(){
		int min = score[0];
		int number = 0;
		for(int i = 1; i<_genetic.population; i++){
			if(min > score[i]){
				min = score[i];
				number = i;
			}
		}
		return path[number];
	}

	public static void main(String args[]){
		Maze m = new Maze();
		_genetic.initGene();
		m.readOutput();


		while(generation <= _const.search_gene){

			if(generation > 1){

				for(int i=0; i < _genetic.population; i++){
					for(int j=0; j<_genetic.g_size; j++){
						first_learn[i][j] = _genetic.genes[i].genetic[j];
					}

				}


				int index1 = 0;
				int index2 = 0;
				for(int i = 0; i < _genetic.population ; i ++){
					//System.out.println(i+"個目のニューラルネットワーク");
					index1 = 0;
					index2 = 0;
					for(int j = 0;j<_genetic.g_size;j++){
						xor[i].res_sample[index2][index1] = first_learn[i][j];
						//System.out.print(xor[i].res_sample[index2][index1]);
						index1++;
						if(index1%2==0){
							//System.out.println();
							index1 = 0;
							index2++;
						}
					}
					//res_sampleにfirst_learn配列を対応させてから学習

					xor[i].learning();
					
				}
				System.out.println("ニューラルネットの学習終了");
			}

			System.out.println(generation+"世代目の遺伝子");
			for(int i = 0 ; i<_genetic.population; i++){
				m.inputArea(i);

				/*
				System.out.println(generation+"世代目の遺伝子");
				System.out.println(i+1 + "個目のニューラルネットワーク");
				System.out.println("試行回数：" + m.score[i]);
				System.out.println("マンハッタン距離:" + m.distance[i]);
				System.out.println("スコア:" + (18-m.distance[i]+100-m.score[i]));
				*/

				_genetic.genes[i].score = 18-m.distance[i]+100-m.score[i];
				_genetic.genes[i].generation = generation;

			}
			

			generation++;

			//優秀な個体
			_genetic.sortGene();

			
			if(_genetic.g_exc[0].score > 83){
				new MazeViewer(m);
				break;
			}
			
			//_genetic.initGene();
			//子の世代を作る
			_genetic.createNewgene();

			if(generation == _const.search_gene+1){
				new MazeViewer(m);
			}
		}

	}
}