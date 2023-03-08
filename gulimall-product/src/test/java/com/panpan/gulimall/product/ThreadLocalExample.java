package com.panpan.gulimall.product;

import lombok.Data;

import java.lang.ref.WeakReference;
import java.util.BitSet;

/**
 * @author panpan
 * @create 2021-08-16 下午7:56
 */
public class ThreadLocalExample {
    @Data
    static class Animal {
        private String name="狮子";

        @Override
        protected void finalize() throws Throwable {
            System.out.println("gg");
            super.finalize();
        }
    }

    static class zoo extends WeakReference<Animal> {


        public zoo(Animal animal) {
            super(animal);
        }

    }

    public static String replaceSpace(StringBuffer str) {
        int P1 = str.length() - 1;
        for (int i = 0; i <= P1; i++){
            if (str.charAt(i) == ' ')
                str.append("  ");}

        int P2 = str.length() - 1;
        while (P1 >= 0 && P2 > P1) {
            char c = str.charAt(P1--);
            if (c == ' ') {
                str.setCharAt(P2--, '0');
                str.setCharAt(P2--, '2');
                str.setCharAt(P2--, '%');
            } else {
                str.setCharAt(P2--, c);
            }
        }
        return str.toString();
    }

    public static int startIndex(String str) {
        boolean hasM=false;
        for (int i = 0; i < str.length()-1; i++) {
            if (str.charAt(i)=='M'){
                hasM=true;
            }else if(hasM&&str.charAt(i)=='T'){
                return i;
            }
        }
        return -1;
    }

    public static int finaIndex(String str) {
        boolean hasT=false;
        for (int i = str.length()-1; i >=0; i--) {
            if (str.charAt(i)=='T'){
                hasT=true;
            }else if(hasT&&str.charAt(i)=='M'){
                return i;
            }
        }
        return -1;
    }

    public static String mtDecode(String str) {
       int start=startIndex(str);
       int fianl=finaIndex(str);
       if(start==-1||fianl==-1||start<=fianl){
           return "输入不合法";
       }

       return str.substring(start+1,fianl);
    }

public static int[] whereToGo(int areas,int[][] intention){
            if(intention.length>areas)
                return new int[]{-1};

            BitSet bitSet = new BitSet(areas);
            int[] result = new int[intention.length];
            for (int i = 0; i < intention.length; i++) {
            flag:for (int j = 0; j < intention[i].length; j++) {
                if(!bitSet.get(intention[i][j]-1)){
                    bitSet.set(intention[i][j]-1);
                    result[i]=intention[i][j];
                    break flag;
                }
            }

        }
        return result;

}




    public static void main(String[] args) throws InterruptedException {
        int[] ints = whereToGo(5, new int[][]{{3, 2, 1, 4, 5}, {3, 5, 4, 2, 1}, {1, 3, 2, 4, 5}, {1, 5, 3, 4, 2}, {2, 3, 4, 1, 5}});
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }


    }


}
