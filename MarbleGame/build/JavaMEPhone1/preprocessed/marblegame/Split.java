/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import java.util.Vector;


/**
 *
 * @author janiestoy
 */
public class Split {
	public static final String erroneousCharacter ="\t";
	public static final String newLine="\n";
	public static final String comma=",";
    
    	public static boolean parseBoolean(String src)
        {
            if(src==null)
                return false;
            if( src.startsWith("t") ||  src.startsWith("T"))
                return true;
            return false;
        }
	public static Vector parseStringList(String src)
	{
		return split(src,comma);
	}

        public static Vector split(String src,String separator)
        {
            if(src!=null && separator !=null)
            {
                Vector elts = new Vector();
                String tmpString = src;
                int currIndex=tmpString.indexOf(separator);
                while(currIndex>=0)
                {
                   
                    if(currIndex< tmpString.length() && (tmpString.length()-currIndex)>0 )
                    {
                        elts.addElement(tmpString.substring(0,currIndex));
                        tmpString = tmpString.substring(currIndex+separator.length(), tmpString.length());
                        currIndex=tmpString.indexOf(separator);
                    }
                }
                if(tmpString.length()>0)
                  elts.addElement(tmpString);
                return elts;
            }
            return new Vector();
        }

        public static Vector split(String src,char separator)
        {
            Vector elts = new Vector();
            if(src!=null)
            {
                String tmpString = src;
                int currIndex=tmpString.indexOf(separator);
                while(currIndex>0)
                {
                    elts.addElement(tmpString.substring(currIndex));
                    tmpString = tmpString.substring(currIndex+1, tmpString.length()-currIndex);
                    currIndex=tmpString.indexOf(separator);
                }
                elts.addElement(tmpString);
            }
            return elts;
        }
     
}
