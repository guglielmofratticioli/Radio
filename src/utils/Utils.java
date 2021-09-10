package utils;

public class Utils {
    public static void handleProcedure(Procedure procedure, Boolean printStacktrace) { 
        try{ 
            procedure.invoke();
        } catch(Exception e) {
            if(printStacktrace) { 
                e.printStackTrace();
            }
        }
    }


    public static short ByteToShort(byte b1, byte b2, boolean isLittleEndian , boolean isSigned){
        if(isLittleEndian){
            if(isSigned)
                return (short) ((b2 << 8) | (b1 & 0xFF));
            else
                return (short) ((b2 & 0xFF) << 8 | (b1 & 0xFF));
        } else
        {
            if(isSigned)
                return (short) ((b1 << 8) | (b2 & 0xFF));
            else
                return (short) ((b1 & 0xFF) << 8 | (b2 & 0xFF));
        }        
    }
    public static byte[] ShortToByte(short value){
        String word = Integer.toBinaryString(value);
        while(word.length() < 16)
            word = "0" + word;
        if(value <0)
            word = word.substring(16);
        String bleft = word.substring(0,8);
        String bright = word.substring(8);
        byte[] bytes = { parse2CByte(bleft) , parse2CByte(bright) };
        return bytes;
    }

    public static byte parse2CByte(String str ) {
        boolean isNegative = false;
        if(str.charAt(0) == '1'){
            isNegative = true; 
            str = str.substring(0,0) + '0' + str.substring(1);
        }
        if(!isNegative) return Byte.parseByte(str,2);
        else return   (byte)(Integer.parseInt(str,2) -128) ; 
    }
    
}
