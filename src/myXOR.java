

import java.util.Random;


public class myXOR {
	static Const _const = new Const();
	
	static int NN_INPUT = 4;//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ蜈･蜉帛ｱ､
	static int NN_HIDDEN = 20;//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ荳ｭ髢灘ｱ､
	static int NN_OUTPUT = 2;//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ蜃ｺ蜉帛ｱ､
	int trial = 1;
	//double error_th = 0.001;
	double error_th = 0.001;
	
	//int trial_times = 1000000;
	int trial_times = 300000;
	
	double NN_weight1[][];//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ蜈･蜉帛ｱ､縺ｨ荳ｭ髢灘ｱ､縺ｮ驥阪∩
	double NN_weight2[][];//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ荳ｭ髢灘ｱ､縺ｨ蜃ｺ蜉帛ｱ､縺ｮ驥阪∩

	double input[];
	double hidden[];
	public double output[];

	double input_sample[][] = {//險鍋ｷｴ繝�繝ｼ繧ｿ(蜈･蜉�)
			{0, 0, 0, 0},
			{0, 0, 0, 1},
			{0, 0, 1, 0},
			{0, 0, 1, 1},
			{0, 1, 0, 0},
			{0, 1, 0, 1},
			{0, 1, 1, 0},
			{0, 1, 1, 1},
			{1, 0, 0, 0},
			{1, 0, 0, 1},
			{1, 0, 1, 0},
			{1, 0, 1, 1},
			{1, 1, 0, 0},
			{1, 1, 0, 1},
			{1, 1, 1, 0},
			{1, 1, 1, 1},
	};

	public double res_sample[][] = {//險鍋ｷｴ繝�繝ｼ繧ｿ(蜃ｺ蜉�)
			{0, 1},
			{0, 1},
			{0, 1},
			{0, 1},
			{0, 0},
			{0, 0},
			{0, 0},
			{0, 0},
			{0, 1},
			{0, 1},
			{0, 1},
			{0, 1},
			{1, 0},
			{1, 1},
			{1, 0},
			{1, 1},
	};

	double learning_rate;//蟄ｦ鄙堤紫

	static int count = 1;//隧ｦ陦悟屓謨ｰ繧ｫ繧ｦ繝ｳ繝�

