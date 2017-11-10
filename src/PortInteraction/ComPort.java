package PortInteraction;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortException;

public class ComPort {

	private static SerialPort serialPort1, serialPort2;

	public static void initializePorts() {
		serialPort1 = new SerialPort("COM2");
		serialPort2 = new SerialPort("COM3");
	}

	public static void setStartParams(int numberOfPort, TextArea area) throws SerialPortException {
		SerialPort port = getPort(numberOfPort);
		port.openPort();
		port.addEventListener(new PortReader(port, area), SerialPort.MASK_RXCHAR);
	}

	public static void setParams(int speed, int numberOfPort) throws SerialPortException {
		SerialPort port = getPort(numberOfPort);
		port.setParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	}

	private static SerialPort getPort(int numberOfPort) {
		switch (numberOfPort) {
		case 1:
			return serialPort1;
		case 2:
			return serialPort2;
		}
		return null;
	}

	public static void writeData(int numberOfPort, String data)
			throws SerialPortException, UnsupportedEncodingException, InterruptedException {
		long waitingTime = 50;
		byte jamSignal = (byte) 0x7F;
		for(int i = 0; i < 10; i++){
			byte [] message = tobyte(splittingIntoPacket(data.getBytes("UTF-8")));
			SerialPort port = getPort(numberOfPort);
			while(waitFreedomOfChannel()) {}; // проверить зан€тость канала
			port.writeBytes(message);
			Thread.currentThread().sleep(waitingTime); //выждать окно коллизий
			if(!checkCollision()) // проверка возникновени€ коллизии
				break;
			port.writeByte(jamSignal);
			Thread.currentThread().sleep(new Random().nextInt((int)Math.pow(2, Math.min(10, i))) * 3); //случайна€ задержка
			if(i == 9) {
				JOptionPane.showMessageDialog(null, "ѕопытки закончились");
				break;
			}
		}
	}

	private static boolean waitFreedomOfChannel() {
		Date date = new Date();
		return date.getTime() % 2 == 0;
	}

	private static boolean checkCollision() {
		Date date = new Date();
		return date.getTime() % 2 != 0;
	}

	private static Byte[] splittingIntoPacket(byte[] data) {
		ArrayList<Byte> packet = new ArrayList<Byte>();
		Byte sourceAddress = 0x3E;
		Byte destinitionAddress = 0x4A;
		Byte symbolEND = 0x7E;
		Byte symbolESC = 0x7D;

		packet.add(symbolEND);
		packet.add(destinitionAddress);
		packet.add(sourceAddress);

		for (int i = 0; i < data.length; i++) {
			if (data[i] == symbolEND) {
				packet.add(symbolESC);
				packet.add((byte) 0x5E);
			} else {
				packet.add(data[i]);
				if (data[i] == symbolESC)
					packet.add((byte) 0x5D);
			}
		}
		packet.add(symbolEND);
		return packet.toArray(new Byte[packet.size()]);
	}

	public static byte[] tobyte(Byte[] arrayByte) {
		byte[] array = new byte[arrayByte.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = arrayByte[i];
		}
		return array;
	}
}
