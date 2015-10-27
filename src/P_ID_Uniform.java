import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class P_ID_Uniform {
	public static final boolean
		WritingFile = false,
		OnlyCT = true;
	
	public static final int 
		DataNum = 200,	//データ数
	
//		FIELD_SIZE = 100,				//フィールドサイズ
//		DISTANCE_FOR_INTERACTION = 1, 	//交流するための距離
		
//		m = 4, 							
		//Nを固定
//		N = 200,
//		n_from = 10,							//個体数（Agentはこれを知らん）
//		n_to = N,
		
		//nを固定
		n = 100,
		N_from = n,
		N_to = 200,							//個体数の上限値    (Agentが既知の情報)
		
		Delta = 4;						//最大次数の上限値(Agentが既知の情報)
//		delta = 4,						//グラフの最大次数(Agentはこれを知らん)
	public static int
//		n,
		N,
		t_max,						//タイマの上限値
		LID_MAX;					//最大のID(初期状況のlidに用いる)
	
	public static Boolean idlist[];
	
	public static String 
		name = "星顕",
	//  System.getProperty("user.name");
		DataPath = "\\Users\\" + name + "\\Dropbox\\Research\\",
		WritingPath = DataPath + "Data\\P_ID\\Uniform\\";
	
	public static void main (String args[]) {
		//fileに書き込むフォルダを用意する
		File file = new File(WritingPath);
        if (!file.exists()) {
            System.out.println("ディレクトリが存在しません。");
            System.exit(-1);
        }
        
		//各パラメータのfor文
		for (int N = n; N <= N_to; N++) {
//		for (n = n_from; n <= n_to; n++) {
			t_max = 8*N*Delta;
			LID_MAX = n;
			idlist = new Boolean[LID_MAX];
			long CTsum=0, HTsum=0;
			double CTave=0.0 , HTave=0.0;
			
			for(int Data=0; Data < DataNum; Data++){
				System.out.print("n: " + n + ", N: " + N + ", Data: " + (Data+1) + "/" + DataNum + "\t");
				//初期化
				Graph graph = new Graph(n, Graph.TORUS);
				Agent[] agent = new Agent[n];
				for (int i = 0; i < LID_MAX; i++) idlist[i] = false;
				for (int i = 0; i < n; i++) agent[i] = new Agent();
				long CT = 0, HT = 0;
				boolean CT_count_flag = true, HT_count_flag = false;
				
				while (true) {
					boolean issafeconfiguration = false;
					if (IsSafeConfiguration(agent)) { 
						CT_count_flag = false; 
						HT_count_flag = true; 
						issafeconfiguration = true;
						if (OnlyCT) break;
					}
					
					//維持時間終了
					if (HT_count_flag && !issafeconfiguration) { 
						break; 
					}
					
					//initiatorとresponderを決める
					Random R = new Random();
					int initiator = -1, responder = -1;
					while (initiator == responder) { 
						initiator = R.nextInt(n);
						responder = R.nextInt(n);
					}
					
					//交流できるなら交流
					if (graph.List[initiator][responder]) {
						Interaction.interaction(agent[initiator], agent[responder]);
						if (CT_count_flag) CT++;
						if (!OnlyCT) if (HT_count_flag) HT++;
					}
				}	//while(true)終了
				if (CT_count_flag) System.out.print("CT = " + CT);
				if (!OnlyCT) System.out.print(", HT = " + HT);
				System.out.print("\n");
				CTsum += CT;
				if (!OnlyCT) HTsum += HT;
			}	//for Data終了
			CTave = (double)CTsum / DataNum;
			if (!OnlyCT) HTave = (double)HTsum / DataNum;
			
			System.out.println("平均収束時間：　" + CTave);
			if (!OnlyCT) System.out.println("平均維持時間：　" + HTave);
			
			try{
		        String stringCTave = String.valueOf(CTave);
//		        String stringHTave;
//		        if(HTave != 0){
//		        	stringHTave = String.valueOf(HTave);
//		        }
//		        else { stringHTave = "Not Have a safe configuration";}
		        
				String nvalue = new Integer(n).toString();
				String Nfromvalue = new Integer(N_from).toString();
				String Ntovalue = new Integer(N_to).toString();

		        File file_nCT = new File(WritingPath + "CT\\" + "n=" + nvalue + "_N=" + Nfromvalue + "~" + Ntovalue + ".txt");
//		        File file_nHT = new File(WritingPath + "HT\\" + "n=" + nvalue + "_N=" + Nfromvalue + "~" + Ntovalue + ".txt");

		        if(!file_nCT.exists()){ file_nCT.createNewFile(); }
//		        if(!file_NHT.exists()){ file_nHT.createNewFile(); }

		        PrintWriter pw_nCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_nCT, true)));
//		        PrintWriter pw_nHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_nHT, true)));

		        pw_nCT.write(stringCTave + "\r\n");
//		        pw_nHT.write(stringHTave + "\r\n");

		        pw_nCT.close();
//		        pw_nHT.close();
		        
//				String Nvalue = new Integer(N).toString();
//				String nfromvalue = new Integer(n_from).toString();
//				String ntovalue = new Integer(n_to).toString();
//
//		        File file_NCT = new File(WritingPath + "CT\\" + "N=" + Nvalue + "_n=" + nfromvalue + "~" + ntovalue + ".txt");
////		        File file_NHT = new File(WritingPath + "HT\\" + "N=" + Nvalue + "_n=" + nfromvalue + "~" + ntovalue + ".txt");
//
//		        if(!file_NCT.exists()){ file_NCT.createNewFile(); }
////		        if(!file_NHT.exists()){ file_NHT.createNewFile(); }
//
//		        PrintWriter pw_NCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_NCT, true)));
////		        PrintWriter pw_NHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_NHT, true)));
//
//		        pw_NCT.write(stringCTave + "\r\n");
////		        pw_sHT.write(stringHTave + "\r\n");
//
//		        pw_NCT.close();
////		        pw_sHT.close();

		      }catch(IOException e){
		        System.out.println(e);
		      }
		}
	}
	
	//安定状況かの判別
	private static boolean IsSafeConfiguration(Agent agent[]){
//		for( int j = 0; j < n; j++) System.out.print(agent[j].lid + "\t");
//		System.out.println("\n");
		
		//最小のidとそのノードを見つける
		int min = LID_MAX+1;
		Agent v_min = null;
		for (int i = 0; i < n; i++) 
			if (min > agent[i].var) {
				min = agent[i].var;
				v_min = agent[i];
			}
		
		//v.lid = id(v_min)かどうか
		for(int i=0; i < n; i++) 
			if (agent[i].lid != v_min.var) {
				return false; 
			}
		
		//セーフタイムかどうか
		for(int i=0; i < n; i++)
			if(agent[i].timer < n/2){
				return false;
			}
		
		//v_min.timer = t_maxかどうか
		if (v_min.timer != t_max) {
			return false; 
		}
		
		//全部条件満たしてたらtrueを返す
		return true;
	}
	
	//距離を返す
	private static double distance (Agent initiator, Agent responder) {
		return Math.sqrt( (initiator.x - responder.x)*(initiator.x - responder.x)-(initiator.y - responder.y)*(initiator.y - responder.y) );
	}
}
