package stormgate.io.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferReader {
	private BufferedInputStream b;
	//private int position = 0;
	
	public BufferReader(FileInputStream f) {
		b = new BufferedInputStream(f);
		//position = 0;
	}
	
	public int readInt() {
		int length = 4;
		byte[] ba = new byte[length];
		try {
			b.read(ba, 0, length);
			//position += length;
			Serialize s = new Serialize();
			s.write(ba);
			return s.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public double readDouble() {
		int length = 8;
		byte[] ba = new byte[length];
		try {
			b.read(ba, 0, length);
			//position += length;
			Serialize s = new Serialize();
			s.write(ba);
			return s.readDouble();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean readBoolean() {
		int length = 1;
		byte[] ba = new byte[length];
		try {
			b.read(ba, 0, length);
			//position += length;
			Serialize s = new Serialize();
			s.write(ba);
			return s.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String readString() {
		try {
			byte[] stringLead = new byte[2];
			b.read(stringLead, 0, 2);
			int multiplier = stringLead[0];
			int multiple = stringLead[1];
			int stringLength = (multiplier*256)+multiple;
			//int length = stringLength + 2;
			
			byte[] ba = new byte[stringLength];
			b.read(ba, 0, stringLength);
			//position += length;
			Serialize s = new Serialize();
			s.write(ba);
			return s.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String readUTF(){
		return readString();
	}
	
	public byte[] read(int length){
		try {
			byte[] ba = new byte[length];
			b.read(ba, 0, length);
			//position += length;
			return ba;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void close(){
		try {
			b.close();
			b = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
