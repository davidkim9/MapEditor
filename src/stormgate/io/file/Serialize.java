package stormgate.io.file;

import java.util.ArrayList;

public class Serialize
{
	
	public ArrayList<Character> serializeByte;
	public int position = 0;
	
	/**
	 * Initializes byte array
	 */
	public Serialize()
	{
		serializeByte = new ArrayList<Character>();
	}
	
	public int size(){
		return serializeByte.size();
	}
	
	/**
	 * Adds an integer value into a byte(char) array
	 * @param value Integer
	 */
	public void writeInt(int value)
	{
		//32 bit integer
		serializeByte.add((char)((value >> 24)%256 & 0xFF));
		serializeByte.add((char)((value >> 16)%256 & 0xFF));
		serializeByte.add((char)((value >> 8)%256 & 0xFF));
		serializeByte.add((char)((value)%256 & 0xFF));
		position += 4;
	}
	
	/**
	 * Writes a boolean to byte array
	 * @param value Boolean
	 */
	public void writeBoolean(boolean value)
	{
		if(value){
			serializeByte.add((char)1);
		}else{
			serializeByte.add((char)0);
		}
	}
	
	/**
	 * Writes a double to byte array
	 * @param value Double
	 */
	public void writeDouble(double value)
	{
		//64 bit double
		long doubleBits = Double.doubleToLongBits(value);
		serializeByte.add((char)((doubleBits >> 56)%256 & 0xFF));
		serializeByte.add((char)((doubleBits >> 48)%256 & 0xFF));
		serializeByte.add((char)((doubleBits >> 40)%256 & 0xFF));
		serializeByte.add((char)((doubleBits >> 32)%256 & 0xFF));
		serializeByte.add((char)((doubleBits >> 24)%256 & 0xFF));
		serializeByte.add((char)((doubleBits >> 16)%256 & 0xFF));
		serializeByte.add((char)((doubleBits >> 8)%256 & 0xFF));
		serializeByte.add((char)((doubleBits)%256 & 0xFF));
	}
	
	/**
	 * Writes a string to byte array
	 * @param value String
	 */
	public void writeString(String value)
	{
		int overhead = value.length();
		serializeByte.add((char)(overhead/256));
		serializeByte.add((char)(overhead%256));
		byte[] stringByte = value.getBytes();
		for(int i = 0; i<stringByte.length ; i++){
			serializeByte.add((char)stringByte[i]);
		}
	}
	
	/**
	 * Reads raw byte array
	 */
	public void writeByte(int value)
	{
		serializeByte.add((char)value);
	}
	
	/**
	 * Reads an Integer
	 * @return Integer
	 */
	public int readInt()
	{
		if(serializeByte.size() >= 4){
			int intValue = (( serializeByte.get(position) << 24) & 0xff000000 ) |
			(( serializeByte.get(position+1) << 16) & 0x00ff0000) |
			(( serializeByte.get(position+2) << 8) & 0x0000ff00) |
			( serializeByte.get(position+3) & 0x000000ff );
			position += 4;
			return intValue;
		}
		return 0;
	}
	
	/**
	 * Reads a boolean
	 * @return Boolean
	 */
	public boolean readBoolean()
	{
		if(serializeByte.size() >= 1){
			int boolValue = (int)serializeByte.get(position);
			position++;
			return boolValue == 1 ? true: false;
		}
		return false;
	}
	
	/**
	 * Reads a character
	 * @return Character
	 */
	public char readChar()
	{
		if(serializeByte.size() >= 1){
			char charValue = serializeByte.get(position);
			position++;
			return charValue;
		}
		return 0;
	}
	
	/**
	 * Reads a double
	 * @return Double
	 */
	public double readDouble()
	{
		if(serializeByte.size() >= 8+position){
			
			long longValue = (long)(0xff & serializeByte.get(position+7)) |
			(long)(0xff & serializeByte.get(position+6)) << 8 |
			(long)(0xff & serializeByte.get(position+5)) << 16 |
			(long)(0xff & serializeByte.get(position+4)) << 24 |
			(long)(0xff & serializeByte.get(position+3)) << 32 |
			(long)(0xff & serializeByte.get(position+2)) << 40 |
			(long)(0xff & serializeByte.get(position+1)) << 48 |
			(long)(0xff & serializeByte.get(position)) << 56;
			
			position += 8;
			return Double.longBitsToDouble(longValue);
		}
		return 0;
	}
	
	/**
	 * Reads a string
	 * @return String
	 */
	public String readString()
	{
		if(serializeByte.size() >= 2+position){
			String byteValue = "";
			int multiplier = readChar();
			int multiple = readChar();
			int stringLength = (multiplier*256)+multiple;
			
			if(serializeByte.size()>=stringLength){
				for(int i = 0; i<stringLength; i++){
					byteValue += serializeByte.get(position+i);
				}
				position += stringLength;
				return byteValue;
			}
		}
		return null;
	}
	
	/**
	 * Sets position to 0
	 */
	public void position()
	{
		position = 0;
	}
	
	/**
	 * Writes raw byte array
	 */
	public void write(String value)
	{
		char[] stringByte = value.toCharArray();
		for(int i = 0; i<stringByte.length ; i++){
			//System.out.println(stringByte[i] +" == "+ ((int)stringByte[i]));
			serializeByte.add((char)stringByte[i]);
		}
	}
	
	public void write(byte[] value)
	{
		for(int i = 0; i<value.length ; i++){
			serializeByte.add((char)value[i]);
		}
	}

	/**
	 * Reads raw byte array
	 * @return
	 */
	public String read()
	{
		String returnString = "";
		for(int i = 0; i<serializeByte.size() ; i++){
			returnString += serializeByte.get(i);
		}
		return returnString;
	}
	
	public byte[] bytes(){
		byte[] b = new byte[serializeByte.size()];
		for(int i = 0; i<serializeByte.size() ; i++){
			b[i] = (byte) serializeByte.get(i).charValue();
		}
		return b;
	}
	
	/**
	 * Removes all bytes
	 */
	public void flush()
	{
		serializeByte = null;
	}
}
