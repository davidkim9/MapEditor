package stormgate.io.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BufferWriter {
	private BufferedOutputStream b;
	
	public BufferWriter(OutputStream fis) {
		b = new BufferedOutputStream(fis);
	}
	
	public void writeInt(int v) {
		Serialize s = new Serialize();
		s.writeInt(v);
		try {
			b.write(s.bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDouble(double v) {
		Serialize s = new Serialize();
		s.writeDouble(v);
		try {
			b.write(s.bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeBoolean(boolean v) {
		Serialize s = new Serialize();
		s.writeBoolean(v);
		try {
			b.write(s.bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeString(String v) {
		Serialize s = new Serialize();
		s.writeString(v);
		try {
			b.write(s.bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeUTF(String v){
		writeString(v);
	}
	
	public void write(byte[] v){
		try {
			b.write(v);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
