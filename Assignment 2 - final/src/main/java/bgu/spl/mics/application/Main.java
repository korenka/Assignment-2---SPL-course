package bgu.spl.mics.application;

import bgu.spl.mics.JsonParse;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		Gson gson = new Gson();
		Reader reader = new FileReader(args[0]);
		JsonParse input =gson.fromJson(reader, JsonParse.class);
		Diary diary = Diary.getInstance();

		Thread leiaThread=new Thread(new LeiaMicroservice(input.getAttacks(), input.getEwoks()));
		Thread LandoThread=new Thread(new LandoMicroservice(input.getLando()));
		Thread R2D2Thread=new Thread(new R2D2Microservice(input.getR2D2()));
		Thread HanSoloThread=new Thread(new HanSoloMicroservice());
		Thread C3POThread=new Thread(new C3POMicroservice());

		HanSoloThread.start();;
		C3POThread.start();
		R2D2Thread.start();
		LandoThread.start();
		leiaThread.start();

		try {
			LandoThread.join();
			HanSoloThread.join();;
			R2D2Thread.join();
			C3POThread.join();
			leiaThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		diary.writeOutput(args[1]);
//		diary.writeOutput("/home/spl211/Desktop/SPL211/SPL211/Output.json");
	}
}
