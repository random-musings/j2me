/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * This class handles the saving and loading of game levels from the record store
 * 
 */
public class GameData {
    
    //saved the data to the record store
    //at the requested position
    public static boolean saveData(String repository, byte[] data,int position)
    {
         try
        {
        RecordStore rs = RecordStore.openRecordStore(repository, true);
        int maxRecords = rs.getNumRecords();
        //ensure that we don't attempt to save a level to a postiion that doesn't exist
        if(rs.getNumRecords()<position ||position<1)
            rs.addRecord(data,0, data.length);
        else
            rs.setRecord(position, data, 0, data.length);
        rs.closeRecordStore();
        }catch(RecordStoreException rse)
        {
            System.out.print("rse "+rse.getMessage());
            return false;
        }
         return true;
    }
    
    
    //retrieve the level from the record store
    public static byte[] retrieveData(String repository,int position)
    {
        try
        {
            byte[] data=null;
            RecordStore rs = RecordStore.openRecordStore(repository, true);
            if(rs.getNumRecords()>=position)
                 data =rs.getRecord(position);
            rs.closeRecordStore();
            return data;
        }catch(RecordStoreException rse)
        {
            System.out.print("rse "+rse.getMessage());
            return null;
        }
    }
}
