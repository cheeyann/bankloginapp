package com.example.xinbank;

import java.util.Random;

public class OTUgenerator {
    Random rnd = new Random();
    String[] strArray = new String[8];
    String a = "";
    int k=0;
    //k control which charactor should produce

    public OTUgenerator() {
    }

    public String getOTU() {
        StringBuilder otustr = new StringBuilder();
        String str;
        Generator();
        for (int i=0;i<strArray.length;i++){
            otustr.append(strArray[i]);
        }
        str = otustr.toString();
        return str;
    }

    public void Generator() {
        for(int j=0; j<2 ; j++){
            for(int i=0; i<4 ; i++){
                switch (k){
                    case 0:
                        //0-9
                        a = String.valueOf(rnd.nextInt(10));
                        break;
                    case 1:
                        String chars1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                        a = String.valueOf(chars1.charAt(rnd.nextInt(chars1.length()))) ;
                        break;
                    case 2:
                        String chars2 = "abcdefghijklmnopqrstuvwxyz";
                        a = String.valueOf(chars2.charAt(rnd.nextInt(chars2.length()))) ;
                        break;
                    case 3:
                        String chars3 = "!?@#$%&*()[]{}+><^=";
                        a = String.valueOf(chars3.charAt(rnd.nextInt(chars3.length()))) ;
                        break;
                }
                //call and put inside
                k++;
                if(k>3){k=rnd.nextInt(4);}
                putInsideArray(strArray,a);
            }
        }
    }
    //put char in random empty of array
    //a = case choose
    public String[] putInsideArray(String[]strarray, String randomchar){
        int count = 0;
        int index = rnd.nextInt(strarray.length);
        boolean notfinish = true;
        //randomly pick 5 places, 3 left str will ascending order placed
        if(count<5){
            //loop until find an random empty space in the array
            while(notfinish){
                if(strarray[index]==null){
                    strarray[index]=a;
                    count++;
                    notfinish = false;
                }else if(strarray[index]!=null){
                    index = rnd.nextInt(8);
                    notfinish = true;
                }
            }}else{
            for(int j=0; j<strarray.length ; j++){
                if(strarray[j]==null){
                    strarray[j]=a;
                    count++;
                    break;
                }
            }
        }
        return strarray;
    }
}
