import java.io.*;
import java.util.BitSet;

//8000000000 rows processed. 1000000000 unique ip-address. 1083740 ms
 public class ip_count{
     public static void main(String[] args) throws IOException {

        long timestart = System.currentTimeMillis();
        
        if(args.length < 1)  System.out.println("NAME\n ip_count - Print ip-address counts for file \n SYNOPSIS \n ip_count [file].\n");

        long counter=0; // кол-во уникальных ip
        long n=1; // кол-во обработанных строк ip
        
        int[] a = {0,0,0,0};// элементы ip адреса (a1.a2.a3.a4)
        int j=0; // индекс массива "int[] a = {0,0,0,0}"
        
        BitSet[] m_ip = new BitSet[256]; // массив битов в котором (помечаем попавшиеся ip)
        int x, y; // координаты ip в массиве битов "m_ip"

        char c; // символ прочитанный из файла
        
        //Открываем файл
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) { 
            String line;
            int i;
            
            // читаем файл построчно
            for(n=1; (line=reader.readLine())!=null; n++){
            
                //переводим поток символов в элементы ip адреса (a1.a2.a3.a4)
                for (i = 0; i < line.length(); i++) {
                    c=line.charAt(i);
                    if (c == '.'){j++; continue;}
                    if(c<'0'||c>'9') continue;
                    if(j>3) continue;
                    a[j]=10*a[j]+(c-'0');
 //                   System.out.println(a[0]);
                }
                
                //проверим  ip адрес 
                if(j==3){
                    //вычислим координаты ip в массиве битов "m_ip"
                    x=a[0];
                    y=65536*a[1]+256*a[2]+a[3]; 
        
                    //Выделим память для массив битов
                    if(m_ip[x] == null) 
                        m_ip[x]= new BitSet(16777216);
                    
                    //если попавшийся ip не отмечен в массиве
                    if( !m_ip[x].get(y) ) {
                        counter++; // увеличим счетчик 
                        m_ip[x].set(y); //отметим ip в массиве
                    }
                }
                 
                //Обнуляем переменные
                a[0] =0; a[1] =0; a[2] =0; a[3] =0; 
                j=0; 
            
                //наблюдаем процесс каждые 100000000 строк
                if(n % 100000000==0) 
                    System.out.println(n + " rows processed. "+counter+" unique ip-address. "+(System.currentTimeMillis()-timestart)+" ms"); 
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Cannot open file.");
        }
        
        System.out.println("In all: "+n+ " rows processed. "+counter+" unique ip-address. "+(System.currentTimeMillis()-timestart)+" ms"); 
    }
 }
