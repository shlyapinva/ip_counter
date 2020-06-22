import java.io.*;
import java.util.BitSet;

 public class ip_count{
     public static void main(String[] args) throws IOException {
	 
	 	String filename = args.length < 1 ? "ip_list.txt": args[0];
        String MemoryError = args.length < 2 ? "0": args[1];

		Integer counter=0; //Кол-во уникальных ip
		
		
    // Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
    // Если не хватает памяти сканируем файл 2 раза
        if(MemoryError!="0"){
            counter =ip_count_range(filename,  0,128);
            counter+=ip_count_range(filename,129,256);                        	
        }else    
            counter =ip_count_range(filename,0,256);
		
		
		System.err.println("In all: "+counter+" unique ip-address in the file" );
//		System.err.println("Error line: "+counter_error+"" );
    }

	//считает кол-во уникальных ip v4 адресов в файле file первое число которых находятся в диапазоне [start,end]
	public static Integer ip_count_range(String filename, Integer start, Integer end) throws IOException {
		Integer counter=0; //Кол-во уникальных ip
		Integer counter_error=0; // Кол-во плохих строк

		Integer  x=-1,y=-1,z=-1,t=-1; //индексы 4-х мерного массива (элементы ip адреса x.y.z.t)
		BitSet[][][] m_ip = new BitSet[end-start+1][256][256]; //4-х мерный массив битов (помечаем попавшиеся ip)
		for (int i1 = 0; i1<end-start+1; i1++)
			for (int i2 = 0; i2<256; i2++)
				for (int i3 = 0; i3<256; i3++)
					m_ip[i1][i2][i3]= new BitSet(256);
		
        // Открываем файл будет автоматически закрыт по ошибке
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { 
            String line;
			String[] s;
            for (int n = 1; (line = reader.readLine()) != null; ++n) {
			    if(n % 100==0) System.out.println(n + ": rows processed  "); //наблюдаем прогресс каждые 100 строк
				
				s = line.split("[.]+"); //разбиваем строки в массив строк
				if(s.length<4){System.out.println(n +": skip '"+line+"'"); counter_error++; continue;}
				x=Integer.valueOf(s[0].trim()); //1-ое число ip x.y.z.t
				y=Integer.valueOf(s[1].trim()); //2-ое
				z=Integer.valueOf(s[2].trim()); //3-ое
				t=Integer.valueOf(s[3].trim()); //4-ое
				
//				if(x<start||x>end) System.err.println(n +": skip '"+line+"'");
				if(x<start||x>end) continue;
				if(!ckip(x,y,z,t)) {System.out.println(n +": skip '"+line+"'"); counter_error++; continue;}
				if(!m_ip[x-start][y][z].get(t)) counter++; //если ip не отмечен в массиве увеличим счетчик
				m_ip[x-start][y][z].set(t); //отметим в массиве попавшийся ip
				
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
		return counter;
	}
	//проверям ip 
	public static boolean ckip(Integer x, Integer y, Integer z, Integer t) {
		if(x<0|x>256) return false;
		if(y<0|y>256) return false;
		if(z<0|z>256) return false;
		if(t<0|t>256) return false;
		return true;
	}
 }
