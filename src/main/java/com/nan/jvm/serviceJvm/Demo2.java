package com.nan.jvm.serviceJvm;

public class Demo2 {

    public static void main(String[] args) {
        String s1 ="a";
        String s2 ="b";
        String s3 ="ab";
        String s4 =s1+s2;   //--> new StringBuilder().append(s1).append(s2).toString();  s4= new String("ab")
        String s5= "a"+"b";
        String s6="a"+s3;

        System.out.println(s3==s4);
        System.out.println(s4==s5);
        System.out.println(s3==s5);
        System.out.println(s3==s6);


    }
}
