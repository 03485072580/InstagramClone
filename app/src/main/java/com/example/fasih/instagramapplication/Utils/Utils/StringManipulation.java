package com.example.fasih.instagramapplication.Utils.Utils;

/**
 * Created by Fasih on 10/20/18.
 */

public class StringManipulation {
    public static String expandUsername(String userName){
        userName=userName.replace("."," ");
        return userName;
    }

    public static String condenseUsername(String username){
        return username.replace(" ",".");
    }

    public static String getTags(String tag){
        Boolean foundTag=false;
        if(tag.contains("#")){
            StringBuilder strBuilder=new StringBuilder(tag.length());
            char[] charArray=tag.toCharArray();
            for(int i=0;i<charArray.length;i++){
                if(charArray[i]=='#'){
                    foundTag=true;
                    strBuilder.append(charArray[i]);
                }
                else{
                    if(foundTag){
                        strBuilder.append(charArray[i]);
                    }
                }
                if(charArray[i]==' '){
                    foundTag=false;
                    try{
                        strBuilder.deleteCharAt(i);
                    }catch(IndexOutOfBoundsException exc){exc.printStackTrace();}
                }
            }
            return strBuilder.toString().replace("#",",#");
        }

       return null;
    }
}
