package PortInteraction;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import GUI.WindowChanges;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class PortReader implements SerialPortEventListener {

	SerialPort serialPort;
	TextArea area;
	Byte symbolESC = 0x7D;
	Byte myAddress = 0x4A;

	public PortReader(SerialPort serialPort, TextArea area) {
		this.serialPort = serialPort;
		this.area = area;
	}

	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() >= 0) {
			try {
				Platform.runLater(new WindowChanges(area, unpuckPacket(serialPort.readBytes())));
			} catch (SerialPortException | UnsupportedEncodingException ex) {
				System.out.println(ex);
			}
		}
	}

	// функция распаковки пакета
	private String unpuckPacket(byte[] packet) throws UnsupportedEncodingException {
		ArrayList<Byte> unpackedData = new ArrayList<Byte> ();
		boolean flag = false;

		for (int i = 0; i < packet.length; i++) {
			switch(packet[i]) {
			//Проверка END-символа
			case 0x7E:
				//Возврат данных если конец пакета
				if(flag)
					return getStringFromBytes(unpackedData);
				//Проверка правильности доставки пакета
				if(packet[i+1] == myAddress)
					i+=2;
				else
					return getStringFromBytes(toByteArray("Data not for me".getBytes("UTF-8")));
				flag = true;
				break;
			case 0x7F:
				return getStringFromBytes(toByteArray("This message is bad".getBytes("UTF-8")));
			//Проверка ESC-символа
			case 0x7D:
				if(packet[i+1] == 0x5E)
					unpackedData.add((byte) 0x7E);
				if(packet[i+1] == 0x5D)
					unpackedData.add(packet[i]);
				i++;
				break;
			//Копирование данных из пакета
			default:
				unpackedData.add(packet[i]);
				break;
			}
		}
		return null;
	}
	
	private String getStringFromBytes(ArrayList<Byte> data) throws UnsupportedEncodingException {
		Byte [] dataIntoBytes = data.toArray(new Byte[data.size()]);
		return new String(ComPort.tobyte(dataIntoBytes), "UTF-8");
	}

	private ArrayList<Byte> toByteArray(byte[] array) {
		ArrayList<Byte> arrayOfByte = new ArrayList<Byte>();
		for (int i = 0; i < array.length; i++)
			arrayOfByte.add(array[i]);
		return arrayOfByte;
	}
}