	public myXOR(){

		input  = new double[NN_INPUT];
		hidden = new double[NN_HIDDEN];
		output = new double[NN_OUTPUT];

		Random rnd = new Random();

		NN_weight1 = new double[NN_INPUT][NN_HIDDEN];//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ蜈･蜉帛ｱ､縺ｨ荳ｭ髢灘ｱ､縺ｮ驥阪∩繧�-0.1�ｽ�0.1縺ｮ髢薙〒險ｭ螳�
		for(int i=0; i<NN_INPUT; i++){
			for(int j=0; j<NN_HIDDEN; j++){
				NN_weight1[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}

		NN_weight2 = new double[NN_HIDDEN][NN_OUTPUT];//繝九Η繝ｼ繝ｩ繝ｫ繝阪ャ繝医Ρ繝ｼ繧ｯ縺ｮ荳ｭ髢灘ｱ､縺ｨ蜃ｺ蜉帛ｱ､縺ｮ驥阪∩繧�-0.1�ｽ�0.1縺ｮ髢薙〒險ｭ螳�
		for(int i=0; i<NN_HIDDEN; i++){
			for(int j=0; j<NN_OUTPUT; j++){
				NN_weight2[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}

		learning_rate = 0.1 ;//蟄ｦ鄙堤紫縺ｮ蛻晄悄蛹�(0<n<=1)
	}

	public void learning(){
		double e = 0.0;//莠御ｹ苓ｪ､蟾ｮ縺ｮ邱丞柱縺ｮ蛻晄悄蛹�

		while(true && trial < trial_times){
		for(int i=0; i<16; i++){//險鍋ｷｴ繝�繝ｼ繧ｿ蛻�郢ｰ繧願ｿ斐☆
			e = 0;
			compute(input_sample[i]);//險鍋ｷｴ繝�繝ｼ繧ｿ(蜈･蜉�)繧誰N縺ｫ蜈･蜉帙＠縺ｦ�ｼ悟推繝九Η繝ｼ繝ｭ繝ｳ縺ｮ蜃ｺ蜉帙ｒ險育ｮ�
			backPropagation(res_sample[i]);//蜷�繝九Η繝ｼ繝ｭ繝ｳ縺ｮ蜃ｺ蜉帙→險鍋ｷｴ繝�繝ｼ繧ｿ(蜃ｺ蜉�)縺ｨ縺ｮ隱､蟾ｮ繧定ｨ育ｮ�
			e += calcError(res_sample[i]);
		}
			trial++;
			//System.out.println("Error = " + e +"\n");
			if(e< error_th)
				break;
		}
	}

	public void compute(double in[]){//險鍋ｷｴ繝�繝ｼ繧ｿ(蜈･蜉�)繧誰N縺ｫ蜈･蜉帙＠縺ｦ�ｼ悟推繝九Η繝ｼ繝ｭ繝ｳ縺ｮ蜃ｺ蜉帙ｒ險育ｮ�

		for(int i=0; i<NN_INPUT; i++){//蜈･蜉帛ｱ､
			input[i] = (double)in[i];
		}

		for(int i=0; i<NN_HIDDEN; i++){//髫�繧悟ｱ､縺ｮ險育ｮ�
			hidden[i] = 0.0;
			for(int j=0; j<NN_INPUT; j++){
				hidden[i] += NN_weight1[j][i] * input[j];
			}
			hidden[i] = sigmoid(hidden[i]);
		}

		for(int i=0; i<NN_OUTPUT; i++){//蜃ｺ蜉帛ｱ､縺ｮ險育ｮ�
			output[i] = 0.0;
			for(int j=0; j<NN_HIDDEN; j++){
				output[i] += NN_weight2[j][i] * (hidden[j]);
			}
			output[i] = sigmoid(output[i]);
		}
	}

	public double sigmoid(double i){//繧ｷ繧ｰ繝｢繧､繝蛾未謨ｰ

		double a = 1.0 / (1.0 + Math.exp(-i));
		return a;
	}

	public void backPropagation(double res[]){//蜷�繝九Η繝ｼ繝ｭ繝ｳ縺ｮ蜃ｺ蜉帙→險鍋ｷｴ繝�繝ｼ繧ｿ(蜃ｺ蜉�)縺ｨ縺ｮ隱､蟾ｮ繧定ｨ育ｮ�

		for(int i=0; i<NN_OUTPUT; i++){//髫�繧�>蜃ｺ蜉帙�ｮ驥阪∩繧呈峩譁ｰ
			for(int j=0; j<NN_HIDDEN; j++){
				double delta = -learning_rate*( -(res[i]-output[i])*output[i]*(1.0-output[i])*hidden[j] );
				NN_weight2[j][i] += delta;
			}
		}

		for(int i=0; i<NN_HIDDEN; i++){//蜈･蜉�>髫�繧後�ｮ驥阪∩繧呈峩譁ｰ

			double sum = 0.0;

			for(int k=0; k<NN_OUTPUT; k++){
				sum += NN_weight2[i][k]*(res[k]-output[k])*output[k]*(1.0-output[k]);
			}

			for(int j=0; j<NN_INPUT; j++){
				double delta = learning_rate*hidden[i]*(1.0-hidden[i])*input[j]*sum;
				NN_weight1[j][i] += delta;
			}
		}
	}

	public double calcError(double teach[]){//莠御ｹ苓ｪ､蟾ｮ縺ｮ險育ｮ�

		double e = 0.0;

		for(int i=0; i<NN_OUTPUT; i++){
			e += Math.pow(teach[i]-output[i], 2.0);
		}
		e *= 0.5;
		return e;
	}

}
