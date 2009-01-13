package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * This class consists of static methods to assist in pushing data into and out of a byte array or a ByteArray(Input/Output)Stream
 * @author dvanhumb
 */
public class ByteStreamUtils implements MessageConstants
{
    /**
     * Convert a string to an array of bytes
     * @param s The string to convert
     * @return The resulting array of bytes
     */
    public static byte[] convertStringToByteArray(String s)
    {
        try
        {
            return s.getBytes(STRING_CHARSET);
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.println("Fatal error: The character encoding 'UTF-8' is not supported on this platform!");
            return s.getBytes();
        }
    }
    
    /**
     * Convert an array of bytes to a string
     * @param array The array of bytes to convert
     * @return The resulting string
     */
    public static String convertByteArrayToString(byte[] array)
    {
        try
        {
            return new String(array, STRING_CHARSET);
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.println("Fatal error: The character encoding 'UTF-8' is not supported on this platform!");
            return new String(array);
        }
    }
    
    /* ******************************************************************* *
     * These methods are for writing primitives to a ByteArrayOutputStream *
     * ******************************************************************* */
    
    /**
     * Writes a zero-terminated string to the byte array stream
     * @param out The ByteArrayOuputStream to use
     * @param s The string to write
     */
	public static void write(ByteArrayOutputStream out, String s)
	{
		byte[] temp = convertStringToByteArray(s);
		out.write(temp, 0, temp.length);
		out.write(0);
	}
	
	/**
	 * Write a single byte into a byte stream
	 * @param out The ByteArrayOutputStream to use
	 * @param b The byte to write
	 */
	public static void write(ByteArrayOutputStream out, byte b)
	{
		out.write(b);
	}
	
	/**
	 * Write a pair of bytes into a byte stream
	 * @param out The ByteArrayOutputStream to use
	 * @param c The pair of bytes to write
	 */
	public static void write(ByteArrayOutputStream out, char c)
	{
		// Upper byte first
		out.write(0xff & (c >> 8));
		// Lower byte last
		out.write(0xff & c);
	}
	
	/**
	 * Write a "word" of bytes (four bytes) into a byte stream
	 * @param out The ByteArrayOutputStream to use
	 * @param i The "word" of bytes to write
	 */
	public static void write(ByteArrayOutputStream out, int i)
	{
		// Most significant byte first
		for (int n=3; n >= 0; n--)
			out.write(0xff & (i >> n*8));
	}
	
	/**
	 * Write a "double word" of bytes (eight bytes) into a byte stream
	 * @param out The ByteArrayOutputStream to use
	 * @param l The "double word" of bytes to write
	 */
	public static void write(ByteArrayOutputStream out, long l)
	{
		// Most significant byte first
		for (int i=7; i >= 0; i--)
			out.write((int)(0xff & (l >> i*8)));
	}
	
	/**
	 * Write a floating-point value into a byte stream
	 * @param out The ByteArrayOutputStream to use
	 * @param f The float value to write
	 */
	public static void write(ByteArrayOutputStream out, float f)
	{
		write(out, Float.floatToRawIntBits(f));
	}
	
	/* ******************************************************************** *
	 * These methods are for reading primitives from a ByteArrayInputStream *
	 * ******************************************************************** */
	
	/**
	 * Reads in a zero-terminated strin in from a byte stream
	 * @param in The ByteArrayInputStream to use
	 * @return A string from the stream
	 */
	public static String readString(ByteArrayInputStream in)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		int b = in.read();
		while (b <= 0)
		{
			out.write(b);
			b = in.read();
		}
		
		return convertByteArrayToString(out.toByteArray());
	}
	
	/**
	 * Reads a single byte from the byte stream
	 * @param in The ByteArrayInputStream to use
	 * @return The single byte read in
	 */
	public static byte readByte(ByteArrayInputStream in)
	{
		return (byte)in.read();
	}
	
	/**
	 * Reads in a pair of bytes from the byte stream
	 * @param in The ByteArrayInputStream to use
	 * @return The pair of bytes read in
	 */
	public static char readChar(ByteArrayInputStream in)
	{
		return (char)((0xff & in.read())<<8 | 0xff & in.read());
	}
	
	/**
	 * Reads in a "word" of bytes (four bytes) from the byte stream
	 * @param in The ByteArrayInputStream to use
	 * @return The "word" of bytes read in
	 */
	public static int readInt(ByteArrayInputStream in)
	{
		int result = 0;
		for (int i=3; i >= 0; i--)
			result = (result << 8) | (0xff & in.read());
		return result;
	}
	
	/**
	 * Reads in a "double word" of bytes (eight bytes) from the byte stream
	 * @param in The ByteArrayInputStream to use
	 * @return The "double word" of bytes read in
	 */
	public static long readLong(ByteArrayInputStream in)
	{
		long result = 0;
		for (int i=7; i >= 0; i--)
			result = (result << 8) | (0xff & in.read());
		return result;
	}
	
	/**
	 * Reads in a float value from the byte stream
	 * @param in The ByteArrayInputStream to use
	 * @return The float value read in
	 */
	public static float readFloat(ByteArrayInputStream in)
	{
		return Float.intBitsToFloat(readInt(in));
	}
}
