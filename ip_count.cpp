//compile "cpp ip_count.cpp -o ip_count"
#include <bits/stdc++.h> 
using namespace std; 
//Memory 525368 KiB. In all: 8000000000 rows processed.  1000000000  unique ip-address. 1101 sec
int main(int argc, char* argv[]){
    unsigned int start_time =  clock();
    if (argc==1) {printf( "NAME\n ip_count - Print ip-address counts for file \n SYNOPSIS \n ip_count [file].\n"); return 1;}

    FILE *file; 
    file = fopen(argv[1],"r");
    if(file == NULL){ printf("Cannot open file '%s'",argv[1]); return 0;}

    
    long int counter=0; // кол-во уникальных ip
    long int n=0; // кол-во обработанных строк ip
 
    int a[] = {0,0,0,0};// элементы ip адреса (a1.a2.a3.a4)
    int j=0;  // индекс массива "int[] a = {0,0,0,0}"

    bitset<16777216> *pM_ip[256]={NULL}; // массив битов в котором (помечаем попавшиеся ip)
    int x, y; // координаты ip в массиве битов "m_ip"
    
    char c; // символ прочитанный из файла

    // получаем символ из файла
    while((c=getc(file))!=EOF) {
        if(c=='\n'){ 
            //проверим  ip адрес 
            if(j==3){
                // вычислим координаты ip в массиве битов "pM_ip"
                x=a[0];
                y=65536*a[1]+256*a[2]+a[3]; 
    
                // выделим память для массив битов
                if(pM_ip[x] == NULL)
                    pM_ip[x]=new bitset<16777216>;

                // если попавшийся ip не отмечен в массиве
                if(!(*pM_ip[x])[y] ) {
                    counter++; // увеличим счетчик 
                    (*pM_ip[x])[y]=1; //отметим ip в массиве
                }
            }

            n++;  // считаем строки
            
            // обнуляем переменные
            a[0] =0; a[1] =0; a[2] =0; a[3] =0; 
            j=0; 
                
            // наблюдаем процесс каждые 100000000 строк
            if(n%100000000==0) printf("%li rows processed.  %li  unique ip-address. %li sec\n",n, counter, (clock()-start_time)/CLOCKS_PER_SEC);                 

        }
        
        // переводим поток символов в элементы ip адреса (a1.a2.a3.a4)
        if (c == '.'){j++; continue;}
        if(c<'0'||c>'9') continue;
        if(j>3) continue;
        a[j]=10*a[j]+(c-'0');
    }

    fclose(file);

    // освободим память
    for(int i=0;i<256;i++) if(pM_ip[i] != NULL) delete pM_ip[i];
    
    unsigned int end_time =  clock();   
    printf("In all: %li rows processed.  %li  unique ip-address. %li sec\n",n, counter, (clock()-start_time)/CLOCKS_PER_SEC);
    return 0;
};

