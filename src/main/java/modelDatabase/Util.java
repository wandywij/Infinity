/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author wandy
 */
public class Util {
    
    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MMyy");
        return format.format(date);
    }
}
