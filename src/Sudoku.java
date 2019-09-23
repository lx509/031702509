import java.io.*;
import java.util.*;

public class sudoku {

	static int x=0;//totalchess->chess时totalchess行数计数
	static int y=0;//chess->totalchess时totalchess行数计数
	
	
	public static void main(String[] args) {
		
		String fileinName=null;
		String fileoutName=null;
		
		int m=0;
		int n=0;
		
		if(args.length!=8) {
			System.out.println("Invalid input.");
			System.exit(0);
		}
		
		if(args.length>0&&args!=null) {
			for(int i=0;i<args.length;i++) {
				switch(args[i]) {
				case "-m":
					m=Integer.parseInt(args[++i]);
					break;
				case "-n":
					n=Integer.parseInt(args[++i]);
					break;
				case "-i":
					fileinName=args[++i];
					break;
				case "-o":
					fileoutName=args[++i];
					break;
				default:break;
				}
			}
		}
		else {
			System.out.println("未输入参数");
			System.exit(1);
		}
		
		File filein = new File(fileinName);
		int[][]totalchess=new int[m*n][m];
		int[][] chess = new int[m][m];
		
		if((filein==null)||!filein.exists()){
			System.out.println("输入文件不存在");
			System.exit(1);
		}
		int N=n;
		
		readFile(filein,totalchess);
		
		
		
		while(N>0) {//N重循环计数
			
			for(int k = 0; k < chess.length ;k++ )
				 for(int j =0; j < chess[k].length; j ++){
					 chess[k][j] = 0;
				 }//初始化chess数组
			
			for(int i=0;i<m;i++) {
				for(int j=0;j<m;j++){
					chess[i][j]=totalchess[x][j];
				}
  			 x++;
  			 }
			
			N--;  

			new sudoku().solveSudoku(chess,m,totalchess);
			
		}
		

		printFuntxt(totalchess,n,m,fileoutName);
	}

	public static int[][] readFile(File file,int[][] totalchess){     //把文件读入一个大数组
		  StringBuilder result = new StringBuilder();
		  String s = null;
		  for(int k = 0; k < totalchess.length ;k++ )
				 for(int j =0; j < totalchess[k].length; j ++){
					 totalchess[k][j] = 0;
				 }
		  try{
			  int i=0;
			  BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			  while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				  if(s.equals(""));
				  else {
					  String[] temp = s.split(" ");
					  for(int j=0;j<temp.length;j++){
						  totalchess[i][j]=Integer.parseInt(temp[j]);   
				  	}
					  i++;
				  }
			  }
			 br.close(); 
		  }catch(Exception e){
		   e.printStackTrace();
		  }
		  return totalchess;
  }
	
	
	public void solveSudoku(int[][] chess,int m,int[][]totalchess) {
		
		if (chess == null || chess.length != m || chess[0].length != m) {
			return ;
		}
		
		if (solve(chess, 0, 0,m)) {   // 打印结果
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < m; j++) { 
					totalchess[y][j]=chess[i][j];
					//System.out .print(totalchess[y][j]);
				}
				//System.out .println();
				y++;
			}
		}
	}
	
	 //打印宫格
    private static void printFuntxt(int[][] totalchess,int n,int m,String fileout) {

    	File file=new File(fileout);
    	try (PrintWriter output=new PrintWriter(file)){
    		
    		for(int i=0;i<m*n;i++) {
    			
    			if(i%m==0&&i!=0)
                	output.println();
    			
                for(int j=0;j<m;j++) {
                	if(j!=m-1)
                		output.print(totalchess[i][j]+" ");
                	else if(i!=m*n-1)
                		output.print(totalchess[i][j]+"\n");
                	else
                		output.print(totalchess[i][j]);
                }
    		}
		} 
    		catch (FileNotFoundException e) {
			e.printStackTrace();
    		}
    	}

	public boolean solve(int[][] chess, int i, int j,int m) {  //同一行按列搜，若j=9，则跳到下一行搜,若宫格填完返回true

		if (j == m) {
			if (i == m-1)
				return true;
			i++;
			j = 0;
		}
		
		if (chess[i][j] != 0) {
			return solve(chess, i, j + 1,m);
		}

		for (char k = 1; k <= m; k++) {
			if (isValid(chess, i, j, k,m)) {
				chess[i][j] = k;
				if (solve(chess, i, j + 1,m))
					return true;
				else				
					chess[i][j] = 0;
			}
		}
		return false;
	}

	
	public boolean isValid(int[][] chess, int i, int j, char c,int m) {  //有效空格

		for (int k = 0; k < m; k++) {
			
			if (chess[i][k] != 0 && chess[i][k] == c)   //按行搜
				return false;

			if (chess[k][j] != 0 && chess[k][j] == c)   //按列搜
				return false;
			
			if(m==4) {   //按宫搜
				if (chess[i / 2 * 2 + k / 2][j / 2 * 2 + k % 2] != 0 && 
					chess[i / 2 * 2 + k / 2][j / 2 * 2 + k % 2] == c)
					return false;
			}
			
			else if(m==6) {
				if (chess[i / 2 * 2 + k / 3][j / 3 * 3 + k % 3] != 0 && 
						chess[i / 2 * 2 + k / 3][j / 3 * 3 + k % 3] == c)
						return false;
			}
			
			else if(m==8) {
				if (chess[i / 4 * 4 + k / 2][j / 2 * 2 + k % 2] != 0 && 
						chess[i / 4 * 4 + k / 2][j / 2 * 2 + k % 2] == c)
						return false;
			}
			else if(m==9) {
				if (chess[i / 3 * 3 + k / 3][j / 3 * 3 + k % 3] != 0 && 
						chess[i / 3 * 3 + k / 3][j / 3 * 3 + k % 3] == c)
						return false;
			}
		}
		return true;
	}
}
