import java.io.*;
import java.util.BitSet;


 public class ip_count{
     public static void main(String[] args) throws IOException {

        long timestart = System.currentTimeMillis();
        
	String filename = args.length < 1 ? "ip_list.txt": args[0];

	Integer counter=0; //Кол-во уникальных ip

	Integer  x=-1,y=-1,z=-1,t=-1; //индексы 4-х мерного массива (элементы ip адреса x.y.z.t)
	BitSet[][][] m_ip = new BitSet[256][256][256]; //4-х мерный массив битов (помечаем попавшиеся ip)
	
        // Открываем файл будет автоматически закрыт по ошибке
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { 
            String line;
            String[] s;
            for (int n = 1; (line = reader.readLine()) != null; ++n) {
               
		s = line.split("[.]+"); //разбиваем строку в массив строк
		if(s.length<4){
                    System.out.println(n +": skip '"+line+"'"); 
                    continue;
                }
		x=Integer.valueOf(s[0].trim()); //1-ое число ip x.y.z.t
		y=Integer.valueOf(s[1].trim()); //2-ое
		z=Integer.valueOf(s[2].trim()); //3-ое
		t=Integer.valueOf(s[3].trim()); //4-ое
				
		if(m_ip[x][y][z] == null) m_ip[x][y][z]= new BitSet(256);
		if(!m_ip[x][y][z].get(t)) {
                    counter++; //если ip не отмечен в массиве увеличим счетчик
                    m_ip[x][y][z].set(t); //отметим в массиве попавшийся ip
                }
                
                //наблюдаем процесс каждые 10000000 строк
		if(n % 10000000==0) {
                    System.out.println(n + " rows processed. "+counter+" unique ip-address. "+(System.currentTimeMillis()-timestart)+" ms"); 
                }				
            }
        }catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
		
	System.err.println("In all: "+counter+" unique ip-address in the file" );
    }
 }
